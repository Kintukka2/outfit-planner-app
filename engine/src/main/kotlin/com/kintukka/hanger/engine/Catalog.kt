package com.kintukka.hanger.engine

import java.time.LocalDate

/** Curated subcategory presets (formality, warmth, wears before wash, flags, seasons). */
internal object Catalog {
    private val SPRING = Season.SPRING
    private val SUMMER = Season.SUMMER
    private val AUTUMN = Season.AUTUMN
    private val WINTER = Season.WINTER

    private fun p(
        name: String,
        category: Category,
        formality: Int,
        warmth: Int,
        wearsBeforeWash: Int,
        sporty: Boolean = false,
        waterproof: Boolean = false,
        seasons: Set<Season> = emptySet(),
    ) = SubcategoryPreset(name, category, formality, warmth, wearsBeforeWash, sporty, waterproof, seasons)

    val presets: List<SubcategoryPreset> = listOf(
        // Tops
        p("T-shirt", Category.TOP, 2, 2, 1),
        p("Tank top", Category.TOP, 2, 1, 1, seasons = setOf(SPRING, SUMMER)),
        p("Crop top", Category.TOP, 2, 1, 1, seasons = setOf(SPRING, SUMMER)),
        p("Shirt", Category.TOP, 3, 2, 2),
        p("Dress shirt", Category.TOP, 4, 2, 1),
        p("Blouse", Category.TOP, 3, 2, 2),
        p("Polo", Category.TOP, 2, 2, 1),
        p("Long-sleeve top", Category.TOP, 2, 2, 1),
        p("Turtleneck", Category.TOP, 3, 3, 2, seasons = setOf(AUTUMN, WINTER)),
        p("Sweatshirt", Category.TOP, 2, 3, 3),
        p("Hoodie", Category.TOP, 1, 3, 3),
        p("Sports top", Category.TOP, 1, 1, 1, sporty = true),

        // Bottoms
        p("Jeans", Category.BOTTOM, 2, 3, 4),
        p("Chinos", Category.BOTTOM, 3, 3, 3),
        p("Trousers", Category.BOTTOM, 4, 3, 3),
        p("Wide-leg trousers", Category.BOTTOM, 3, 3, 3),
        p("Cargo trousers", Category.BOTTOM, 2, 3, 3),
        p("Corduroys", Category.BOTTOM, 3, 4, 3, seasons = setOf(AUTUMN, WINTER)),
        p("Shorts", Category.BOTTOM, 2, 1, 2, seasons = setOf(SUMMER)),
        p("Tailored shorts", Category.BOTTOM, 3, 1, 2, seasons = setOf(SPRING, SUMMER)),
        p("Running shorts", Category.BOTTOM, 1, 1, 1, sporty = true, seasons = setOf(SPRING, SUMMER)),
        p("Skirt", Category.BOTTOM, 3, 2, 2),
        p("Midi skirt", Category.BOTTOM, 3, 2, 2),
        p("Leggings", Category.BOTTOM, 1, 2, 1, sporty = true),
        p("Joggers", Category.BOTTOM, 1, 3, 2),

        // Dresses & jumpsuits
        p("Dress", Category.ONE_PIECE, 3, 2, 2),
        p("Maxi dress", Category.ONE_PIECE, 3, 2, 2, seasons = setOf(SPRING, SUMMER)),
        p("Slip dress", Category.ONE_PIECE, 3, 1, 2),
        p("Sundress", Category.ONE_PIECE, 2, 1, 2, seasons = setOf(SUMMER)),
        p("Shirt dress", Category.ONE_PIECE, 3, 2, 2),
        p("Knit dress", Category.ONE_PIECE, 3, 4, 3, seasons = setOf(AUTUMN, WINTER)),
        p("Evening dress", Category.ONE_PIECE, 5, 2, 3),
        p("Jumpsuit", Category.ONE_PIECE, 3, 2, 2),
        p("Playsuit", Category.ONE_PIECE, 2, 1, 2, seasons = setOf(SUMMER)),
        p("Overalls", Category.ONE_PIECE, 2, 3, 3),

        // Layers
        p("Knit sweater", Category.LAYER, 2, 4, 4),
        p("Cardigan", Category.LAYER, 3, 3, 4),
        p("Blazer", Category.LAYER, 4, 3, 8),
        p("Fleece", Category.LAYER, 1, 4, 4),
        p("Gilet", Category.LAYER, 2, 3, 6),
        p("Zip hoodie", Category.LAYER, 1, 3, 3),
        p("Overshirt", Category.LAYER, 2, 3, 4),
        p("Sweater vest", Category.LAYER, 3, 3, 4),
        p("Waistcoat", Category.LAYER, 4, 2, 6),

        // Outerwear
        p("Coat", Category.OUTERWEAR, 4, 5, 15),
        p("Trench coat", Category.OUTERWEAR, 4, 4, 15, waterproof = true),
        p("Puffer jacket", Category.OUTERWEAR, 2, 5, 20, seasons = setOf(AUTUMN, WINTER)),
        p("Parka", Category.OUTERWEAR, 2, 5, 15, waterproof = true, seasons = setOf(AUTUMN, WINTER)),
        p("Jacket", Category.OUTERWEAR, 3, 3, 10),
        p("Denim jacket", Category.OUTERWEAR, 2, 3, 10),
        p("Leather jacket", Category.OUTERWEAR, 3, 3, 20),
        p("Bomber jacket", Category.OUTERWEAR, 2, 3, 10),
        p("Raincoat", Category.OUTERWEAR, 2, 3, 15, waterproof = true),
        p("Windbreaker", Category.OUTERWEAR, 1, 2, 10, sporty = true),

        // Shoes (never washed)
        p("Sneakers", Category.SHOES, 2, 2, 0),
        p("Running shoes", Category.SHOES, 1, 2, 0, sporty = true),
        p("Hiking boots", Category.SHOES, 1, 4, 0, sporty = true, waterproof = true),
        p("Boots", Category.SHOES, 3, 4, 0),
        p("Chelsea boots", Category.SHOES, 3, 4, 0),
        p("Ankle boots", Category.SHOES, 3, 4, 0),
        p("Loafers", Category.SHOES, 3, 2, 0),
        p("Flats", Category.SHOES, 3, 2, 0),
        p("Heels", Category.SHOES, 4, 2, 0),
        p("Dress shoes", Category.SHOES, 5, 2, 0),
        p("Sandals", Category.SHOES, 2, 1, 0, seasons = setOf(SUMMER)),
        p("Espadrilles", Category.SHOES, 2, 1, 0, seasons = setOf(SPRING, SUMMER)),
        p("Flip-flops", Category.SHOES, 1, 1, 0, seasons = setOf(SUMMER)),
        p("Rain boots", Category.SHOES, 2, 3, 0, waterproof = true),

        // Bags
        p("Tote", Category.BAG, 2, 2, 0),
        p("Crossbody", Category.BAG, 2, 2, 0),
        p("Backpack", Category.BAG, 2, 2, 0),
        p("Handbag", Category.BAG, 3, 2, 0),
        p("Clutch", Category.BAG, 4, 2, 0),
        p("Belt bag", Category.BAG, 2, 2, 0),
        p("Gym bag", Category.BAG, 1, 2, 0, sporty = true),

        // Accessories
        p("Belt", Category.ACCESSORY, 2, 2, 0),
        p("Scarf", Category.ACCESSORY, 3, 4, 10, seasons = setOf(AUTUMN, WINTER)),
        p("Hat", Category.ACCESSORY, 2, 2, 10),
        p("Beanie", Category.ACCESSORY, 2, 4, 10, seasons = setOf(AUTUMN, WINTER)),
        p("Cap", Category.ACCESSORY, 1, 2, 8),
        p("Gloves", Category.ACCESSORY, 2, 4, 10, seasons = setOf(AUTUMN, WINTER)),
        p("Sunglasses", Category.ACCESSORY, 2, 1, 0, seasons = setOf(SPRING, SUMMER)),
        p("Watch", Category.ACCESSORY, 3, 1, 0),
        p("Necklace", Category.ACCESSORY, 3, 1, 0),
        p("Earrings", Category.ACCESSORY, 3, 1, 0),
        p("Bracelet", Category.ACCESSORY, 3, 1, 0),
        p("Ring", Category.ACCESSORY, 3, 1, 0),
        p("Tie", Category.ACCESSORY, 4, 1, 0),

        // Other
        p("Swimwear", Category.OTHER, 2, 1, 1, seasons = setOf(SUMMER)),
        p("Socks", Category.OTHER, 2, 2, 1),
        p("Tights", Category.OTHER, 3, 3, 1, seasons = setOf(AUTUMN, WINTER)),
        p("Sleepwear", Category.OTHER, 1, 2, 3),
        p("Loungewear", Category.OTHER, 1, 3, 3),
    )

