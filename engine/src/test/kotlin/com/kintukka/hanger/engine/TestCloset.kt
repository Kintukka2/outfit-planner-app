package com.kintukka.hanger.engine

import java.time.LocalDate
import kotlin.random.Random

/** Shared fixtures: a realistic sample closet built from the presets, plus synthetic closets for perf tests. */
object TestCloset {
    val TODAY: LocalDate = LocalDate.of(2026, 10, 26) // autumn in the north

    // Tops
    const val WHITE_TEE = 1L
    const val BLACK_TEE = 2L
    const val BLUE_SHIRT = 3L
    const val WHITE_SHIRT = 4L
    const val CREAM_BLOUSE = 5L
    const val GREY_HOODIE = 6L
    const val NAVY_SWEATSHIRT = 7L
    const val WHITE_TANK = 8L
    const val SPORTS_TOP = 9L
    const val BRETON = 10L
    const val GREEN_BLOUSE = 11L

    // Bottoms
    const val JEANS = 12L
    const val OLIVE_CHINOS = 13L
    const val CHARCOAL_TROUSERS = 14L
    const val KHAKI_SHORTS = 15L
    const val BLACK_SKIRT = 16L
    const val LEGGINGS = 17L
    const val FLORAL_SKIRT = 18L

    // One-pieces
    const val NAVY_DRESS = 19L
    const val RUST_MAXI = 20L

    // Layers
    const val RUST_KNIT = 21L
    const val GREY_CARDIGAN = 22L
    const val NAVY_BLAZER = 23L

    // Outerwear
    const val CAMEL_COAT = 24L
    const val BEIGE_TRENCH = 25L
    const val DENIM_JACKET = 26L
    const val BLACK_PUFFER = 27L

    // Shoes
    const val WHITE_SNEAKERS = 28L
    const val BROWN_BOOTS = 29L
    const val BLACK_LOAFERS = 30L
    const val SANDALS = 31L
    const val RUNNING_SHOES = 32L
    const val BLACK_HEELS = 33L

    // Bags & accessories
    const val CAMEL_TOTE = 34L
    const val BLACK_CROSSBODY = 35L
    const val BURGUNDY_SCARF = 36L
    const val SUNGLASSES = 37L
    const val SILVER_WATCH = 38L

    /** Builds a garment from a preset so tests also exercise the catalogue. */
    fun item(
        id: Long,
        subcategory: String,
        vararg colors: FashionColor,
        pattern: Pattern = Pattern.SOLID,
        favorite: Boolean = false,
        wearCount: Int = 0,
        lastWorn: LocalDate? = null,
        status: GarmentStatus = GarmentStatus.CLEAN,
        price: Double? = null,
        seasons: Set<Season>? = null,
    ): Garment {
        val p = requireNotNull(Subcategories.find(subcategory)) { "No preset $subcategory" }
        return Garment(
            id = id,
            category = p.category,
            subcategory = p.name,
            colors = colors.toList(),
            pattern = pattern,
            formality = p.formality,
            warmth = p.warmth,
            seasons = seasons ?: p.seasons,
            sporty = p.sporty,
            waterproof = p.waterproof,
            status = status,
            favorite = favorite,
            wearCount = wearCount,
            lastWorn = lastWorn,
            price = price,
            wearsBeforeWash = p.wearsBeforeWash,
        )
    }

    private fun ago(days: Long) = TODAY.minusDays(days)

