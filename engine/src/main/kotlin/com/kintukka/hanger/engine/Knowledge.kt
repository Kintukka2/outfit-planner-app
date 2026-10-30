package com.kintukka.hanger.engine

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDate

/**
 * The built-in style guide: researched colour pairings, outfit formulas, garment pairings, dress codes,
 * weather and fabric advice, trends and closet checks. Ships inside the app (`/hanger/style-knowledge.json`,
 * built from `research_notes/Style knowledge 2026` by `tools/build_knowledge.py`); nothing is fetched online.
 */
@Serializable
data class StyleKnowledge(
    val version: Int,
    val updated: String,
    val sources: List<Source> = emptyList(),
    val colors: ColorKnowledge = ColorKnowledge(),
    val formulas: List<Formula> = emptyList(),
    val pairings: List<TypePairing> = emptyList(),
    val patterns: List<PatternPairing> = emptyList(),
    val styleRules: List<GuideRule> = emptyList(),
    val occasions: Map<Occasion, OccasionGuide> = emptyMap(),
    val dressCodes: List<DressCode> = emptyList(),
    val weather: List<WeatherRule> = emptyList(),
    val fabrics: List<Fabric> = emptyList(),
    val trends: TrendKnowledge = TrendKnowledge(),
    val wardrobe: WardrobeKnowledge = WardrobeKnowledge(),
) {
    private val sourcesById by lazy { sources.associateBy { it.id } }

    fun source(id: String): Source? = sourcesById[id]

    /** Colour-pair score (-2..2), 0 when the guide has no view on the pair. */
    fun colorScore(a: FashionColor, b: FashionColor): Int = colorMatrix[a.ordinal * COLORS + b.ordinal].toInt()

    /** Row-major colour-pair scores by ordinal, for hot loops that read it once per call. */
    internal val colorTable: ByteArray get() = colorMatrix

    fun colorPair(a: FashionColor, b: FashionColor): ColorPair? = colorPairs[key(a.ordinal, b.ordinal)]

    fun profile(color: FashionColor): ColorProfile? = colors.profiles[color]

    /** Type-pair score (-2..2) for two garment types (e.g. "Loafers", "Chinos"), 0 when unknown. */
    fun typeScore(a: String, b: String): Int {
        val ia = typeIndex(a)
        val ib = typeIndex(b)
        return if (ia < 0 || ib < 0) 0 else typeMatrix[ia * typeNames.size + ib].toInt()
    }

    fun typePairing(a: String, b: String): TypePairing? {
        val ia = typeIndex(a)
        val ib = typeIndex(b)
        return if (ia < 0 || ib < 0) null else typePairs[key(ia, ib)]
    }

    /** Dense index for a garment type, or -1 when the guide doesn't know it. */
    fun typeIndex(type: String): Int = if (type.isEmpty()) -1 else typeCache.getOrPut(type) { typeIds[type.lowercase()] ?: -1 }

    /** Exact-string cache so hot scoring loops don't lowercase on every lookup. */
    @kotlinx.serialization.Transient
    private val typeCache = java.util.concurrent.ConcurrentHashMap<String, Int>()

    /** Type score by dense indexes (fast path for the capsule counter). */
    fun typeScore(ia: Int, ib: Int): Int = if (ia < 0 || ib < 0) 0 else typeMatrix[ia * typeNames.size + ib].toInt()

    fun patternScore(a: Pattern, b: Pattern): Int? = patternTable[a.ordinal * PATTERNS + b.ordinal]?.score

    fun patternPairing(a: Pattern, b: Pattern): PatternPairing? = patternTable[a.ordinal * PATTERNS + b.ordinal]

    fun fabricFor(material: String): Fabric? {
        val text = material.lowercase()
        if (text.isBlank()) return null
        // Longest keyword wins, so "merino wool" is merino rather than wool.
        return fabrics.flatMap { f -> f.match.map { it to f } }
            .filter { (kw, _) -> text.contains(kw) }
            .maxByOrNull { it.first.length }?.second
    }

    fun liveTrends(today: LocalDate): List<Trend> = trends.trends.filter { it.isLive(today) }

    fun upcomingTrends(today: LocalDate): List<Trend> = trends.trends.filter { it.startDate?.isAfter(today) == true }

    /** Rules for a day, judged with the same temperature and rain test as the rest of the engine. */
    fun weatherRulesFor(w: WeatherContext): List<WeatherRule> {
        val feels = WeatherMath.effective(w)
        val snow = w.condition == WeatherCondition.SNOW
        val rain = WeatherMath.wantsWaterproof(w) && !snow
        return weather.filter { it.condition.matches(feels, rain, snow, w.windKph, w.uvIndex, w.tempMaxC) }
    }

    fun tipOfTheDay(date: LocalDate): Tip? =
        wardrobe.tips.takeIf { it.isNotEmpty() }?.let { it[Math.floorMod(date.toEpochDay(), it.size.toLong()).toInt()] }

    // --- Lookup tables (built once) -------------------------------------------------------------

    private val colorMatrix: ByteArray by lazy {
        val m = ByteArray(COLORS * COLORS)
        for (p in colors.pairs) {
            m[p.a.ordinal * COLORS + p.b.ordinal] = p.score.toByte()
            m[p.b.ordinal * COLORS + p.a.ordinal] = p.score.toByte()
        }
        m
    }

    private val colorPairs: Map<Long, ColorPair> by lazy { colors.pairs.associateBy { key(it.a.ordinal, it.b.ordinal) } }

    private val typeNames: List<String> by lazy {
        (pairings.flatMap { listOf(it.a, it.b) } + Catalog.presets.map { it.name }).map { it.lowercase() }.distinct()
    }

    private val typeIds: Map<String, Int> by lazy { typeNames.withIndex().associate { it.value to it.index } }

    private val typeMatrix: ByteArray by lazy {
        val n = typeNames.size
        val m = ByteArray(n * n)
        for (p in pairings) {
            val ia = typeIds.getValue(p.a.lowercase())
            val ib = typeIds.getValue(p.b.lowercase())
            m[ia * n + ib] = p.score.toByte()
            m[ib * n + ia] = p.score.toByte()
        }
        m
    }

    private val typePairs: Map<Long, TypePairing> by lazy {
        pairings.associateBy { key(typeIds.getValue(it.a.lowercase()), typeIds.getValue(it.b.lowercase())) }
    }

    private val patternTable: Array<PatternPairing?> by lazy {
        val t = arrayOfNulls<PatternPairing>(PATTERNS * PATTERNS)
        for (p in patterns) {
            t[p.a.ordinal * PATTERNS + p.b.ordinal] = p
            t[p.b.ordinal * PATTERNS + p.a.ordinal] = p
        }
        t
    }

    companion object {
        private val COLORS = FashionColor.entries.size
        private val PATTERNS = Pattern.entries.size
        private fun key(a: Int, b: Int): Long = if (a <= b) a.toLong() shl 32 or b.toLong() else b.toLong() shl 32 or a.toLong()

        private val json = Json { ignoreUnknownKeys = true }

        fun parse(text: String): StyleKnowledge = json.decodeFromString(serializer(), text)

        private val loaded: StyleKnowledge by lazy {
            val stream = StyleKnowledge::class.java.getResourceAsStream("/hanger/style-knowledge.json")
                ?: error("Bundled style guide missing")
            parse(stream.bufferedReader().use { it.readText() })
        }

        /** Test hook: swap in a small fixed guide so engine tests don't depend on the research wording. */
        @Volatile
        var override: StyleKnowledge? = null

        /** The guide bundled with the app. */
        val bundled: StyleKnowledge get() = override ?: loaded
    }

    /** Palette colour masks (3+ colours), for the colour engine. */
    internal val paletteMasks: List<Pair<Int, Palette>> by lazy {
        colors.palettes.filter { it.colors.size >= 3 }.map { Harmony.maskOf(it.colors) to it }
    }

    /** Formulas with their required slots ordered most-constrained first, most specific formulas first. */
    internal val preparedFormulas: List<PreparedFormula> by lazy {
        formulas.map { f ->
            val slots = f.required.sortedByDescending { it.constraint }
            var mask = 0
            for (s in slots) mask = mask or (1 shl s.category.ordinal)
            PreparedFormula(f, slots, mask, slots.size * 4 + slots.sumOf { it.constraint })
        }.sortedByDescending { it.specificity }
    }
}

