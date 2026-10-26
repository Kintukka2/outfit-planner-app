package com.kintukka.hanger.demo

import androidx.room.withTransaction
import com.kintukka.hanger.data.OutfitLayout
import com.kintukka.hanger.data.db.DayEntryEntity
import com.kintukka.hanger.data.db.DayEntryItemEntity
import com.kintukka.hanger.data.db.HangerDatabase
import com.kintukka.hanger.data.db.ItemEntity
import com.kintukka.hanger.data.db.OutfitEntity
import com.kintukka.hanger.engine.Category
import com.kintukka.hanger.engine.FashionColor
import com.kintukka.hanger.engine.FashionColor.*
import com.kintukka.hanger.engine.GarmentStatus
import com.kintukka.hanger.engine.Occasion
import com.kintukka.hanger.engine.Pattern
import com.kintukka.hanger.engine.Season
import com.kintukka.hanger.images.ImageStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import kotlin.random.Random

/** A believable starter closet so every feature can be explored before adding real clothes. */
object DemoCloset {

    private data class Spec(
        val name: String,
        val category: Category,
        val sub: String,
        val colors: List<FashionColor>,
        val formality: Int,
        val warmth: Int,
        val price: Double? = null,
        val pattern: Pattern = Pattern.SOLID,
        val seasons: Set<Season> = emptySet(),
        val wash: Int = 1,
        val sporty: Boolean = false,
        val waterproof: Boolean = false,
        val brand: String = "",
        val favorite: Boolean = false,
    )

