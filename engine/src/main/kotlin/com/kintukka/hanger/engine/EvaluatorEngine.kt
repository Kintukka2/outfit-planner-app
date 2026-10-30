package com.kintukka.hanger.engine

import java.time.LocalDate
import kotlin.math.max
import kotlin.math.min
import kotlin.math.tanh

internal class EvalResult(
    val score: Double,
    val notes: List<Note>,
    val warnings: List<Note>,
    /** Formality spread after letting shoes/outerwear/bags (+1) and accessories (+2) sit above the core. */
    val formalitySpread: Int,
)

/**
 * Scores an outfit from colour harmony, patterns, formality coherence, occasion fit,
 * weather fit, structural completeness and learned affinity.
 *
 * With `explain = false` no strings are built, so the generator and capsule loops stay fast.
 */
internal object Evaluator {
    private const val W_COLOR = 0.35
    private const val W_PATTERN = 0.15
    private const val W_FORMALITY = 0.20
    private const val W_OCCASION = 0.20
    private const val W_WEATHER = 0.30

    private val EMPTY = EvalResult(0.0, emptyList(), listOf(Note("Add some pieces to start an outfit", 1.0)), 0)
    private val guide get() = StyleKnowledge.bundled
    private val categories = Category.entries

    private fun isCore(c: Category) = c == Category.TOP || c == Category.BOTTOM || c == Category.ONE_PIECE || c == Category.LAYER

    // Shared numeric building blocks (also used by the capsule fast path so both always agree).

    fun colorScore(mask: Int): Double = if (mask == 0) 0.85 else Harmony.score(mask)

    fun patternScore(bold: Int, first: Pattern?, second: Pattern?): Double = when {
        bold <= 1 -> 1.0
        bold == 2 -> when (guide.patternScore(first!!, second!!)) {
            2 -> 0.9
            1 -> 0.75
            -1 -> 0.45
            -2 -> 0.3
            else -> if (mixable(first, second)) 0.75 else 0.35
        }
        else -> 0.1
    }

    /** Guide verdict on one pair of garment types, split into lift and cost. */
    fun typeLift(score: Int): Double = when (score) {
        2 -> 0.025
        1 -> 0.012
        else -> 0.0
    }

    fun typeCost(score: Int): Double = when (score) {
        -1 -> 0.06
        -2 -> 0.14
        else -> 0.0
    }

    /** Classic pairings lift a little (capped); clashing pairings cost more, so one bad pair still shows. */
    fun typeAdjustment(lift: Double, cost: Double): Double = (min(lift, 0.05) - cost).coerceAtLeast(-0.3)

    /**
     * Identity below 0.9, then eases towards 1 so great outfits keep their order instead of all saturating at 1.
     * Monotonic, so thresholds below 0.9 (the capsule counter's 0.55) are unaffected.
     */
    fun softCap(x: Double): Double = if (x <= 0.9) x else 0.9 + 0.1 * tanh((x - 0.9) / 0.1)

    fun formalityScore(spread: Int): Double = when {
        spread <= 1 -> 1.0
        spread == 2 -> 0.7
        spread == 3 -> 0.4
        else -> 0.25
    }

    /** Formality of a piece as judged against the core (flex pieces may sit a little above it). */
    fun adjustedFormality(formality: Int, category: Category, coreMax: Int): Int {
        val flex = flexAbove(category)
        return if (flex > 0 && formality > coreMax && formality <= coreMax + flex) coreMax else formality
    }

    /** Weighted mean blended with the weakest component, so one real problem can't hide behind good averages. */
    fun combine(color: Double, pattern: Double, formality: Double, occasion: Double = -1.0, weather: Double = -1.0): Double {
        var wSum = W_COLOR + W_PATTERN + W_FORMALITY
        var sum = W_COLOR * color + W_PATTERN * pattern + W_FORMALITY * formality
        var minComponent = minOf(color, pattern, formality)
        if (occasion >= 0.0) {
            sum += W_OCCASION * occasion; wSum += W_OCCASION
            minComponent = min(minComponent, occasion)
        }
        if (weather >= 0.0) {
            sum += W_WEATHER * weather; wSum += W_WEATHER
            minComponent = min(minComponent, weather)
        }
        return 0.6 * (sum / wSum) + 0.4 * minComponent
    }

    /** How far above the core formality a piece may sit without looking out of place. */
    fun flexAbove(c: Category): Int = when (c) {
        Category.SHOES, Category.OUTERWEAR, Category.BAG -> 1
        Category.ACCESSORY -> 2
        else -> 0
    }

