package com.kintukka.hanger.engine

import java.time.LocalDate

/*
 * Public engine API. The Android app depends ONLY on the signatures in this
 * file and Model.kt. Implementations live in this module.
 */

/** Colour naming, extraction and harmony. */
object ColorTools {
    /** Snap an sRGB colour (0xRRGGBB, alpha ignored) to the nearest [FashionColor] (perceptual, CIE Lab). */
    fun nameFor(rgb: Int): FashionColor = TODO()

    /**
     * Dominant colours of a garment cut-out. [argbPixels] are ARGB ints (e.g. from Bitmap.getPixels);
     * pixels with alpha < 128 are ignored. Returns up to [maxColors] distinct fashion colours with their
     * pixel share (0..1), largest first. Shares under 8% are dropped. Must be fast on ~65k pixels.
     */
    fun dominantColors(argbPixels: IntArray, maxColors: Int = 3): List<Pair<FashionColor, Double>> = TODO()

    /** 0..1 harmony of a set of colours (all garments' colours in an outfit). 1 = effortless. */
    fun harmony(colors: List<FashionColor>): Double = TODO()
}

object Seasons {
    /** Meteorological season for a date (Dec–Feb winter in the north, summer in the south). */
    fun forDate(date: LocalDate, hemisphere: Hemisphere = Hemisphere.NORTH): Season = TODO()
}

object WeatherAdvisor {
    fun needs(weather: WeatherContext): WeatherNeeds = TODO()
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
    fun forCategory(category: Category): List<SubcategoryPreset> = TODO()
    fun all(): List<SubcategoryPreset> = TODO()
    fun find(name: String): SubcategoryPreset? = TODO()
}

object CategoryGuesser {
    /**
     * Map on-device image-labeler labels (e.g. "Jeans", "Shoe", "Jacket", "Dress", "Handbag", "Sunglasses")
     * with confidences to a likely preset. Returns null when nothing clothing-like is recognised.
     */
    fun guess(labels: List<Pair<String, Float>>): SubcategoryPreset? = TODO()
}

object LaundryRules {
    fun defaultWearsBeforeWash(category: Category, subcategory: String = ""): Int = TODO()

    /** Apply one wear: increments wear counters and flips to DIRTY when the wash threshold is reached. */
    fun afterWear(garment: Garment, date: LocalDate): Garment = TODO()

    /** Laundry done: DIRTY -> CLEAN, wearsSinceWash -> 0. */
    fun washed(garment: Garment): Garment = TODO()
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
    ): OutfitEvaluation = TODO()
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
    ): List<SuggestedOutfit> = TODO()
}

object Shuffler {
    /**
     * "Dress me" slot machine: one random garment per row, keeping locked picks.
     * Rows with no eligible garment are omitted from the result.
     */
    fun shuffle(closet: List<Garment>, request: ShuffleRequest): Map<Category, Long> = TODO()
}

object FeedbackLearner {
    fun apply(model: AffinityModel, event: FeedbackEvent): AffinityModel = TODO()
}

object PackingPlanner {
    fun plan(closet: List<Garment>, trip: TripSpec): PackingPlan = TODO()
}

object StatsCalculator {
    fun compute(closet: List<Garment>, wearLog: List<WearRecord>, today: LocalDate): StatsReport = TODO()

    /** Price / wears, or null when there is no price or no wears. */
    fun costPerWear(garment: Garment): Double? = TODO()
}

object CapsuleAnalyzer {
    /** Number of distinct valid core outfits (top+bottom+shoes, one-piece+shoes) the garments can make. */
    fun outfitCount(garments: List<Garment>): Int = TODO()

    /** How many extra valid outfits [candidate] would unlock if added to [closet]. */
    fun wishlistImpact(closet: List<Garment>, candidate: Garment): Int = TODO()
}
