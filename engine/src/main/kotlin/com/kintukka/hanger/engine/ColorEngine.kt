package com.kintukka.hanger.engine

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Colour science used by [ColorTools]: sRGB -> CIE Lab (D65), CIEDE2000 distance,
 * nearest-palette lookup and k-means dominant colour extraction.
 */
internal object ColorScience {
    private const val XN = 0.95047
    private const val YN = 1.0
    private const val ZN = 1.08883
    private const val EPSILON = 216.0 / 24389.0 // (6/29)^3
    private const val KAPPA = 24389.0 / 27.0
    private const val POW25_7 = 6103515625.0 // 25^7

    private const val MAX_SAMPLES = 20_000
    private const val CLUSTERS = 5
    private const val MAX_ITERATIONS = 10
    private const val MIN_SHARE = 0.08
    private const val KMEANS_SEED = 0x5EEDC010L

    /** sRGB 8-bit channel -> linear light. */
    private val LINEAR = DoubleArray(256) { i ->
        val c = i / 255.0
        if (c <= 0.04045) c / 12.92 else ((c + 0.055) / 1.055).pow(2.4)
    }

    private val palette: Array<FashionColor> = FashionColor.entries.toTypedArray()

    /** Lab triples of the palette, precomputed once. */
    private val paletteLab: DoubleArray = DoubleArray(palette.size * 3).also { arr ->
        palette.forEachIndexed { i, c -> rgbToLab(c.rgb, arr, i * 3) }
    }

    private fun f(t: Double): Double = if (t > EPSILON) Math.cbrt(t) else (KAPPA * t + 16.0) / 116.0

    /** Writes L, a, b of [rgb] (0xRRGGBB, alpha ignored) into [out] at [offset]. */
    fun rgbToLab(rgb: Int, out: DoubleArray, offset: Int = 0) {
        val r = LINEAR[(rgb shr 16) and 0xFF]
        val g = LINEAR[(rgb shr 8) and 0xFF]
        val b = LINEAR[rgb and 0xFF]
        val x = (0.4124564 * r + 0.3575761 * g + 0.1804375 * b) / XN
        val y = (0.2126729 * r + 0.7151522 * g + 0.0721750 * b) / YN
        val z = (0.0193339 * r + 0.1191920 * g + 0.9503041 * b) / ZN
        val fx = f(x)
        val fy = f(y)
        val fz = f(z)
        out[offset] = 116.0 * fy - 16.0
        out[offset + 1] = 500.0 * (fx - fy)
        out[offset + 2] = 200.0 * (fy - fz)
    }

    fun nearest(l: Double, a: Double, b: Double): FashionColor {
        var best = 0
        var bestDistance = Double.MAX_VALUE
        for (i in palette.indices) {
            val d = deltaE2000(l, a, b, paletteLab[i * 3], paletteLab[i * 3 + 1], paletteLab[i * 3 + 2])
            if (d < bestDistance) {
                bestDistance = d
                best = i
            }
        }
        return palette[best]
    }

    fun nameFor(rgb: Int): FashionColor {
        val lab = DoubleArray(3)
        rgbToLab(rgb and 0xFFFFFF, lab)
        return nearest(lab[0], lab[1], lab[2])
    }

    private fun rad(deg: Double) = Math.toRadians(deg)

    private fun hueDegrees(b: Double, a: Double): Double {
        if (a == 0.0 && b == 0.0) return 0.0
        val h = Math.toDegrees(atan2(b, a))
        return if (h < 0) h + 360.0 else h
    }