internal class PreparedFormula(val formula: Formula, val slots: List<FormulaSlot>, val categoryMask: Int, val specificity: Int)

@Serializable
data class Source(val id: String, val title: String, val publisher: String = "", val url: String = "", val date: String = "")

@Serializable
data class ColorKnowledge(
    val pairs: List<ColorPair> = emptyList(),
    val palettes: List<Palette> = emptyList(),
    val profiles: Map<FashionColor, ColorProfile> = emptyMap(),
    val rules: List<GuideRule> = emptyList(),
)

@Serializable
data class ColorPair(val a: FashionColor, val b: FashionColor, val score: Int, val why: String, val src: List<String> = emptyList())

@Serializable
data class Palette(val name: String, val colors: List<FashionColor>, val why: String, val src: List<String> = emptyList())

@Serializable
data class ColorProfile(
    val role: String,
    val summary: String,
    val bestWith: List<FashionColor> = emptyList(),
    val avoidWith: List<FashionColor> = emptyList(),
    val seasons: List<Season> = emptyList(),
)

@Serializable
data class GuideRule(val id: String, val topic: String = "", val title: String, val text: String, val src: List<String> = emptyList())

@Serializable
data class FormulaSlot(
    val category: Category,
    val types: List<String> = emptyList(),
    val colors: List<FashionColor> = emptyList(),
    val optional: Boolean = false,
) {
    /** How tightly the slot is specified (types weigh more than colours). */
    val constraint: Int get() = (if (types.isEmpty()) 0 else 2) + (if (colors.isEmpty()) 0 else 1)

    fun accepts(g: Garment): Boolean =
        g.category == category &&
            (types.isEmpty() || types.any { it.equals(g.subcategory, ignoreCase = true) }) &&
            (colors.isEmpty() || g.colors.firstOrNull() in colors)
}