    private val specs = listOf(
        Spec("White tee", Category.TOP, "T-shirt", listOf(WHITE), 2, 2, 18.0, wash = 1, favorite = true, brand = "Uniqlo"),
        Spec("Black tee", Category.TOP, "T-shirt", listOf(BLACK), 2, 2, 18.0, wash = 1),
        Spec("Breton stripe top", Category.TOP, "Long-sleeve top", listOf(WHITE, NAVY), 2, 2, 45.0, Pattern.STRIPE, wash = 2, favorite = true, brand = "Armor Lux"),
        Spec("Oxford shirt", Category.TOP, "Shirt", listOf(LIGHT_BLUE), 3, 2, 55.0, wash = 2),
        Spec("Silk blouse", Category.TOP, "Blouse", listOf(CREAM), 4, 2, 89.0, wash = 2),
        Spec("Grey sweatshirt", Category.TOP, "Sweatshirt", listOf(GREY), 2, 3, 40.0, Pattern.TEXTURE, wash = 3),
        Spec("Running top", Category.TOP, "Sports top", listOf(TEAL), 1, 1, 25.0, sporty = true),
        Spec("Mid-wash jeans", Category.BOTTOM, "Jeans", listOf(DENIM), 2, 3, 80.0, wash = 5, favorite = true, brand = "Levi's"),
        Spec("Black jeans", Category.BOTTOM, "Jeans", listOf(BLACK), 2, 3, 75.0, wash = 5),
        Spec("Camel chinos", Category.BOTTOM, "Chinos", listOf(CAMEL), 3, 3, 60.0, wash = 3),
        Spec("Navy trousers", Category.BOTTOM, "Trousers", listOf(NAVY), 4, 3, 95.0, wash = 3),
        Spec("Olive shorts", Category.BOTTOM, "Shorts", listOf(OLIVE), 2, 1, 35.0, seasons = setOf(Season.SUMMER, Season.SPRING), wash = 2),
        Spec("Black midi skirt", Category.BOTTOM, "Skirt", listOf(BLACK), 3, 2, 50.0, wash = 2),
        Spec("Running leggings", Category.BOTTOM, "Leggings", listOf(CHARCOAL), 1, 2, 45.0, sporty = true),
        Spec("Green midi dress", Category.ONE_PIECE, "Dress", listOf(GREEN), 3, 2, 120.0, wash = 2),
        Spec("Floral sundress", Category.ONE_PIECE, "Dress", listOf(CREAM, RUST), 2, 1, 70.0, Pattern.FLORAL, setOf(Season.SUMMER), 2),
        Spec("Grey merino knit", Category.LAYER, "Knit sweater", listOf(GREY), 3, 4, 110.0, Pattern.TEXTURE, wash = 4, favorite = true),
        Spec("Rust knit", Category.LAYER, "Knit sweater", listOf(RUST), 2, 4, 65.0, Pattern.TEXTURE, setOf(Season.AUTUMN, Season.WINTER), 4),
        Spec("Cream cardigan", Category.LAYER, "Cardigan", listOf(CREAM), 3, 3, 70.0, wash = 4),
        Spec("Navy blazer", Category.LAYER, "Blazer", listOf(NAVY), 4, 3, 180.0, wash = 10),
        Spec("Camel wool coat", Category.OUTERWEAR, "Coat", listOf(CAMEL), 4, 5, 260.0, seasons = setOf(Season.AUTUMN, Season.WINTER), wash = 0, favorite = true),
        Spec("Beige trench", Category.OUTERWEAR, "Trench coat", listOf(BEIGE), 4, 3, 190.0, seasons = setOf(Season.SPRING, Season.AUTUMN), wash = 0, waterproof = true),
        Spec("Denim jacket", Category.OUTERWEAR, "Denim jacket", listOf(DENIM), 2, 3, 85.0, wash = 0),
        Spec("Black puffer", Category.OUTERWEAR, "Puffer jacket", listOf(BLACK), 2, 5, 150.0, seasons = setOf(Season.WINTER), wash = 0, waterproof = true),
        Spec("White sneakers", Category.SHOES, "Sneakers", listOf(WHITE), 2, 2, 95.0, wash = 0, favorite = true),
        Spec("Brown Chelsea boots", Category.SHOES, "Chelsea boots", listOf(BROWN), 3, 4, 170.0, seasons = setOf(Season.AUTUMN, Season.WINTER, Season.SPRING), wash = 0),
        Spec("Black loafers", Category.SHOES, "Loafers", listOf(BLACK), 4, 2, 120.0, wash = 0),
        Spec("Tan sandals", Category.SHOES, "Sandals", listOf(CAMEL), 2, 1, 55.0, seasons = setOf(Season.SUMMER), wash = 0),
        Spec("Running shoes", Category.SHOES, "Running shoes", listOf(GREY, ORANGE), 1, 2, 130.0, wash = 0, sporty = true),
        Spec("Black tote", Category.BAG, "Tote", listOf(BLACK), 3, 2, 140.0, wash = 0),
        Spec("Tan crossbody", Category.BAG, "Crossbody", listOf(CAMEL), 3, 2, 90.0, wash = 0),
        Spec("Burgundy scarf", Category.ACCESSORY, "Scarf", listOf(BURGUNDY), 3, 4, 40.0, seasons = setOf(Season.AUTUMN, Season.WINTER), wash = 0),
        Spec("Gold necklace", Category.ACCESSORY, "Necklace", listOf(GOLD), 3, 1, 60.0, wash = 0),
        Spec("Tortoise sunglasses", Category.ACCESSORY, "Sunglasses", listOf(BROWN), 2, 1, 120.0, seasons = setOf(Season.SUMMER, Season.SPRING), wash = 0),
        Spec("Charcoal beanie", Category.ACCESSORY, "Beanie", listOf(CHARCOAL), 1, 4, 20.0, seasons = setOf(Season.WINTER), wash = 0),
    )