    /** CIEDE2000 colour difference (Sharma, Wu & Dalal 2005), kL = kC = kH = 1. */
    fun deltaE2000(l1: Double, a1: Double, b1: Double, l2: Double, a2: Double, b2: Double): Double {
        val c1 = sqrt(a1 * a1 + b1 * b1)
        val c2 = sqrt(a2 * a2 + b2 * b2)
        val cBar7 = ((c1 + c2) / 2.0).pow(7)
        val g = 0.5 * (1.0 - sqrt(cBar7 / (cBar7 + POW25_7)))
        val a1p = (1.0 + g) * a1
        val a2p = (1.0 + g) * a2
        val c1p = sqrt(a1p * a1p + b1 * b1)
        val c2p = sqrt(a2p * a2p + b2 * b2)
        val h1p = hueDegrees(b1, a1p)
        val h2p = hueDegrees(b2, a2p)

        val dLp = l2 - l1
        val dCp = c2p - c1p
        val cProduct = c1p * c2p
        var dhp = 0.0
        if (cProduct != 0.0) {
            dhp = h2p - h1p
            if (dhp > 180.0) dhp -= 360.0 else if (dhp < -180.0) dhp += 360.0
        }
        val dHp = 2.0 * sqrt(cProduct) * sin(rad(dhp / 2.0))

        val lBarP = (l1 + l2) / 2.0
        val cBarP = (c1p + c2p) / 2.0
        val hBarP = when {
            cProduct == 0.0 -> h1p + h2p
            abs(h1p - h2p) <= 180.0 -> (h1p + h2p) / 2.0
            h1p + h2p < 360.0 -> (h1p + h2p + 360.0) / 2.0
            else -> (h1p + h2p - 360.0) / 2.0
        }
        val t = 1.0 - 0.17 * cos(rad(hBarP - 30.0)) + 0.24 * cos(rad(2.0 * hBarP)) +
            0.32 * cos(rad(3.0 * hBarP + 6.0)) - 0.20 * cos(rad(4.0 * hBarP - 63.0))
        val dTheta = 30.0 * exp(-((hBarP - 275.0) / 25.0).pow(2))
        val cBarP7 = cBarP.pow(7)
        val rc = 2.0 * sqrt(cBarP7 / (cBarP7 + POW25_7))
        val lm = (lBarP - 50.0) * (lBarP - 50.0)
        val sl = 1.0 + 0.015 * lm / sqrt(20.0 + lm)
        val sc = 1.0 + 0.045 * cBarP
        val sh = 1.0 + 0.015 * cBarP * t
        val rt = -sin(rad(2.0 * dTheta)) * rc
        val x = dLp / sl
        val y = dCp / sc
        val z = dHp / sh
        return sqrt(x * x + y * y + z * z + rt * y * z)
    }

    /**
     * k-means (k-means++ init, fixed seed) in Lab over at most [MAX_SAMPLES] opaque pixels;
     * centroids are snapped to the palette and clusters naming the same colour are merged.
     */
    fun dominantColors(argbPixels: IntArray, maxColors: Int): List<Pair<FashionColor, Double>> {
        if (maxColors <= 0 || argbPixels.isEmpty()) return emptyList()
        var opaque = 0
        for (p in argbPixels) if ((p ushr 24) >= 128) opaque++
        if (opaque == 0) return emptyList()

        val step = (opaque + MAX_SAMPLES - 1) / MAX_SAMPLES
        val capacity = (opaque + step - 1) / step
        val lab = DoubleArray(capacity * 3)
        var n = 0
        var seen = 0
        var lastRgb = -1
        for (p in argbPixels) {
            if ((p ushr 24) < 128) continue
            if (seen % step == 0 && n < capacity) {
                val rgb = p and 0xFFFFFF
                if (rgb == lastRgb && n > 0) {
                    lab[n * 3] = lab[(n - 1) * 3]
                    lab[n * 3 + 1] = lab[(n - 1) * 3 + 1]
                    lab[n * 3 + 2] = lab[(n - 1) * 3 + 2]
                } else {
                    rgbToLab(rgb, lab, n * 3)
                    lastRgb = rgb
                }
                n++
            }
            seen++
        }

        val k = min(CLUSTERS, n)
        val centers = DoubleArray(k * 3)
        val rnd = Random(KMEANS_SEED)

        // k-means++ initialisation.
        var centerCount = 1
        val first = rnd.nextInt(n)
        centers[0] = lab[first * 3]; centers[1] = lab[first * 3 + 1]; centers[2] = lab[first * 3 + 2]
        val d2 = DoubleArray(n) { i -> dist2(lab, i, centers, 0) }
        while (centerCount < k) {
            var total = 0.0
            for (i in 0 until n) total += d2[i]
            if (total <= 1e-9) break // every pixel already sits on a centre
            var r = rnd.nextDouble() * total
            var chosen = n - 1
            for (i in 0 until n) {
                r -= d2[i]
                if (r <= 0.0) { chosen = i; break }
            }
            val c = centerCount * 3
            centers[c] = lab[chosen * 3]; centers[c + 1] = lab[chosen * 3 + 1]; centers[c + 2] = lab[chosen * 3 + 2]
            for (i in 0 until n) {
                val d = dist2(lab, i, centers, centerCount)
                if (d < d2[i]) d2[i] = d
            }
            centerCount++
        }

        // Lloyd iterations.
        val assignment = IntArray(n) { -1 }
        val counts = IntArray(centerCount)
        val sums = DoubleArray(centerCount * 3)
        for (iteration in 0 until MAX_ITERATIONS) {
            var changed = false
            for (i in 0 until n) {
                var best = 0
                var bestD = Double.MAX_VALUE
                for (j in 0 until centerCount) {
                    val d = dist2(lab, i, centers, j)
                    if (d < bestD) { bestD = d; best = j }
                }
                if (assignment[i] != best) { assignment[i] = best; changed = true }
            }
            counts.fill(0)
            sums.fill(0.0)
            for (i in 0 until n) {
                val j = assignment[i]
                counts[j]++
                sums[j * 3] += lab[i * 3]; sums[j * 3 + 1] += lab[i * 3 + 1]; sums[j * 3 + 2] += lab[i * 3 + 2]
            }
            for (j in 0 until centerCount) {
                if (counts[j] > 0) {
                    centers[j * 3] = sums[j * 3] / counts[j]
                    centers[j * 3 + 1] = sums[j * 3 + 1] / counts[j]
                    centers[j * 3 + 2] = sums[j * 3 + 2] / counts[j]
                }
            }
            if (!changed) break
        }

        val shares = DoubleArray(palette.size)
        for (j in 0 until centerCount) {
            if (counts[j] == 0) continue
            val color = nearest(centers[j * 3], centers[j * 3 + 1], centers[j * 3 + 2])
            shares[color.ordinal] += counts[j].toDouble() / n
        }
        return palette.indices
            .filter { shares[it] >= MIN_SHARE }
            .sortedWith(compareByDescending<Int> { shares[it] }.thenBy { it })
            .take(maxColors)
            .map { palette[it] to shares[it] }
    }

