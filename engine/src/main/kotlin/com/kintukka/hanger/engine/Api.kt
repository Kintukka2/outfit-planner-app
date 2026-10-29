package com.kintukka.hanger.engine

import java.time.LocalDate

/*
 * Public engine API. The Android app depends ONLY on the signatures in this
 * file and Model.kt. Implementations live in this module.
 */

/** Colour naming, extraction and harmony. */
object ColorTools {
    /** Snap an sRGB colour (0xRRGGBB, alpha ignored) to the nearest [FashionColor] (perceptual, CIE Lab). */
    fun nameFor(rgb: Int): FashionColor = ColorScience.nameFor(rgb)

    /**
     * Dominant colours of a garment cut-out. [argbPixels] are ARGB ints (e.g. from Bitmap.getPixels);
     * pixels with alpha < 128 are ignored. Returns up to [maxColors] distinct fashion colours with their
     * pixel share (0..1), largest first. Shares under 8% are dropped. Must be fast on ~65k pixels.
     */
    fun dominantColors(argbPixels: IntArray, maxColors: Int = 3): List<Pair<FashionColor, Double>> =
        ColorScience.dominantColors(argbPixels, maxColors)

    /** 0..1 harmony of a set of colours (all garments' colours in an outfit). 1 = effortless. */
    fun harmony(colors: List<FashionColor>): Double = Harmony.score(Harmony.maskOf(colors))
}

object Seasons {
    /** Meteorological season for a date (Dec–Feb winter in the north, summer in the south). */
    fun forDate(date: LocalDate, hemisphere: Hemisphere = Hemisphere.NORTH): Season = SeasonMath.forDate(date, hemisphere)
}

object WeatherAdvisor {
    fun needs(weather: WeatherContext): WeatherNeeds = WeatherMath.needs(weather)
}

data class SubcategoryPreset(
    val name: String,
    val category: Category,
    val formality: Int,
    val warmth: Int,
    val wearsBeforeWash: Int,
    val sporty: Boolean = false,
    val waterproof: Boolean = false,
    val seasons: Set<Season> = emptySet(),
)

object Subcategories {
    /** Curated presets for pickers, e.g. TOP -> T-shirt, Shirt, Blouse, Tank, Polo, Sweatshirt... */
    fun forCategory(category: Category): List<SubcategoryPreset> = Catalog.forCategory(category)
    fun all(): List<SubcategoryPreset> = Catalog.presets
    fun find(name: String): SubcategoryPreset? = Catalog.find(name)
}

object CategoryGuesser {
    /**
     * Map on-device image-labeler labels (e.g. "Jeans", "Shoe", "Jacket", "Dress", "Handbag", "Sunglasses")
     * with confidences to a likely preset. Returns null when nothing clothing-like is recognised.
     */
    fun guess(labels: List<Pair<String, Float>>, silhouette: Silhouette = Silhouette.UNKNOWN): SubcategoryPreset? =
        Guesser.guess(labels, silhouette)
}

object SilhouetteReader {
    /** Read a cut-out's outline from its opacity mask (row-major, `width * height`). */
    fun read(opaque: BooleanArray, width: Int, height: Int): Silhouette = Outline.read(opaque, width, height)
}

object LaundryRules {
    fun defaultWearsBeforeWash(category: Category, subcategory: String = ""): Int =
        Laundry.defaultWearsBeforeWash(category, subcategory)

    /** Apply one wear: increments wear counters and flips to DIRTY when the wash threshold is reached. */
    fun afterWear(garment: Garment, date: LocalDate): Garment = Laundry.afterWear(garment, date)

    /** Laundry done: DIRTY -> CLEAN, wearsSinceWash -> 0. */
    fun washed(garment: Garment): Garment = Laundry.washed(garment)
}

data class OutfitEvaluation(
    /** 0..1 overall quality. */
    val score: Double,
    /** Positive notes, e.g. "Navy and camel are a classic pairing". */
    val notes: List<String>,
    /** Problems, e.g. "Two bold patterns compete", "Light for 6°C — add a coat". */
    val warnings: List<String>,
)

object OutfitEvaluator {
    /** Judge any user-built combination (used live by the outfit builder). */
    fun evaluate(
        items: List<Garment>,
        weather: WeatherContext? = null,
        occasion: Occasion? = null,
        affinity: AffinityModel = AffinityModel(),
        /** Enables "on trend" notes for trends live on this date. */
        date: LocalDate? = null,
    ): OutfitEvaluation {
        val result = Evaluator.run(items, weather?.let { WeatherCtx.of(it) }, occasion, affinity, explain = true, date = date)
        return OutfitEvaluation(result.score, result.notes.map { it.text }, result.warnings.map { it.text })
    }
}