    private val byName: Map<String, SubcategoryPreset> = presets.associateBy { it.name.lowercase() }

    /** Common alternative names (UK/US) resolved by [find]. */
    private val aliases: Map<String, String> = mapOf(
        "tee" to "T-shirt", "t shirt" to "T-shirt", "tshirt" to "T-shirt",
        "tank" to "Tank top", "vest top" to "Tank top", "camisole" to "Tank top",
        "jumper" to "Knit sweater", "sweater" to "Knit sweater", "pullover" to "Knit sweater",
        "trainers" to "Sneakers", "pants" to "Trousers", "slacks" to "Trousers",
        "vest" to "Gilet", "purse" to "Handbag", "rain jacket" to "Raincoat",
        "wellies" to "Rain boots", "wellingtons" to "Rain boots", "pyjamas" to "Sleepwear", "pajamas" to "Sleepwear",
        "swimsuit" to "Swimwear", "bikini" to "Swimwear", "romper" to "Playsuit", "dungarees" to "Overalls",
        "sweatpants" to "Joggers", "track pants" to "Joggers", "down jacket" to "Puffer jacket",
    )

    private val byCategory: Map<Category, List<SubcategoryPreset>> = presets.groupBy { it.category }

    fun forCategory(category: Category): List<SubcategoryPreset> = byCategory[category].orEmpty()