    private fun dist2(lab: DoubleArray, i: Int, centers: DoubleArray, j: Int): Double {
        val dl = lab[i * 3] - centers[j * 3]
        val da = lab[i * 3 + 1] - centers[j * 3 + 1]
        val db = lab[i * 3 + 2] - centers[j * 3 + 2]
        return dl * dl + da * da + db * db
    }
}

/** Hue families used to judge colour harmony. Neutrals belong to none. */
internal enum class HueFamily(val plural: String) {
    RED("reds"), PINK("pinks"), ORANGE("oranges"), YELLOW("yellows"),
    GREEN("greens"), TEAL("teals"), BLUE("blues"), PURPLE("purples"),
}

/**
 * Rule-based colour harmony. Colour sets are handled as bit masks over [FashionColor.ordinal]
 * so outfit scoring stays allocation-free in the generator and capsule loops.
 */
internal object Harmony {
    private val colors = FashionColor.entries

    private val familyOf: Array<HueFamily?> = Array(colors.size) { i ->
        val c = colors[i]
        if (c.neutral) null else when (c) {
            FashionColor.RED, FashionColor.BURGUNDY -> HueFamily.RED
            FashionColor.PINK, FashionColor.HOT_PINK -> HueFamily.PINK
            FashionColor.ORANGE, FashionColor.RUST -> HueFamily.ORANGE
            FashionColor.YELLOW, FashionColor.MUSTARD -> HueFamily.YELLOW
            FashionColor.GREEN, FashionColor.MINT -> HueFamily.GREEN
            FashionColor.TEAL -> HueFamily.TEAL
            FashionColor.BLUE, FashionColor.LIGHT_BLUE -> HueFamily.BLUE
            FashionColor.PURPLE, FashionColor.LAVENDER -> HueFamily.PURPLE
            else -> null
        }
    }

    /** Circular mean of the palette hues in each family. */
    private val familyHue: DoubleArray = DoubleArray(HueFamily.entries.size).also { arr ->
        for (fam in HueFamily.entries) {
            var sx = 0.0
            var sy = 0.0
            colors.forEachIndexed { i, c ->
                if (familyOf[i] == fam) {
                    sx += cos(Math.toRadians(c.hue.toDouble()))
                    sy += sin(Math.toRadians(c.hue.toDouble()))
                }
            }
            var h = Math.toDegrees(atan2(sy, sx))
            if (h < 0) h += 360.0
            arr[fam.ordinal] = h
        }
    }

    /** The researched colour pairings and palettes from the built-in style guide. */
    private val guide get() = StyleKnowledge.bundled

    private val paletteMasks: List<Pair<Int, Palette>> get() = guide.paletteMasks

    /** Artist's-wheel complements that sit outside the 150–210° hue window of this palette. */
    private val extraComplements: Set<Set<HueFamily>> = setOf(setOf(HueFamily.YELLOW, HueFamily.PURPLE))