@Serializable
data class Formula(
    val id: String,
    val name: String,
    val slots: List<FormulaSlot>,
    val occasions: List<Occasion> = emptyList(),
    val seasons: List<Season> = emptyList(),
    val formality: List<Int> = emptyList(),
    val why: String,
    val src: List<String> = emptyList(),
) {
    val required: List<FormulaSlot> get() = slots.filter { !it.optional }
}

@Serializable
data class TypePairing(val a: String, val b: String, val score: Int, val why: String, val src: List<String> = emptyList())

@Serializable
data class PatternPairing(val a: Pattern, val b: Pattern, val score: Int, val why: String, val src: List<String> = emptyList())

@Serializable
data class OccasionGuide(
    val summary: String,
    val formality: List<Int> = emptyList(),
    val good: List<String> = emptyList(),
    val avoid: List<String> = emptyList(),
    val tips: List<String> = emptyList(),
    val src: List<String> = emptyList(),
) {
    fun avoids(type: String): Boolean = type.isNotEmpty() && avoid.any { it.equals(type, ignoreCase = true) }
}

@Serializable
data class DressCode(
    val name: String,
    val occasion: Occasion,
    val summary: String,
    val examples: List<String> = emptyList(),
    val avoid: List<String> = emptyList(),
    val src: List<String> = emptyList(),
)

@Serializable
data class WeatherWhen(
    val minFeelsC: Double? = null,
    val maxFeelsC: Double? = null,
    val rain: Boolean = false,
    val snow: Boolean = false,
    val windKphMin: Double? = null,
    val uvMin: Double? = null,
    /** Hot-and-humid style rules key off the day's high rather than the wearing-hours feel. */
    val minHighC: Double? = null,
) {
    fun matches(feels: Double, rain: Boolean, snow: Boolean, windKph: Double, uv: Double, highC: Double): Boolean =
        (minFeelsC == null || feels >= minFeelsC) &&
            (maxFeelsC == null || feels < maxFeelsC) &&
            (!this.rain || rain) &&
            (!this.snow || snow) &&
            (windKphMin == null || windKph >= windKphMin) &&
            (uvMin == null || uv >= uvMin) &&
            (minHighC == null || highC >= minHighC)
}

@Serializable
data class WeatherRule(
    val id: String,
    @SerialName("when") val condition: WeatherWhen,
    val title: String,
    val advice: String,
    val types: List<String> = emptyList(),
    val avoidTypes: List<String> = emptyList(),
    val src: List<String> = emptyList(),
) {
    fun avoids(type: String): Boolean = type.isNotEmpty() && avoidTypes.any { it.equals(type, ignoreCase = true) }
}

@Serializable
data class Fabric(
    val material: String,
    val match: List<String>,
    val seasons: List<Season> = emptyList(),
    val warmth: Int = 3,
    val tip: String,
    val care: String = "",
    val src: List<String> = emptyList(),
)

