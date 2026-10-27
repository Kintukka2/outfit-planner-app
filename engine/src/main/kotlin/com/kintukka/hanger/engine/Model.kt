package com.kintukka.hanger.engine

import java.time.LocalDate

/**
 * Core, Android-free domain model used by the styling engine.
 *
 * The app maps its Room entities onto these types, asks the engine for
 * suggestions, shuffles, packing plans and stats, and maps results back.
 */

/** Where a garment sits on the body. Drives outfit templates and slot ordering. */
enum class Category(val label: String, val order: Int) {
    TOP("Tops", 1),
    BOTTOM("Bottoms", 2),
    ONE_PIECE("Dresses & jumpsuits", 0),
    LAYER("Layers", 3),          // knits, cardigans, hoodies, blazers worn over a top
    OUTERWEAR("Outerwear", 4),   // coats, jackets, raincoats
    SHOES("Shoes", 5),
    BAG("Bags", 6),
    ACCESSORY("Accessories", 7), // jewellery, belts, hats, scarves, sunglasses, watches
    OTHER("Other", 8);
}

/** Coarse outline of a cut-out garment, used to sanity-check label-based category guesses. */
enum class Silhouette {
    /** Sleeves or shoulders clearly wider than the body, one hem at the bottom. */
    TOP,
    /** One waistband splitting into two legs. */
    LEGS,
    UNKNOWN,
}

enum class Season(val label: String) { SPRING("Spring"), SUMMER("Summer"), AUTUMN("Autumn"), WINTER("Winter") }

enum class Hemisphere { NORTH, SOUTH }

enum class Pattern(val label: String, val bold: Boolean) {
    SOLID("Solid", false),
    TEXTURE("Textured", false),
    STRIPE("Stripes", true),
    CHECK("Checks", true),
    DOT("Dots", true),
    FLORAL("Floral", true),
    GRAPHIC("Graphic", true),
    ANIMAL("Animal", true),
    OTHER("Other print", true),
}

/** Laundry / availability state. Only [CLEAN] garments are suggested. */
enum class GarmentStatus(val label: String) {
    CLEAN("Clean"),
    DIRTY("In the laundry"),
    AWAY("Away"), // lent out, at the tailor, in storage
}

/**
 * Formality scale used everywhere:
 * 1 = lounge / athletic, 2 = casual, 3 = smart casual, 4 = business, 5 = formal.
 */
object Formality {
    const val LOUNGE = 1
    const val CASUAL = 2
    const val SMART_CASUAL = 3
    const val BUSINESS = 4
    const val FORMAL = 5
    val range = LOUNGE..FORMAL
    fun label(level: Int): String = when (level) {
        1 -> "Lounge"
        2 -> "Casual"
        3 -> "Smart casual"
        4 -> "Business"
        else -> "Formal"
    }
}

/**
 * Warmth scale: 1 = very light (tanks, shorts, sandals), 3 = medium,
 * 5 = very warm (puffers, heavy wool).
 */
object Warmth {
    val range = 1..5
    fun label(level: Int): String = when (level) {
        1 -> "Very light"
        2 -> "Light"
        3 -> "Medium"
        4 -> "Warm"
        else -> "Very warm"
    }
}

/** What the outfit is for. Each occasion accepts a formality window. */
enum class Occasion(val label: String, val formality: IntRange, val ideal: Int) {
    LOUNGE("Lounge", 1..2, 1),
    ACTIVE("Active", 1..2, 1),
    CASUAL("Casual", 1..3, 2),
    WORK("Work", 3..4, 3),
    SMART("Smart", 3..5, 4),
    DATE("Date night", 2..4, 3),
    FORMAL("Formal", 4..5, 5),
    TRAVEL("Travel", 1..3, 2),
}

/**
 * Named fashion colour palette. Every garment colour is snapped to one of these.
 * [neutral] colours pair with anything; [hue] is in degrees (ignored for neutrals).
 */