    fun sample(): List<Garment> = listOf(
        item(WHITE_TEE, "T-shirt", FashionColor.WHITE, wearCount = 20, lastWorn = ago(3), price = 20.0),
        item(BLACK_TEE, "T-shirt", FashionColor.BLACK, wearCount = 15, lastWorn = ago(6), price = 20.0),
        item(BLUE_SHIRT, "Shirt", FashionColor.LIGHT_BLUE, wearCount = 8, lastWorn = ago(10), price = 60.0),
        item(WHITE_SHIRT, "Shirt", FashionColor.WHITE, wearCount = 6, lastWorn = ago(14), price = 55.0),
        item(CREAM_BLOUSE, "Blouse", FashionColor.CREAM, wearCount = 3, lastWorn = ago(30), price = 70.0),
        item(GREY_HOODIE, "Hoodie", FashionColor.GREY, wearCount = 25, lastWorn = ago(2), price = 45.0),
        item(NAVY_SWEATSHIRT, "Sweatshirt", FashionColor.NAVY, wearCount = 9, lastWorn = ago(8)),
        item(WHITE_TANK, "Tank top", FashionColor.WHITE, wearCount = 5, lastWorn = ago(70)),
        item(SPORTS_TOP, "Sports top", FashionColor.BLACK, wearCount = 12, lastWorn = ago(4)),
        item(BRETON, "Long-sleeve top", FashionColor.NAVY, FashionColor.WHITE, pattern = Pattern.STRIPE, wearCount = 7, lastWorn = ago(20)),
        item(GREEN_BLOUSE, "Blouse", FashionColor.GREEN, price = 80.0),
        item(JEANS, "Jeans", FashionColor.DENIM, wearCount = 30, lastWorn = ago(1), price = 90.0),
        item(OLIVE_CHINOS, "Chinos", FashionColor.OLIVE, wearCount = 4, lastWorn = ago(35), price = 65.0),
        item(CHARCOAL_TROUSERS, "Trousers", FashionColor.CHARCOAL, wearCount = 10, lastWorn = ago(5), price = 120.0),
        item(KHAKI_SHORTS, "Shorts", FashionColor.KHAKI, wearCount = 6, lastWorn = ago(80)),
        item(BLACK_SKIRT, "Skirt", FashionColor.BLACK, wearCount = 5, lastWorn = ago(12)),
        item(LEGGINGS, "Leggings", FashionColor.BLACK, wearCount = 14, lastWorn = ago(4)),
        item(FLORAL_SKIRT, "Midi skirt", FashionColor.PINK, FashionColor.CREAM, pattern = Pattern.FLORAL, wearCount = 2, lastWorn = ago(40)),
        item(NAVY_DRESS, "Dress", FashionColor.NAVY, wearCount = 4, lastWorn = ago(25)),
        item(RUST_MAXI, "Maxi dress", FashionColor.RUST, pattern = Pattern.FLORAL, wearCount = 3, lastWorn = ago(60)),
        item(RUST_KNIT, "Knit sweater", FashionColor.RUST, wearCount = 6, lastWorn = ago(9)),
        item(GREY_CARDIGAN, "Cardigan", FashionColor.GREY, wearCount = 8, lastWorn = ago(11)),
        item(NAVY_BLAZER, "Blazer", FashionColor.NAVY, wearCount = 5, lastWorn = ago(16), price = 180.0),
        item(CAMEL_COAT, "Coat", FashionColor.CAMEL, wearCount = 12, lastWorn = ago(3), price = 250.0),
        item(BEIGE_TRENCH, "Trench coat", FashionColor.BEIGE, wearCount = 6, lastWorn = ago(20)),
        item(DENIM_JACKET, "Denim jacket", FashionColor.DENIM, wearCount = 10, lastWorn = ago(7)),
        item(BLACK_PUFFER, "Puffer jacket", FashionColor.BLACK, wearCount = 2, lastWorn = ago(200)),
        item(WHITE_SNEAKERS, "Sneakers", FashionColor.WHITE, favorite = true, wearCount = 40, lastWorn = ago(2), price = 110.0),
        item(BROWN_BOOTS, "Chelsea boots", FashionColor.BROWN, wearCount = 18, lastWorn = ago(5), price = 160.0),
        item(BLACK_LOAFERS, "Loafers", FashionColor.BLACK, wearCount = 9, lastWorn = ago(9)),
        item(SANDALS, "Sandals", FashionColor.BEIGE, wearCount = 11, lastWorn = ago(75)),
        item(RUNNING_SHOES, "Running shoes", FashionColor.GREY, wearCount = 20, lastWorn = ago(4)),
        item(BLACK_HEELS, "Heels", FashionColor.BLACK, wearCount = 3, lastWorn = ago(45)),
        item(CAMEL_TOTE, "Tote", FashionColor.CAMEL, wearCount = 15, lastWorn = ago(3)),
        item(BLACK_CROSSBODY, "Crossbody", FashionColor.BLACK, wearCount = 10, lastWorn = ago(6)),
        item(BURGUNDY_SCARF, "Scarf", FashionColor.BURGUNDY, wearCount = 4, lastWorn = ago(150)),
        item(SUNGLASSES, "Sunglasses", FashionColor.BLACK, wearCount = 30, lastWorn = ago(50)),
        item(SILVER_WATCH, "Watch", FashionColor.SILVER, wearCount = 60, lastWorn = ago(1)),
    )

    fun byId(closet: List<Garment> = sample()): Map<Long, Garment> = closet.associateBy { it.id }

    val CORE = setOf(Category.TOP, Category.BOTTOM, Category.ONE_PIECE, Category.SHOES)

    fun coreIds(outfit: SuggestedOutfit, closet: Map<Long, Garment>): Set<Long> =
        outfit.itemIds.filter { closet.getValue(it).category in CORE }.toSet()

    fun categories(ids: List<Long>, closet: Map<Long, Garment>): List<Category> = ids.map { closet.getValue(it).category }

    /** Garments are a complete outfit: (top + bottom or one-piece) + shoes. */
    fun isComplete(ids: List<Long>, closet: Map<Long, Garment>): Boolean {
        val cats = categories(ids, closet)
        return Category.SHOES in cats && (Category.ONE_PIECE in cats || (Category.TOP in cats && Category.BOTTOM in cats))
    }

    /** Deterministic synthetic closet with a realistic category mix. */
    fun synthetic(size: Int, seed: Int = 42): List<Garment> {
        val rnd = Random(seed)
        val mix = listOf(
            Category.TOP to 30, Category.BOTTOM to 18, Category.ONE_PIECE to 8, Category.LAYER to 10,
            Category.OUTERWEAR to 7, Category.SHOES to 12, Category.BAG to 6, Category.ACCESSORY to 9,
        )
        val total = mix.sumOf { it.second }
        val colors = FashionColor.entries
        val patterns = listOf(
            Pattern.SOLID, Pattern.SOLID, Pattern.SOLID, Pattern.SOLID, Pattern.SOLID, Pattern.TEXTURE,
            Pattern.STRIPE, Pattern.CHECK, Pattern.FLORAL, Pattern.DOT,
        )
        return (1..size).map { i ->
            var r = rnd.nextInt(total)
            val category = mix.first { (_, w) -> (r < w).also { r -= w } }.first
            val preset = Subcategories.forCategory(category).random(rnd)
            val colorCount = 1 + rnd.nextInt(2)
            val worn = rnd.nextInt(40)
            item(
                i.toLong(),
                preset.name,
                *Array(colorCount) { colors.random(rnd) },
                pattern = patterns.random(rnd),
                favorite = rnd.nextInt(10) == 0,
                wearCount = worn,
                lastWorn = if (worn == 0) null else TODAY.minusDays(rnd.nextLong(0, 120)),
                price = if (rnd.nextBoolean()) 10.0 + rnd.nextInt(200) else null,
            )
        }
    }
}