    fun find(name: String): SubcategoryPreset? {
        val key = name.trim().lowercase()
        if (key.isEmpty()) return null
        return byName[key] ?: aliases[key]?.let { byName[it.lowercase()] }
    }
}

/** Maps ML Kit-style image labels to the most likely preset. */
internal object Guesser {
    private class Rule(val keywords: List<String>, val preset: String, val specificity: Double)

    private fun r(preset: String, specificity: Double, vararg keywords: String) = Rule(keywords.toList(), preset, specificity)

    // Generic labels ("Clothing", "Textile", "Fashion", "Fabric", "Pattern"...) deliberately have no rule.
    private val rules: List<Rule> = listOf(
        r("T-shirt", 1.0, "t-shirt", "t shirt", "tshirt", "tee"),
        r("Tank top", 1.0, "tank top", "tank", "camisole"),
        r("Crop top", 1.0, "crop top"),
        r("Blouse", 1.0, "blouse"),
        r("Polo", 0.9, "polo shirt", "polo"),
        r("Dress shirt", 1.0, "dress shirt"),
        r("Shirt", 0.8, "shirt"),
        r("Turtleneck", 1.0, "turtleneck", "polo neck"),
        r("Sweatshirt", 1.0, "sweatshirt"),
        r("Hoodie", 1.0, "hoodie", "hooded"),
        r("Sports top", 0.6, "jersey", "sportswear", "activewear"),
        r("T-shirt", 0.35, "top"),
        r("Jeans", 1.0, "jeans"),
        r("Jeans", 0.5, "denim"),
        r("Chinos", 1.0, "chinos", "khakis"),
        r("Trousers", 0.9, "trousers", "pants", "slacks"),
        r("Shorts", 1.0, "shorts"),
        r("Skirt", 1.0, "skirt", "miniskirt"),
        r("Leggings", 1.0, "leggings"),
        r("Joggers", 1.0, "joggers", "sweatpants", "tracksuit"),
        r("Evening dress", 1.0, "gown", "evening dress", "wedding dress"),
        r("Dress", 0.9, "dress"),
        r("Jumpsuit", 1.0, "jumpsuit"),
        r("Playsuit", 1.0, "playsuit", "romper"),
        r("Overalls", 1.0, "overalls", "dungarees"),
        r("Knit sweater", 1.0, "sweater", "jumper", "knitwear", "pullover"),
        r("Cardigan", 1.0, "cardigan"),
        r("Blazer", 0.9, "blazer", "suit", "tuxedo"),
        r("Fleece", 0.9, "fleece"),
        r("Gilet", 0.8, "vest", "gilet"),
        r("Trench coat", 1.0, "trench coat", "trench"),
        r("Raincoat", 1.0, "raincoat", "rain coat", "poncho"),
        r("Parka", 1.0, "parka"),
        r("Puffer jacket", 1.0, "puffer", "down jacket"),
        r("Windbreaker", 1.0, "windbreaker"),
        r("Leather jacket", 1.0, "leather jacket"),
        r("Denim jacket", 1.0, "denim jacket", "jean jacket"),
        r("Bomber jacket", 1.0, "bomber"),
        r("Coat", 0.9, "coat", "overcoat"),
        r("Jacket", 0.8, "jacket"),
        r("Coat", 0.4, "outerwear"),
        r("Sneakers", 1.0, "sneakers", "sneaker", "trainers"),
        r("Running shoes", 1.0, "running shoe", "athletic shoe"),
        r("Heels", 1.0, "high heels", "heels", "stiletto", "pumps"),
        r("Sandals", 1.0, "sandal"),
        r("Flip-flops", 1.0, "flip-flops", "flip flops"),
        r("Loafers", 1.0, "loafer", "moccasin"),
        r("Rain boots", 1.0, "rain boot", "wellington", "gumboot"),
        r("Hiking boots", 1.0, "hiking boot"),
        r("Boots", 0.9, "boot"),
        r("Sneakers", 0.45, "shoe", "footwear"),
        r("Handbag", 1.0, "handbag", "purse"),
        r("Backpack", 1.0, "backpack", "rucksack"),
        r("Tote", 1.0, "tote"),
        r("Clutch", 1.0, "clutch"),
        r("Handbag", 0.55, "bag"),
        r("Sunglasses", 1.0, "sunglasses", "eyewear"),
        r("Beanie", 1.0, "beanie"),
        r("Cap", 0.9, "cap", "baseball cap"),
        r("Hat", 0.9, "hat", "fedora", "sun hat"),
        r("Scarf", 1.0, "scarf", "shawl"),
        r("Gloves", 1.0, "glove", "mitten"),
        r("Watch", 1.0, "watch", "wristwatch"),
        r("Necklace", 1.0, "necklace", "pendant"),
        r("Earrings", 1.0, "earring"),
        r("Bracelet", 1.0, "bracelet", "bangle"),
        r("Ring", 0.7, "ring"),
        r("Necklace", 0.6, "jewellery", "jewelry"),
        r("Belt", 1.0, "belt"),
        r("Tie", 1.0, "tie", "necktie", "bow tie"),
        r("Socks", 1.0, "sock"),
        r("Tights", 1.0, "tights", "stockings"),
        r("Swimwear", 1.0, "swimwear", "swimsuit", "bikini", "swim trunks"),
        r("Sleepwear", 1.0, "pajamas", "pyjamas", "nightwear", "sleepwear"),
    )