    private fun occasionWeight(c: Category): Double = when (c) {
        Category.TOP, Category.BOTTOM, Category.ONE_PIECE -> 1.0
        Category.LAYER, Category.SHOES -> 0.8
        Category.OUTERWEAR -> 0.5
        Category.BAG, Category.ACCESSORY -> 0.3
        Category.OTHER -> 0.0
    }

    /** Effective outfit warmth on the 1..5 scale: core average plus what layers add. */
    fun warmth(items: List<Garment>): Double? {
        var coreSum = 0.0
        var coreN = 0
        var extra = 0.0
        var accessory = 0.0
        var any = false
        for (g in items) {
            when (g.category) {
                Category.TOP, Category.BOTTOM, Category.ONE_PIECE -> { coreSum += g.warmth; coreN++; any = true }
                Category.LAYER -> { extra += 0.15 * g.warmth; any = true }
                Category.OUTERWEAR -> { extra += 0.20 * g.warmth; any = true }
                Category.ACCESSORY -> if (g.warmth >= 4) accessory += 0.1
                else -> {}
            }
        }
        if (!any) return null
        val core = if (coreN > 0) coreSum / coreN else 2.0
        return (core + extra + min(0.2, accessory)).coerceIn(1.0, 5.0)
    }

    fun run(
        items: List<Garment>,
        weather: WeatherCtx?,
        occasion: Occasion?,
        affinity: AffinityModel,
        explain: Boolean,
        /** Warmth the generator expects to add later (layer/outerwear add-ons). */
        assumedExtraWarmth: Double = 0.0,
        /** Skip missing-outerwear / waterproof penalties because outerwear will be added later. */
        deferOuterwear: Boolean = false,
        /** Enables "on trend" notes for trends live on this date. */
        date: LocalDate? = null,
    ): EvalResult {
        if (items.isEmpty()) return EMPTY
        val notes: MutableList<Note>? = if (explain) ArrayList() else null
        val warnings: MutableList<Note>? = if (explain) ArrayList() else null

        val counts = IntArray(categories.size)
        for (g in items) counts[g.category.ordinal]++

        // --- Colour ---------------------------------------------------------------------------
        var mask = 0
        for (g in items) for (c in g.colors) mask = mask or Harmony.bit(c)
        val colorScore = colorScore(mask)
        if (explain) Harmony.explain(mask, items, notes!!, warnings!!)

        // --- Pattern --------------------------------------------------------------------------
        var bold = 0
        var firstBold: Garment? = null
        var secondBold: Garment? = null
        var textured = false
        for (g in items) {
            if (g.pattern.bold) {
                bold++
                if (firstBold == null) firstBold = g else if (secondBold == null) secondBold = g
            } else if (g.pattern == Pattern.TEXTURE) textured = true
        }
        val patternScore = patternScore(bold, firstBold?.pattern, secondBold?.pattern)
        if (explain) {
            when {
                bold == 1 -> notes!! += Note("The ${Words.phrase(firstBold!!)} ${Words.verb(Words.noun(firstBold), "are", "is")} the only print, so ${Words.verb(Words.noun(firstBold), "they take", "it takes")} centre stage", 0.6)
                bold == 2 && guide.patternPairing(firstBold!!.pattern, secondBold!!.pattern) != null -> {
                    val v = guide.patternPairing(firstBold.pattern, secondBold.pattern)!!
                    if (v.score > 0) notes!! += Note(v.why, 0.58) else warnings!! += Note(v.why, if (v.score == -2) 0.84 else 0.6)
                }
                bold == 2 && patternScore >= 0.75 -> warnings!! += Note(
                    "${firstBold!!.pattern.label} with ${secondBold!!.pattern.label.lowercase()} can work — keep everything else simple", 0.5,
                )
                bold == 2 -> warnings!! += Note("Two bold patterns are competing — let one print lead", 0.82)
                bold > 2 -> warnings!! += Note("${Words.count(bold)} bold patterns at once is a lot — let one print lead", 0.88)
                textured -> notes!! += Note("Texture adds interest without noise", 0.4)
            }
        }

        // --- Formality coherence --------------------------------------------------------------
        var coreMax = Int.MIN_VALUE
        var coreMin = Int.MAX_VALUE
        for (g in items) if (isCore(g.category)) {
            coreMax = max(coreMax, g.formality); coreMin = min(coreMin, g.formality)
        }
        if (coreMax == Int.MIN_VALUE) {
            for (g in items) if (g.category != Category.OTHER) {
                coreMax = max(coreMax, g.formality); coreMin = min(coreMin, g.formality)
            }
        }
        var lowItem: Garment? = null
        var highItem: Garment? = null
        var lo = Int.MAX_VALUE
        var hi = Int.MIN_VALUE
        var formalitySum = 0
        var formalityN = 0
        for (g in items) {
            if (g.category == Category.OTHER) continue
            val f = adjustedFormality(g.formality, g.category, coreMax)
            if (f < lo) { lo = f; lowItem = g }
            if (f > hi) { hi = f; highItem = g }
            if (isCore(g.category)) { formalitySum += g.formality; formalityN++ }
        }
        val spread = if (lowItem == null) 0 else hi - lo
        val formalityScore = formalityScore(spread)
        if (explain && lowItem != null && highItem != null) {
            when {
                spread <= 1 && formalityN > 0 -> {
                    val level = Math.round(formalitySum.toDouble() / formalityN).toInt().coerceIn(Formality.range)
                    notes!! += Note("Consistent ${Formality.label(level).lowercase()} feel from head to toe", 0.5)
                }
                spread == 2 -> warnings!! += Note(
                    "The ${Words.phrase(lowItem)} ${Words.verb(Words.noun(lowItem), "are", "is")} a touch casual next to the ${Words.phrase(highItem)}", 0.4,
                )
                spread >= 3 -> warnings!! += Note(
                    "The ${Words.phrase(lowItem)} and ${Words.phrase(highItem)} are worlds apart in formality", 0.76,
                )
            }
        }

        // --- Occasion fit ---------------------------------------------------------------------
        var occasionScore = -1.0
        if (occasion != null) {
            var wSum = 0.0
            var fSum = 0.0
            var worst: Garment? = null
            var worstDist = 0
            for (g in items) {
                val w = occasionWeight(g.category)
                if (w == 0.0) continue
                val lowBound = occasion.formality.first
                val highBound = occasion.formality.last + flexAbove(g.category).coerceAtMost(1)
                val dist = when {
                    g.formality < lowBound -> lowBound - g.formality
                    g.formality > highBound -> g.formality - highBound
                    else -> 0
                }
                fSum += w * max(0.0, 1.0 - 0.4 * dist)
                wSum += w
                if (dist * w > worstDist * (worst?.let { occasionWeight(it.category) } ?: 0.0)) {
                    worst = g; worstDist = dist
                }
            }
            var s = if (wSum > 0) fSum / wSum else 1.0
            var sportyIssue: Garment? = null
            val sportyPenalised = HashSet<Long>()
            var nonSportyCore: Garment? = null
            var allSporty = true
            when (occasion) {
                Occasion.ACTIVE -> for (g in items) {
                    if (g.category == Category.TOP || g.category == Category.BOTTOM ||
                        g.category == Category.ONE_PIECE || g.category == Category.SHOES
                    ) {
                        if (!g.sporty) {
                            s -= if (g.category == Category.SHOES) 0.2 else 0.1 // footwear matters most when moving
                            allSporty = false
                            if (nonSportyCore == null || g.category == Category.SHOES) nonSportyCore = g
                        }
                    }
                }
                Occasion.WORK, Occasion.SMART, Occasion.FORMAL, Occasion.DATE -> for (g in items) {
                    if (g.sporty && g.category != Category.OTHER) {
                        s -= 0.3
                        sportyPenalised += g.id
                        if (sportyIssue == null) sportyIssue = g
                    }
                }
                else -> {}
            }
            // Pieces the dress-code guide rules out for this occasion.
            var avoided: Garment? = null
            guide.occasions[occasion]?.let { og ->
                for (g in items) if (g.id !in sportyPenalised && og.avoids(g.subcategory)) {
                    s -= 0.25
                    if (avoided == null) avoided = g
                }
            }
            occasionScore = s.coerceIn(0.0, 1.0)
            if (explain) {
                val occ = Words.occasionPhrase(occasion)
                if (worst != null && worstDist > 0) {
                    val n = Words.noun(worst)
                    val text = if (worst.formality < occasion.formality.first) {
                        "The ${Words.phrase(worst)} ${Words.verb(n, "are", "is")} too casual for $occ"
                    } else {
                        "The ${Words.phrase(worst)} ${Words.verb(n, "are", "is")} dressier than $occ needs"
                    }
                    warnings!! += Note(text, 0.7)
                }
                sportyIssue?.let {
                    warnings!! += Note("The ${Words.phrase(it)} ${Words.verb(Words.noun(it), "read", "reads")} too sporty for $occ", 0.68)
                }
                avoided?.let {
                    warnings!! += Note("The ${Words.phrase(it)} ${Words.verb(Words.noun(it), "aren't", "isn't")} right for $occ", 0.78)
                }
                if (occasion == Occasion.ACTIVE) {
                    if (allSporty) notes!! += Note("Sporty pieces throughout — ready to move", 0.7)
                    else nonSportyCore?.let { warnings!! += Note("Sporty pieces would move better than the ${Words.phrase(it)}", 0.55) }
                }
                if (occasionScore >= 0.95 && sportyIssue == null && avoided == null && (occasion != Occasion.ACTIVE || allSporty)) {
                    notes!! += Note("Just right for $occ", 0.62)
                }
            }
        }

        // --- Weather fit ----------------------------------------------------------------------
        var weatherScore = -1.0
        val outfitWarmth = if (weather != null) warmth(items) else null
        if (weather != null && outfitWarmth != null) {
            val needs = weather.needs
            val t = Words.temp(weather.effective)
            val w = outfitWarmth + assumedExtraWarmth
            val diff = w - needs.targetWarmth
            var s = when {
                diff < -0.35 -> 1.0 - 0.55 * (-diff - 0.35)
                diff > 0.35 -> 1.0 - 0.35 * (diff - 0.35)
                else -> 1.0
            }
            var outer: Garment? = null
            var waterproofItem: Garment? = null
            for (g in items) {
                if (g.category == Category.OUTERWEAR && (outer == null || g.warmth > outer.warmth)) outer = g
                if (g.waterproof && (waterproofItem == null || g.category == Category.OUTERWEAR)) waterproofItem = g
            }
            val missingOuter = needs.needsOuterwear && outer == null && !deferOuterwear
            if (missingOuter) {
                s -= 0.25
                if (explain) warnings!! += Note("Light for $t — add a coat or jacket", 0.95)
            }
            val chilly = HashSet<Long>()
            if (!needs.allowsShortsAndOpenShoes) {
                for (g in items) {
                    if (Shapes.isShortsLike(g) || Shapes.isOpenShoe(g)) {
                        s -= 0.3
                        chilly += g.id
                        if (explain) {
                            val n = Words.noun(g)
                            warnings!! += Note("${Words.cap(n)} ${Words.verb(n, "look", "looks")} chilly for $t", 0.9)
                        }
                    }
                }
            }
            // Pieces the weather guide rules out today (suede in heavy rain, heavy knits in a heatwave...).
            for (g in items) {
                if (g.id in chilly) continue
                val rule = weather.guideRules.firstOrNull { it.avoids(g.subcategory) } ?: continue
                s -= 0.15
                if (explain) warnings!! += Note("${rule.title}: swap the ${Words.phrase(g)}", 0.8)
            }
            if (needs.wantsWaterproof && !deferOuterwear) {
                if (waterproofItem == null) {
                    s -= 0.05
                    if (explain) warnings!! += Note("Rain is likely — something waterproof would help", 0.45)
                } else if (explain) {
                    val n = Words.noun(waterproofItem)
                    notes!! += Note("The ${Words.phrase(waterproofItem)} ${Words.verb(n, "keep", "keeps")} the rain off", 0.8)
                }
            }
            weatherScore = s.coerceIn(0.0, 1.0)
            if (explain) {
                when {
                    diff >= -0.5 && diff <= 0.6 && !missingOuter -> {
                        val text = when {
                            outer != null && needs.needsOuterwear -> "Warm enough for $t with the ${Words.phrase(outer)}"
                            weather.effective >= 25.0 -> "Light and breathable for $t"
                            else -> "Right weight for $t"
                        }
                        notes!! += Note(text, 0.88)
                    }
                    diff < -0.6 && !missingOuter -> warnings!! += Note("Light for $t — add a warmer layer", 0.8)
                    diff > 1.0 -> warnings!! += Note("Could feel warm for $t — lighter pieces would breathe better", 0.5)
                }
            }
        }

        // --- Structure ------------------------------------------------------------------------
        var factor = 1.0
        val hasTop = counts[Category.TOP.ordinal] > 0
        val hasBottom = counts[Category.BOTTOM.ordinal] > 0
        val hasOnePiece = counts[Category.ONE_PIECE.ordinal] > 0
        if (!hasOnePiece && !(hasTop && hasBottom)) {
            factor *= 0.6
            if (explain) {
                val text = when {
                    hasTop -> "Add bottoms to complete the look"
                    hasBottom -> "Add a top to complete the look"
                    else -> "Needs a top and bottoms, or a dress"
                }
                warnings!! += Note(text, 1.0)
            }
        }
        if (counts[Category.SHOES.ordinal] == 0) {
            factor *= 0.85
            if (explain) warnings!! += Note("No shoes yet — add a pair to finish the look", 0.97)
        }
        for (c in DUPLICATE_SENSITIVE) {
            val n = counts[c.ordinal]
            if (n > 1) {
                factor *= 0.85
                if (explain) warnings!! += Note("${Words.count(n)} picks from ${c.label} — choose one", 0.92)
            }
        }
        if (counts[Category.LAYER.ordinal] > 2) {
            factor *= 0.9
            if (explain) warnings!! += Note("${Words.count(counts[Category.LAYER.ordinal])} layers is a lot — drop one", 0.6)
        }

        // --- Affinity -------------------------------------------------------------------------
        var affinityAdj = 0.0
        if (affinity.pairScores.isNotEmpty() || affinity.itemScores.isNotEmpty()) {
            var pairSum = 0.0
            var itemSum = 0.0
            for (i in items.indices) {
                itemSum += affinity.item(items[i].id)
                if (affinity.pairScores.isNotEmpty()) {
                    for (j in i + 1 until items.size) {
                        if (items[i].id != items[j].id) pairSum += affinity.pair(items[i].id, items[j].id)
                    }
                }
            }
            affinityAdj = 0.08 * tanh((pairSum + 0.5 * itemSum) / 1.5)
            if (explain) {
                if (pairSum >= 0.3) notes!! += Note("You've enjoyed these pieces together before", 0.74)
                else if (pairSum <= -0.4) warnings!! += Note("You've passed on this pairing before", 0.3)
            }
        }

        // --- Garment pairings (style guide) -----------------------------------------------------
        var lift = 0.0
        var cost = 0.0
        val k = guide
        val typeIdx = IntArray(items.size) { k.typeIndex(items[it].subcategory) }
        for (i in items.indices) for (j in i + 1 until items.size) {
            if (items[i].id == items[j].id) continue
            val v = k.typeScore(typeIdx[i], typeIdx[j])
            lift += typeLift(v)
            cost += typeCost(v)
        }
        val typeAdj = typeAdjustment(lift, cost)
        if (explain) {
            val verdicts = ArrayList<TypePairing>()
            for (i in items.indices) for (j in i + 1 until items.size) {
                if (items[i].id != items[j].id) guide.typePairing(items[i].subcategory, items[j].subcategory)?.let(verdicts::add)
            }
            verdicts.filter { it.score > 0 }.maxByOrNull { it.score }?.let { notes!! += Note(it.why, if (it.score == 2) 0.86 else 0.72) }
            for (v in verdicts.filter { it.score < 0 }.sortedBy { it.score }.take(2)) {
                warnings!! += Note(v.why, if (v.score == -2) 0.85 else 0.6)
            }
            Formulas.match(items, occasion)
                ?.takeIf { occasion == null || it.occasions.isEmpty() || occasion in it.occasions }
                ?.let { notes!! += Note("Proven formula: ${it.name}", 0.9) }
            if (date != null) {
                guide.liveTrends(date).firstOrNull { it.matchesOutfit(items) }?.let {
                    notes!! += Note("On trend: ${it.name.replaceFirstChar(Char::lowercase)}", 0.66)
                }
            }
        }

        // --- Combine --------------------------------------------------------------------------
        val base = combine(colorScore, patternScore, formalityScore, occasionScore, weatherScore)
        val score = softCap(base * factor + affinityAdj + typeAdj).coerceIn(0.0, 1.0)

        return if (explain) {
            EvalResult(score, dedupe(notes!!), dedupe(warnings!!), spread)
        } else {
            EvalResult(score, emptyList(), emptyList(), spread)
        }
    }

    private val DUPLICATE_SENSITIVE = listOf(
        Category.TOP, Category.BOTTOM, Category.ONE_PIECE, Category.SHOES, Category.OUTERWEAR, Category.BAG,
    )

    private fun dedupe(list: List<Note>): List<Note> =
        list.sortedByDescending { it.priority }.distinctBy { it.text }

    /** Stripes and dots are the "friendly" prints that can partner a different print. */
    private fun mixable(a: Pattern, b: Pattern): Boolean =
        a != b && (a == Pattern.STRIPE || a == Pattern.DOT || b == Pattern.STRIPE || b == Pattern.DOT)
}