    fun bit(c: FashionColor): Int = 1 shl c.ordinal

    fun maskOf(colors: Iterable<FashionColor>): Int {
        var m = 0
        for (c in colors) m = m or bit(c)
        return m
    }

    fun has(mask: Int, c: FashionColor) = mask and bit(c) != 0

    fun hueDistance(a: Double, b: Double): Double {
        val d = abs(a - b) % 360.0
        return if (d > 180.0) 360.0 - d else d
    }

    enum class Relation { SINGLE, ANALOGOUS, COMPLEMENTARY, CONTRASTING }

    fun relationOf(f1: HueFamily, f2: HueFamily): Relation {
        val d = hueDistance(familyHue[f1.ordinal], familyHue[f2.ordinal])
        return when {
            d <= 50.0 -> Relation.ANALOGOUS
            d >= 150.0 || setOf(f1, f2) in extraComplements -> Relation.COMPLEMENTARY
            else -> Relation.CONTRASTING
        }
    }

    fun families(mask: Int): List<HueFamily> {
        var famMask = 0
        var m = mask
        while (m != 0) {
            val i = Integer.numberOfTrailingZeros(m)
            m = m and (m - 1)
            familyOf[i]?.let { famMask = famMask or (1 shl it.ordinal) }
        }
        return HueFamily.entries.filter { famMask and (1 shl it.ordinal) != 0 }
    }

    private fun span(families: List<HueFamily>): Double {
        val hues = families.map { familyHue[it.ordinal] }.sorted()
        var maxGap = 360.0 - hues.last() + hues.first()
        for (i in 1 until hues.size) maxGap = maxOf(maxGap, hues[i] - hues[i - 1])
        return 360.0 - maxGap
    }

    /** Guide verdicts on every colour pair in [mask], strongest first. */
    fun guidePairs(mask: Int): List<ColorPair> {
        val present = colors.filter { has(mask, it) }
        val out = ArrayList<ColorPair>()
        for (i in present.indices) for (j in i + 1 until present.size) guide.colorPair(present[i], present[j])?.let(out::add)
        return out.sortedByDescending { kotlin.math.abs(it.score) * 10 + it.score }
    }

    /** Sum of the guide's adjustments for the colour pairs in [mask] (allocation-free). */
    private fun guideAdjustment(mask: Int): Double {
        val k = guide
        val table = k.colorTable
        val n = colors.size
        var plus = 0.0
        var minus = 0.0
        var outer = mask
        while (outer != 0) {
            val i = Integer.numberOfTrailingZeros(outer)
            outer = outer and (outer - 1)
            var inner = outer
            while (inner != 0) {
                val j = Integer.numberOfTrailingZeros(inner)
                inner = inner and (inner - 1)
                when (table[i * n + j].toInt()) {
                    2 -> plus += 0.03
                    1 -> plus += 0.015
                    -1 -> minus += 0.05
                    -2 -> minus += 0.12
                }
            }
        }
        for ((pm, _) in k.paletteMasks) if (mask and pm == pm) { plus += 0.02; break }
        return minOf(plus, 0.06) - minus
    }

    /** Whether the guide rates some pairing between the two hue families' colours in [mask] as good. */
    private fun guideBlesses(mask: Int, f1: HueFamily, f2: HueFamily): Boolean {
        for (a in colors) {
            if (!has(mask, a) || familyOf[a.ordinal] != f1) continue
            for (b in colors) if (has(mask, b) && familyOf[b.ordinal] == f2 && guide.colorScore(a, b) > 0) return true
        }
        return false
    }

    fun palette(mask: Int): Palette? = paletteMasks.firstOrNull { (pm, _) -> mask and pm == pm }?.second

    fun hasNeutral(mask: Int): Boolean {
        var m = mask
        while (m != 0) {
            val i = Integer.numberOfTrailingZeros(m)
            m = m and (m - 1)
            if (colors[i].neutral) return true
        }
        return false
    }