enum class FashionColor(val label: String, val rgb: Int, val neutral: Boolean, val hue: Float) {
    BLACK("Black", 0x111111, true, 0f),
    CHARCOAL("Charcoal", 0x3A3A3C, true, 0f),
    GREY("Grey", 0x8E8E93, true, 0f),
    LIGHT_GREY("Light grey", 0xC9C9CC, true, 0f),
    WHITE("White", 0xF7F7F5, true, 0f),
    CREAM("Cream", 0xEFE6D2, true, 45f),
    BEIGE("Beige", 0xD8C3A5, true, 35f),
    CAMEL("Camel", 0xB9864D, true, 32f),
    KHAKI("Khaki", 0xA89F72, true, 50f),
    BROWN("Brown", 0x6B4426, true, 26f),
    NAVY("Navy", 0x1F2A44, true, 222f),
    DENIM("Denim", 0x4A6A8F, true, 212f),
    OLIVE("Olive", 0x6B6B2E, true, 60f),
    BLUE("Blue", 0x2F6FDE, false, 218f),
    LIGHT_BLUE("Light blue", 0xA9C8EA, false, 212f),
    TEAL("Teal", 0x1F7A7A, false, 180f),
    GREEN("Green", 0x2E8B57, false, 146f),
    MINT("Mint", 0xA8E0C8, false, 154f),
    YELLOW("Yellow", 0xF2C94C, false, 45f),
    MUSTARD("Mustard", 0xC9A227, false, 46f),
    ORANGE("Orange", 0xE8772E, false, 24f),
    RUST("Rust", 0xA94D2A, false, 16f),
    RED("Red", 0xC62828, false, 0f),
    BURGUNDY("Burgundy", 0x6D1A2C, false, 345f),
    PINK("Pink", 0xF2A6BF, false, 340f),
    HOT_PINK("Hot pink", 0xE0457B, false, 340f),
    PURPLE("Purple", 0x6A3FA0, false, 268f),
    LAVENDER("Lavender", 0xC7B8E6, false, 260f),
    GOLD("Gold", 0xC8A24A, true, 43f),
    SILVER("Silver", 0xBFC3C8, true, 0f),
}

/** Engine view of one wardrobe item. */
data class Garment(
    val id: Long,
    val category: Category,
    val subcategory: String = "",
    val colors: List<FashionColor> = emptyList(), // primary first
    val pattern: Pattern = Pattern.SOLID,
    val formality: Int = Formality.CASUAL,
    val warmth: Int = 3,
    val seasons: Set<Season> = emptySet(), // empty = all seasons
    val sporty: Boolean = false,
    val waterproof: Boolean = false,
    val status: GarmentStatus = GarmentStatus.CLEAN,
    val favorite: Boolean = false,
    val wearCount: Int = 0,
    val lastWorn: LocalDate? = null,
    val price: Double? = null,
    val added: LocalDate? = null,
    val wearsSinceWash: Int = 0,
    /** 0 = never needs washing (shoes, bags, jewellery). */
    val wearsBeforeWash: Int = 1,
)

enum class WeatherCondition(val label: String) {
    CLEAR("Clear"), PARTLY_CLOUDY("Partly cloudy"), CLOUDY("Cloudy"), FOG("Fog"),
    DRIZZLE("Drizzle"), RAIN("Rain"), SNOW("Snow"), STORM("Thunderstorm"),
}

/** Weather for the hours the outfit will be worn. Temperatures in °C. */
data class WeatherContext(
    val tempMinC: Double,
    val tempMaxC: Double,
    val feelsLikeC: Double? = null,
    val precipitationProbability: Int = 0, // 0..100
    val windKph: Double = 0.0,
    val condition: WeatherCondition = WeatherCondition.CLEAR,
    val uvIndex: Double = 0.0,
)

/** What the weather demands from an outfit. Produced by [WeatherAdvisor]. */
data class WeatherNeeds(
    val targetWarmth: Double,       // ideal average warmth of core pieces, 1..5
    val needsOuterwear: Boolean,
    val suggestsLayer: Boolean,
    val allowsShortsAndOpenShoes: Boolean,
    val wantsWaterproof: Boolean,
    val summary: String,            // e.g. "Cool and showery — bring a layer"
)

data class StylingRequest(
    val date: LocalDate,
    val occasion: Occasion = Occasion.CASUAL,
    val weather: WeatherContext? = null,
    val hemisphere: Hemisphere = Hemisphere.NORTH,
    /** Items that must appear (e.g. "style this jacket"). */
    val anchorItemIds: Set<Long> = emptySet(),
    val excludeItemIds: Set<Long> = emptySet(),
    val count: Int = 3,
    /** Signatures (see [OutfitSignature]) of recently suggested/worn outfits to avoid repeating. */
    val recentSignatures: Set<String> = emptySet(),
    val includeAccessories: Boolean = true,
    /** Change to get a different but equally valid set ("regenerate"). */
    val seed: Long = 0L,
)

data class SuggestedOutfit(
    /** Garment ids ordered by [Category.order]. */
    val itemIds: List<Long>,
    val score: Double,
    /** Short, human readable stylist notes explaining the pick. */
    val reasons: List<String>,
) {
    val signature: String get() = OutfitSignature.of(itemIds)
}