@Serializable
data class TrendKnowledge(
    val updated: String = "",
    val trends: List<Trend> = emptyList(),
    val fading: List<TrendNote> = emptyList(),
    val classics: List<TrendNote> = emptyList(),
    val colourOfTheYear: List<ColourOfTheYear> = emptyList(),
)

@Serializable
data class Trend(
    val id: String,
    val name: String,
    val kind: String,
    val season: String,
    val from: String,
    val to: String,
    val status: String = "peak",
    val summary: String,
    val howToWear: String = "",
    val colors: List<FashionColor> = emptyList(),
    val types: List<String> = emptyList(),
    val patterns: List<Pattern> = emptyList(),
    val match: String = "all",
    val src: List<String> = emptyList(),
) {
    @kotlinx.serialization.Transient
    val startDate: LocalDate? = runCatching { LocalDate.parse(from) }.getOrNull()

    @kotlinx.serialization.Transient
    val endDate: LocalDate? = runCatching { LocalDate.parse(to) }.getOrNull()

    /** Live between its dates, inclusive; a trend with a date that doesn't parse is never live. */
    fun isLive(today: LocalDate): Boolean {
        val start = startDate ?: return false
        val end = endDate ?: return false
        return !today.isBefore(start) && !today.isAfter(end)
    }

    /** Whether the trend can be recognised in clothes at all. */
    val recognisable: Boolean get() = colors.isNotEmpty() || types.isNotEmpty() || patterns.isNotEmpty()

    /** Whether [items] (an outfit) carries the trend. */
    fun matchesOutfit(items: List<Garment>): Boolean {
        if (!recognisable) return false
        val colorHits = colors.map { c -> items.any { c in it.colors } }
        val typeHits = types.map { t -> items.any { it.subcategory.equals(t, ignoreCase = true) } }
        val patternHits = patterns.map { p -> items.any { it.pattern == p } }
        val hits = colorHits + typeHits + patternHits
        return if (match == "any") hits.any { it } else hits.all { it }
    }

    /**
     * Whether a single piece expresses the trend (for "pieces you own" lists). For "all" trends a piece must fit
     * every dimension the trend names (dark florals = a floral piece in black; brown jeans = jeans in brown), while
     * a pairing's colours or types are alternatives; "any" trends take a hit on any dimension.
     */
    fun matchesPiece(g: Garment): Boolean {
        if (!recognisable) return false
        val colorOk = g.colors.firstOrNull() in colors
        val typeOk = types.any { it.equals(g.subcategory, ignoreCase = true) }
        val patternOk = g.pattern in patterns
        return if (match == "any") colorOk || typeOk || patternOk
        else (colors.isEmpty() || colorOk) && (types.isEmpty() || typeOk) && (patterns.isEmpty() || patternOk)
    }
}

@Serializable
data class TrendNote(
    val name: String,
    val summary: String,
    val colors: List<FashionColor> = emptyList(),
    val types: List<String> = emptyList(),
    val src: List<String> = emptyList(),
)

@Serializable
data class ColourOfTheYear(
    val name: String,
    val by: String,
    val year: Int,
    val nearest: FashionColor? = null,
    val summary: String,
    val src: List<String> = emptyList(),
)

@Serializable
data class WardrobeKnowledge(
    val tips: List<Tip> = emptyList(),
    val checks: List<ClosetCheck> = emptyList(),
    val ratios: Ratios? = null,
    val essentials: List<Essential> = emptyList(),
)

@Serializable
data class Tip(val id: String, val topic: String, val text: String, val src: List<String> = emptyList())

@Serializable
data class ClosetCheck(
    val id: String,
    val title: String,
    val metric: String,
    val op: String,
    val value: Double,
    val minItems: Int = 0,
    val message: String,
    val action: String = "",
    /** "positive" (well done) or "corrective" (worth fixing). */
    val tone: String = "corrective",
    /** 1 = most useful to show (weather and occasion gaps) ... 6 = least. */
    val priority: Int = 3,
    val src: List<String> = emptyList(),
) {
    val positive: Boolean get() = tone == "positive"
}

@Serializable
data class Ratios(val summary: String, val perBottom: Map<Category, Double> = emptyMap(), val src: List<String> = emptyList())

@Serializable
data class Essential(
    val name: String,
    val category: Category,
    val types: List<String> = emptyList(),
    val why: String,
    val src: List<String> = emptyList(),
) {
    fun ownedBy(closet: List<Garment>): Boolean =
        closet.any { g -> g.category == category && (types.isEmpty() || types.any { it.equals(g.subcategory, ignoreCase = true) }) }
}