    /** 0..1 harmony of the colour set [mask]. */
    fun score(mask: Int): Double {
        if (mask == 0) return 1.0
        var neutral = false
        var famMask = 0
        var accents = 0
        var m = mask
        while (m != 0) {
            val i = Integer.numberOfTrailingZeros(m)
            m = m and (m - 1)
            val fam = familyOf[i]
            if (fam == null) neutral = true else {
                famMask = famMask or (1 shl fam.ordinal)
                accents++
            }
        }
        val nf = Integer.bitCount(famMask)
        var s = when (nf) {
            0 -> 0.95
            1 -> 0.90 + if (accents >= 2) 0.02 else 0.0 // tonal dressing
            2 -> {
                val fams = HueFamily.entries.filter { famMask and (1 shl it.ordinal) != 0 }
                when (relationOf(fams[0], fams[1])) {
                    Relation.ANALOGOUS -> 0.82
                    Relation.COMPLEMENTARY -> 0.80
                    else -> if (guideBlesses(mask, fams[0], fams[1])) 0.80 else 0.62
                }
            }
            3 -> {
                val fams = HueFamily.entries.filter { famMask and (1 shl it.ordinal) != 0 }
                if (span(fams) <= 90.0) 0.66 else 0.52
            }
            else -> maxOf(0.15, 0.30 - 0.05 * (nf - 4))
        }
        if (nf > 0 && neutral) s += if (nf <= 3) 0.05 else 0.03
        s += guideAdjustment(mask)
        val distinct = Integer.bitCount(mask)
        if (distinct > 5) s -= 0.03 * (distinct - 5)
        return s.coerceIn(0.0, 1.0)
    }

    /** Adds human-readable colour notes and warnings for [items] (whose colours make up [mask]). */
    fun explain(mask: Int, items: List<Garment>, notes: MutableList<Note>, warnings: MutableList<Note>) {
        if (mask == 0) return
        val fams = families(mask)
        val neutral = hasNeutral(mask)
        // Talk about the pairing on the main pieces (top, bottoms, dress) before shoes and accessories.
        fun weight(c: FashionColor): Int = items.maxOfOrNull { g ->
            when {
                g.colors.firstOrNull() != c -> if (c in g.colors) 1 else 0
                g.category == Category.TOP || g.category == Category.BOTTOM || g.category == Category.ONE_PIECE -> 4
                g.category == Category.LAYER || g.category == Category.OUTERWEAR -> 3
                g.category == Category.SHOES -> 2
                else -> 1
            }
        } ?: 0
        val verdicts = guidePairs(mask).sortedWith(compareByDescending<ColorPair> { kotlin.math.abs(it.score) }.thenByDescending { weight(it.a) + weight(it.b) })
        verdicts.firstOrNull { it.score > 0 }?.let { notes += Note(it.why, if (it.score == 2) 0.92 else 0.8) }
        for (v in verdicts.filter { it.score < 0 }.take(2)) warnings += Note(v.why, if (v.score == -2) 0.78 else 0.5)
        palette(mask)?.let { notes += Note("${it.name} palette: ${it.why.replaceFirstChar(Char::lowercase)}", 0.72) }
        when (fams.size) {
            0 -> notes += Note("An easy all-neutral palette that always works", 0.78)
            1 -> {
                val fam = fams[0]
                val members = FashionColor.entries.filter { has(mask, it) && familyOf[it.ordinal] == fam }
                if (members.size >= 2) {
                    notes += Note("Tonal ${fam.plural} look polished", 0.86)
                }
                val accentItems = items.filter { g -> g.colors.firstOrNull()?.let { familyOf[it.ordinal] == fam } == true }
                if (neutral) {
                    if (accentItems.size == 1) {
                        notes += Note("Neutral base lets the ${Words.phrase(accentItems[0])} shine", 0.9)
                    } else {
                        notes += Note("Neutral base keeps the ${fam.plural} grounded", 0.84)
                    }
                }
            }
            2 -> {
                val a = FashionColor.entries.first { has(mask, it) && familyOf[it.ordinal] == fams[0] }
                val b = FashionColor.entries.first { has(mask, it) && familyOf[it.ordinal] == fams[1] }
                when (relationOf(fams[0], fams[1])) {
                    Relation.ANALOGOUS ->
                        notes += Note("${a.label} and ${b.label.lowercase()} sit side by side on the colour wheel", 0.82)
                    Relation.COMPLEMENTARY ->
                        notes += Note("${a.label} and ${b.label.lowercase()} make a confident complementary pair", 0.82)
                    else -> if (!neutral && !guideBlesses(mask, fams[0], fams[1])) {
                        warnings += Note("${a.label} with ${b.label.lowercase()} is a bold mix — a neutral piece would calm it", 0.45)
                    }
                }
            }
            3 -> if (span(fams) <= 90.0) {
                notes += Note("A rich, analogous palette", 0.7)
            } else {
                warnings += Note("Three strong colours compete — swap one for a neutral", 0.6)
            }
            else -> warnings += Note("${Words.count(fams.size)} colours are fighting for attention — anchor with a neutral", 0.72)
        }
    }
}