object OutfitGenerator {
    /**
     * Suggest up to [StylingRequest.count] diverse, weather- and occasion-appropriate outfits from CLEAN
     * garments. Core templates: TOP+BOTTOM+SHOES and ONE_PIECE+SHOES, plus LAYER/OUTERWEAR when the
     * weather calls for it and optionally one BAG/ACCESSORY. Deterministic for a given seed.
     */
    fun suggest(
        closet: List<Garment>,
        request: StylingRequest,
        affinity: AffinityModel = AffinityModel(),
    ): List<SuggestedOutfit> = Generator.suggest(closet, request, affinity)
}

object Shuffler {
    /**
     * "Dress me" slot machine: one random garment per row, keeping locked picks.
     * Rows with no eligible garment are omitted from the result.
     */
    fun shuffle(closet: List<Garment>, request: ShuffleRequest): Map<Category, Long> = ShuffleEngine.shuffle(closet, request)
}

object FeedbackLearner {
    fun apply(model: AffinityModel, event: FeedbackEvent): AffinityModel = Feedback.apply(model, event)
}

object PackingPlanner {
    fun plan(closet: List<Garment>, trip: TripSpec): PackingPlan = Packing.plan(closet, trip)
}

object StatsCalculator {
    fun compute(closet: List<Garment>, wearLog: List<WearRecord>, today: LocalDate): StatsReport =
        Stats.compute(closet, wearLog, today)

    /** Price / wears, or null when there is no price or no wears. */
    fun costPerWear(garment: Garment): Double? = Stats.costPerWear(garment)
}

object CapsuleAnalyzer {
    /** Number of distinct valid core outfits (top+bottom+shoes, one-piece+shoes) the garments can make. */
    fun outfitCount(garments: List<Garment>): Int = Capsule.outfitCount(garments)

    /** How many extra valid outfits [candidate] would unlock if added to [closet]. */
    fun wishlistImpact(closet: List<Garment>, candidate: Garment): Int = Capsule.wishlistImpact(closet, candidate)
}

/** A formula the closet can make right now, with the pieces that make it. */
data class FormulaMatch(val formula: Formula, val itemIds: List<Long>)

/** A formula one piece away: [missing] is the slot nothing in the closet fills. */
data class FormulaGap(val formula: Formula, val missing: FormulaSlot, val itemIds: List<Long>)

data class FormulaCoverage(val ready: List<FormulaMatch>, val oneAway: List<FormulaGap>)

/** A closet check from the guide that fired, with its message filled in. */
data class ClosetInsight(val check: ClosetCheck, val value: Double, val message: String)

/** Everything the built-in style guide can say about a closet or an outfit. */
object StyleGuide {
    val knowledge: StyleKnowledge get() = StyleKnowledge.bundled

    /** The proven formula an outfit follows, if any. */
    fun formulaFor(items: List<Garment>, occasion: Occasion? = null): Formula? = Formulas.match(items, occasion)

    /** Formulas the closet can make, and those one piece away. */
    fun formulaCoverage(closet: List<Garment>): FormulaCoverage = Formulas.coverage(closet)

    fun closetMetrics(closet: List<Garment>, wearLog: List<WearRecord>, today: LocalDate, wishlist: Int = 0): Map<String, Double> =
        ClosetChecks.metrics(closet, wearLog, today, wishlist)

    /** The guide's closet checks that fire for this closet. */
    fun closetInsights(
        closet: List<Garment>,
        wearLog: List<WearRecord>,
        today: LocalDate,
        wishlist: Int = 0,
        currency: String = "£",
    ): List<ClosetInsight> = ClosetChecks.run(ClosetChecks.metrics(closet, wearLog, today, wishlist), currency)

    /** Live trends and the closet pieces that already speak to each. */
    fun trendPieces(closet: List<Garment>, today: LocalDate): List<Pair<Trend, List<Garment>>> =
        knowledge.liveTrends(today).map { t -> t to closet.filter(t::matchesPiece) }

    /** Garment types the guide advises against for [occasion]. */
    fun avoidsFor(occasion: Occasion): List<String> = knowledge.occasions[occasion]?.avoid.orEmpty()
}