object OutfitSignature {
    /** Order-independent identity of a set of items. */
    fun of(itemIds: Collection<Long>): String = itemIds.sorted().joinToString("-")
}

/** Learned preferences from the user's reactions. Immutable; update via [FeedbackLearner]. */
data class AffinityModel(
    /** Key is [pairKey] of two garment ids. Positive = they go well together for this user. */
    val pairScores: Map<String, Double> = emptyMap(),
    /** Per-garment preference, positive = user likes wearing it. */
    val itemScores: Map<Long, Double> = emptyMap(),
) {
    fun pair(a: Long, b: Long): Double = pairScores[pairKey(a, b)] ?: 0.0
    fun item(id: Long): Double = itemScores[id] ?: 0.0

    companion object {
        fun pairKey(a: Long, b: Long): String = if (a < b) "$a:$b" else "$b:$a"
    }
}

enum class FeedbackType {
    WORN,       // user wore the outfit (strong positive)
    SAVED,      // saved / favourited the outfit
    LIKED,      // thumbs up on a suggestion
    REJECTED,   // thumbs down / skipped a suggestion
}

data class FeedbackEvent(val type: FeedbackType, val itemIds: List<Long>)

/** One day of logged wear. */
data class WearRecord(val date: LocalDate, val itemIds: List<Long>)

/** Shuffle ("Dress me") configuration. */
data class ShuffleRequest(
    /** Rows to fill, in display order, e.g. [TOP, BOTTOM, SHOES]. */
    val rows: List<Category>,
    /** Pinned picks that must be kept. */
    val locked: Map<Category, Long> = emptyMap(),
    val onlyClean: Boolean = true,
    val season: Season? = null,
    /** When true, only return combinations that pass basic colour/pattern/formality checks. */
    val smart: Boolean = true,
    /** Bias towards rarely worn pieces. */
    val favourLeastWorn: Boolean = false,
    val seed: Long = 0L,
)

data class TripSpec(
    val startDate: LocalDate,
    val endDate: LocalDate,
    /** One entry per day if known (forecast), otherwise empty. */
    val weatherByDay: List<WeatherContext> = emptyList(),
    /** Outfits the user already planned for the trip (garment ids per outfit). */
    val plannedOutfits: List<List<Long>> = emptyList(),
    val occasions: Set<Occasion> = setOf(Occasion.CASUAL),
    val laundryAvailable: Boolean = false,
    val hemisphere: Hemisphere = Hemisphere.NORTH,
)

enum class PackCategory(val label: String) {
    CLOTHING("Clothes"),
    UNDERWEAR("Underwear & socks"),
    SLEEP("Sleepwear"),
    TOILETRIES("Toiletries"),
    TECH("Tech"),
    DOCUMENTS("Documents"),
    WEATHER("Weather extras"),
    OTHER("Other"),
}

data class PackItem(
    val label: String,
    val category: PackCategory,
    val quantity: Int = 1,
    /** Set when the line is a specific wardrobe item. */
    val garmentId: Long? = null,
    val reason: String? = null,
)

data class PackingPlan(
    val items: List<PackItem>,
    /** Day-by-day outfits (garment ids) the plan was built from. */
    val outfits: List<List<Long>>,
    val notes: List<String>,
)

data class CountedGarment(val garmentId: Long, val value: Double)

data class StatsReport(
    val totalItems: Int,
    val closetValue: Double,
    val itemsWithPrice: Int,
    /** Average cost per wear over items that have a price and at least one wear. */
    val averageCostPerWear: Double?,
    /** Share (0..1) of items worn at least once in the last 30 / 90 days. */
    val utilisation30: Double,
    val utilisation90: Double,
    val mostWorn: List<CountedGarment>,          // value = wear count
    val leastWorn: List<CountedGarment>,         // value = wear count (excludes never worn)
    val neverWorn: List<Long>,
    val bestCostPerWear: List<CountedGarment>,   // value = cost per wear, ascending
    val worstCostPerWear: List<CountedGarment>,  // value = cost per wear, descending
    /** Not worn for 60+ days (but worn before). */
    val forgotten: List<Long>,
    val byCategory: Map<Category, Int>,
    /** Primary colour counts. */
    val byColor: Map<FashionColor, Int>,
    /** Logged outfit-days per calendar month, oldest first, last 12 months including the current one. */
    val wearsPerMonth: List<Pair<String, Int>>, // "2026-10" to count
    val currentStreak: Int,
    val longestStreak: Int,
    val daysLogged: Int,
)