    private const val MIN_SCORE = 0.15
    private val splitter = Regex("[^a-z0-9]+")

    private fun matches(label: String, tokens: List<String>, keyword: String): Boolean =
        if (keyword.contains(' ') || keyword.contains('-')) {
            label.contains(keyword)
        } else {
            tokens.any { it == keyword || it == keyword + "s" || it == keyword + "es" }
        }

    fun guess(labels: List<Pair<String, Float>>): SubcategoryPreset? {
        val scores = LinkedHashMap<String, Double>()
        val normalised = labels.map { (l, c) -> l.trim().lowercase() to c.toDouble().coerceIn(0.0, 1.0) }
        for ((label, confidence) in normalised) {
            if (label.isEmpty() || confidence <= 0.0) continue
            val tokens = label.split(splitter).filter { it.isNotEmpty() }
            var bestRule: Rule? = null
            var bestLength = -1
            for (rule in rules) {
                for (kw in rule.keywords) {
                    if (!matches(label, tokens, kw)) continue
                    if (kw.length > bestLength || (kw.length == bestLength && rule.specificity > (bestRule?.specificity ?: 0.0))) {
                        bestRule = rule
                        bestLength = kw.length
                    }
                }
            }
            val rule = bestRule ?: continue
            scores[rule.preset] = (scores[rule.preset] ?: 0.0) + confidence * rule.specificity
        }
        val best = scores.maxByOrNull { it.value } ?: return null
        if (best.value < MIN_SCORE) return null
        var name = best.key
        // Material hints refine a plain "Jacket".
        if (name == "Jacket") {
            val text = normalised.filter { it.second >= 0.4 }.joinToString(" ") { it.first }
            name = when {
                text.contains("denim") || text.contains("jeans") -> "Denim jacket"
                text.contains("leather") -> "Leather jacket"
                else -> name
            }
        }
        return Catalog.find(name)
    }
}

internal object Laundry {
    fun categoryDefault(category: Category): Int = when (category) {
        Category.TOP -> 1
        Category.BOTTOM -> 3
        Category.ONE_PIECE -> 2
        Category.LAYER -> 4
        Category.OUTERWEAR -> 15
        Category.SHOES, Category.BAG, Category.ACCESSORY -> 0
        Category.OTHER -> 1
    }

    fun defaultWearsBeforeWash(category: Category, subcategory: String): Int =
        Catalog.find(subcategory)?.wearsBeforeWash ?: categoryDefault(category)

    fun afterWear(g: Garment, date: LocalDate): Garment {
        val wears = g.wearsSinceWash + 1
        val dirty = g.wearsBeforeWash > 0 && wears >= g.wearsBeforeWash
        val last = g.lastWorn
        return g.copy(
            wearCount = g.wearCount + 1,
            lastWorn = if (last != null && last.isAfter(date)) last else date,
            wearsSinceWash = wears,
            status = if (dirty) GarmentStatus.DIRTY else g.status,
        )
    }

    fun washed(g: Garment): Garment = g.copy(
        status = if (g.status == GarmentStatus.DIRTY) GarmentStatus.CLEAN else g.status,
        wearsSinceWash = 0,
    )
}