    suspend fun load(db: HangerDatabase, images: ImageStore, today: LocalDate = LocalDate.now()) = withContext(Dispatchers.Default) {
        val random = Random(42)
        val entities = specs.mapIndexed { index, s ->
            val art = DemoArt.render(s.category, s.sub, s.colors, s.pattern)
            val file = images.saveCutout(art)
            ItemEntity(
                name = s.name,
                category = s.category,
                subcategory = s.sub,
                colors = s.colors,
                pattern = s.pattern,
                formality = s.formality,
                warmth = s.warmth,
                seasons = s.seasons,
                sporty = s.sporty,
                waterproof = s.waterproof,
                favorite = s.favorite,
                brand = s.brand,
                price = s.price,
                purchaseDate = today.minusDays(30L + random.nextInt(700)),
                imageFile = file,
                wearsBeforeWash = s.wash,
                createdAt = System.currentTimeMillis() - (specs.size - index) * 60_000L,
                isDemo = true,
            )
        }

        db.withTransaction {
            val ids = db.items().insertAll(entities)
            val byName = specs.map { it.name }.zip(ids).toMap()
            fun id(name: String) = byName.getValue(name)
            val catOf = specs.associate { byName.getValue(it.name) to it.category }

            val looks = listOf(
                Triple("Weekend classic", Occasion.CASUAL, listOf("Breton stripe top", "Mid-wash jeans", "White sneakers", "Tortoise sunglasses")),
                Triple("Office layers", Occasion.WORK, listOf("Oxford shirt", "Grey merino knit", "Navy trousers", "Black loafers", "Black tote")),
                Triple("Autumn camel", Occasion.SMART, listOf("Black tee", "Camel wool coat", "Black jeans", "Brown Chelsea boots", "Burgundy scarf")),
                Triple("Dinner in green", Occasion.DATE, listOf("Green midi dress", "Black loafers", "Gold necklace", "Tan crossbody")),
                Triple("Rainy day errands", Occasion.CASUAL, listOf("Grey sweatshirt", "Beige trench", "Black jeans", "White sneakers")),
                Triple("Morning run", Occasion.ACTIVE, listOf("Running top", "Running leggings", "Running shoes")),
            )
            val outfitIds = looks.map { (name, occasion, pieces) ->
                val outfitId = db.outfits().insert(OutfitEntity(name = name, occasion = occasion, isDemo = true, favorite = name == "Weekend classic"))
                val placements = OutfitLayout.arrange(pieces.map { id(it) to catOf.getValue(id(it)) })
                db.outfits().insertPlacements(placements.mapIndexed { i, p -> p.copy(z = i).toEntity(outfitId) })
                outfitId to pieces.map(::id)
            }

            // Six weeks of history: most days logged, a few skipped, favouring some pieces.
            for (daysAgo in 42 downTo 1) {
                if (random.nextInt(100) < 22) continue
                val (outfitId, pieces) = outfitIds[random.nextInt(outfitIds.size - 1)]
                val reuse = random.nextInt(100) < 55
                val entryId = db.days().insert(
                    DayEntryEntity(date = today.minusDays(daysAgo.toLong()), outfitId = if (reuse) outfitId else null, worn = true, isDemo = true),
                )
                val itemsForDay = if (reuse) pieces else randomLook(random, byName)
                db.days().insertItems(itemsForDay.map { DayEntryItemEntity(entryId, it) })
            }
            // Something planned for tomorrow.
            val (planned, plannedItems) = outfitIds[1]
            val plannedId = db.days().insert(DayEntryEntity(date = today.plusDays(1), outfitId = planned, isDemo = true))
            db.days().insertItems(plannedItems.map { DayEntryItemEntity(plannedId, it) })

            // A couple of things waiting for laundry day.
            val dirty = listOf("Black tee", "Grey sweatshirt").map(::id)
            db.items().getMany(dirty).forEach {
                db.items().update(it.copy(status = GarmentStatus.DIRTY, wearsSinceWash = it.wearsBeforeWash))
            }
        }
    }

    private fun randomLook(random: Random, byName: Map<String, Long>): List<Long> {
        val tops = listOf("White tee", "Black tee", "Breton stripe top", "Oxford shirt", "Grey sweatshirt")
        val bottoms = listOf("Mid-wash jeans", "Black jeans", "Camel chinos", "Black midi skirt")
        val shoes = listOf("White sneakers", "Brown Chelsea boots", "Black loafers")
        return listOf(tops.random(random), bottoms.random(random), shoes.random(random)).map(byName::getValue)
    }
}
