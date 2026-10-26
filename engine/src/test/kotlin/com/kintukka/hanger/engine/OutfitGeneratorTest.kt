package com.kintukka.hanger.engine

import com.kintukka.hanger.engine.TestCloset.BEIGE_TRENCH
import com.kintukka.hanger.engine.TestCloset.BLACK_PUFFER
import com.kintukka.hanger.engine.TestCloset.BURGUNDY_SCARF
import com.kintukka.hanger.engine.TestCloset.CAMEL_COAT
import com.kintukka.hanger.engine.TestCloset.GREY_HOODIE
import com.kintukka.hanger.engine.TestCloset.JEANS
import com.kintukka.hanger.engine.TestCloset.KHAKI_SHORTS
import com.kintukka.hanger.engine.TestCloset.LEGGINGS
import com.kintukka.hanger.engine.TestCloset.NAVY_DRESS
import com.kintukka.hanger.engine.TestCloset.OLIVE_CHINOS
import com.kintukka.hanger.engine.TestCloset.RUNNING_SHOES
import com.kintukka.hanger.engine.TestCloset.SANDALS
import com.kintukka.hanger.engine.TestCloset.SPORTS_TOP
import com.kintukka.hanger.engine.TestCloset.TODAY
import com.kintukka.hanger.engine.TestCloset.WHITE_SNEAKERS
import com.kintukka.hanger.engine.TestCloset.WHITE_TANK
import com.kintukka.hanger.engine.TestCloset.WHITE_TEE
import com.kintukka.hanger.engine.TestCloset.item
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class OutfitGeneratorTest {
    private val closet = TestCloset.sample()
    private val byId = TestCloset.byId(closet)

    private fun suggest(
        date: LocalDate = TODAY,
        occasion: Occasion = Occasion.CASUAL,
        weather: WeatherContext? = null,
        count: Int = 3,
        seed: Long = 0,
        anchors: Set<Long> = emptySet(),
        exclude: Set<Long> = emptySet(),
        recent: Set<String> = emptySet(),
        accessories: Boolean = true,
        garments: List<Garment> = closet,
        affinity: AffinityModel = AffinityModel(),
    ) = OutfitGenerator.suggest(
        garments,
        StylingRequest(date, occasion, weather, Hemisphere.NORTH, anchors, exclude, count, recent, accessories, seed),
        affinity,
    )

    private fun cats(o: SuggestedOutfit) = TestCloset.categories(o.itemIds, byId)

    @Test
    fun returnsCompleteOrderedOutfitsWithReasons() {
        val outfits = suggest()
        assertEquals(3, outfits.size)
        for (o in outfits) {
            assertTrue(o.itemIds.toString(), TestCloset.isComplete(o.itemIds, byId))
            val orders = cats(o).map { it.order }
            assertEquals("ordered by Category.order", orders.sorted(), orders)
            assertEquals(o.itemIds.size, o.itemIds.toSet().size)
            assertTrue(o.reasons.toString(), o.reasons.size in 2..4)
            assertTrue(o.reasons.all { it.isNotBlank() })
            assertTrue(o.score in 0.0..1.0)
            assertTrue("score ${o.score}", o.score > 0.6)
        }
        assertEquals(outfits.map { it.signature }.toSet().size, outfits.size)
    }

    @Test
    fun coatIsAddedAt4Degrees() {
        val cold = WeatherContext(tempMinC = 1.0, tempMaxC = 7.0, condition = WeatherCondition.CLOUDY) // effective ≈ 3.4°C
        for (seed in 0L..4L) {
            val outfits = suggest(weather = cold, seed = seed)
            assertEquals(3, outfits.size)
            for (o in outfits) {
                val c = cats(o)
                assertTrue("outerwear in $c", Category.OUTERWEAR in c)
                assertTrue("warm layer in $c", Category.LAYER in c)
                assertFalse(o.itemIds.contains(SANDALS))
                assertFalse(o.itemIds.contains(KHAKI_SHORTS))
                val eval = OutfitEvaluator.evaluate(o.itemIds.map(byId::getValue), cold, Occasion.CASUAL)
                assertTrue(eval.warnings.toString(), eval.warnings.none { it.contains("coat or jacket") })
            }
        }
    }

    @Test
    fun noShortsOrSandalsAt8DegreesEvenInSummer() {
        val july = LocalDate.of(2026, 7, 10) // shorts, sandals and tanks are in season
        val chilly = WeatherContext(tempMinC = 6.0, tempMaxC = 11.0) // effective 8°C
        for (seed in 0L..5L) {
            for (o in suggest(date = july, weather = chilly, seed = seed)) {
                assertFalse(o.itemIds.toString(), o.itemIds.contains(KHAKI_SHORTS))
                assertFalse(o.itemIds.toString(), o.itemIds.contains(SANDALS))
                assertFalse(o.itemIds.toString(), o.itemIds.contains(WHITE_TANK))
                assertTrue(Category.OUTERWEAR in cats(o))
            }
        }
    }

    @Test
    fun hotDaysGetLightPiecesAndNoCoats() {
        val july = LocalDate.of(2026, 7, 10)
        val hot = WeatherContext(tempMinC = 24.0, tempMaxC = 33.0, condition = WeatherCondition.CLEAR, uvIndex = 8.0)
        val outfits = suggest(date = july, weather = hot)
        val all = outfits.flatMap { it.itemIds }
        assertTrue(outfits.isNotEmpty())
        assertTrue(outfits.none { Category.OUTERWEAR in cats(it) || Category.LAYER in cats(it) })
        assertFalse(all.contains(BLACK_PUFFER))
        assertFalse(all.contains(BURGUNDY_SCARF))
        val light = setOf(KHAKI_SHORTS, SANDALS, WHITE_TANK, TestCloset.RUST_MAXI, TestCloset.BLACK_SKIRT, TestCloset.FLORAL_SKIRT)
        assertTrue("some summer piece in $all", all.any { it in light })
    }

    @Test
    fun rainPrefersWaterproofOuterwear() {
        val wet = WeatherContext(tempMinC = 7.0, tempMaxC = 12.0, precipitationProbability = 90, condition = WeatherCondition.RAIN)
        val outfits = suggest(weather = wet)
        assertTrue(outfits.any { BEIGE_TRENCH in it.itemIds })
    }

    @Test
    fun workAvoidsHoodiesAndSportswear() {
        for (seed in 0L..4L) {
            for (o in suggest(occasion = Occasion.WORK, seed = seed)) {
                for (id in listOf(GREY_HOODIE, SPORTS_TOP, LEGGINGS, RUNNING_SHOES)) {
                    assertFalse("$id in ${o.itemIds}", id in o.itemIds)
                }
                assertTrue(o.itemIds.map(byId::getValue).filter { it.category in TestCloset.CORE }.all { it.formality >= 2 })
            }
        }
    }

    @Test
    fun activeDaysPreferSportyPieces() {
        val o = suggest(occasion = Occasion.ACTIVE, count = 1).single()
        assertTrue(o.itemIds.toString(), RUNNING_SHOES in o.itemIds)
        assertTrue(o.itemIds.toString(), LEGGINGS in o.itemIds || SPORTS_TOP in o.itemIds)
    }

    @Test
    fun excludesDirtyAwayAndExcludedItems() {
        val modified = closet.map {
            when (it.id) {
                JEANS, WHITE_SNEAKERS -> it.copy(status = GarmentStatus.DIRTY)
                OLIVE_CHINOS -> it.copy(status = GarmentStatus.AWAY)
                else -> it
            }
        }
        for (seed in 0L..5L) {
            val outfits = suggest(garments = modified, seed = seed, exclude = setOf(WHITE_TEE), count = 4)
            val used = outfits.flatMap { it.itemIds }
            for (id in listOf(JEANS, WHITE_SNEAKERS, OLIVE_CHINOS, WHITE_TEE)) assertFalse("$id used", id in used)
        }
    }

    @Test
    fun outfitsAreDiverse() {
        for (seed in 0L..5L) {
            val outfits = suggest(seed = seed, count = 3)
            for (i in outfits.indices) for (j in i + 1 until outfits.size) {
                val shared = TestCloset.coreIds(outfits[i], byId).intersect(TestCloset.coreIds(outfits[j], byId))
                assertTrue("seed $seed shares $shared", shared.size <= 1)
            }
        }
    }

    @Test
    fun deterministicPerSeedAndVariesAcrossSeeds() {
        assertEquals(suggest(seed = 42), suggest(seed = 42))
        val sets = (0L..9L).map { seed -> suggest(seed = seed).map { it.signature } }.toSet()
        assertTrue("different seeds should explore: ${sets.size}", sets.size >= 3)
    }

    @Test
    fun anchorsAreRespected() {
        for (o in suggest(anchors = setOf(OLIVE_CHINOS))) assertTrue(OLIVE_CHINOS in o.itemIds)
        for (o in suggest(anchors = setOf(CAMEL_COAT))) assertTrue(CAMEL_COAT in o.itemIds)
        for (o in suggest(anchors = setOf(NAVY_DRESS))) {
            assertTrue(NAVY_DRESS in o.itemIds)
            assertFalse(Category.BOTTOM in cats(o))
        }
        val styled = suggest(anchors = setOf(OLIVE_CHINOS, WHITE_SNEAKERS, BURGUNDY_SCARF))
        assertTrue(styled.isNotEmpty())
        for (o in styled) assertTrue(o.itemIds.containsAll(listOf(OLIVE_CHINOS, WHITE_SNEAKERS, BURGUNDY_SCARF)))
    }

    @Test
    fun anchorsOverrideLaundryStatus() {
        val dirtyChinos = closet.map { if (it.id == OLIVE_CHINOS) it.copy(status = GarmentStatus.DIRTY) else it }
        val outfits = suggest(garments = dirtyChinos, anchors = setOf(OLIVE_CHINOS))
        assertTrue(outfits.isNotEmpty())
        assertTrue(outfits.all { OLIVE_CHINOS in it.itemIds })
    }

    @Test
    fun recentSignaturesAreAvoided() {
        val first = suggest(count = 1).single()
        val again = suggest(count = 1, recent = setOf(first.signature)).single()
        assertNotEquals(first.signature, again.signature)
        // A recent outfit with a different bag is still the same look.
        val coreOnly = first.itemIds.filter { byId.getValue(it).category in TestCloset.CORE }
        val third = suggest(count = 1, recent = setOf(OutfitSignature.of(coreOnly))).single()
        assertNotEquals(TestCloset.coreIds(first, byId), TestCloset.coreIds(third, byId))
    }

    @Test
    fun neglectedPiecesGetAStory() {
        // The most neglected piece leads the story; keep the heels (6 weeks) out so the chinos (5 weeks) win.
        val o = suggest(anchors = setOf(OLIVE_CHINOS), exclude = setOf(TestCloset.BLACK_HEELS), count = 1).single()
        assertTrue(o.reasons.toString(), "You haven't worn the olive chinos in 5 weeks" in o.reasons)
    }

    @Test
    fun recentlyWornPiecesArePenalised() {
        // Jeans were worn yesterday; with plenty of alternatives they should rarely lead.
        val usage = (0L..9L).sumOf { seed -> suggest(seed = seed).count { JEANS in it.itemIds } }
        assertTrue("jeans used $usage times", usage <= 10)
    }

    @Test
    fun accessoriesAreOptional() {
        val without = suggest(accessories = false)
        assertTrue(without.all { o -> cats(o).none { it == Category.BAG || it == Category.ACCESSORY } })
        val with = suggest(accessories = true)
        assertTrue(with.any { o -> cats(o).any { it == Category.BAG || it == Category.ACCESSORY } })
    }

    @Test
    fun scarfOnlyWhenItIsCold() {
        val cold = WeatherContext(tempMinC = -1.0, tempMaxC = 5.0)
        val coldOutfits = suggest(weather = cold)
        assertTrue(coldOutfits.map { it.itemIds }.toString(), coldOutfits.any { BURGUNDY_SCARF in it.itemIds })
        // Out of season in summer, and too warm on a mild autumn day.
        val mild = WeatherContext(tempMinC = 18.0, tempMaxC = 25.0)
        val mildOutfits = suggest(weather = mild)
        assertTrue(mildOutfits.map { it.itemIds }.toString(), mildOutfits.none { BURGUNDY_SCARF in it.itemIds })
    }

    @Test
    fun learnedAffinityShiftsSuggestions() {
        val base = suggest(count = 1).single()
        var model = AffinityModel()
        repeat(5) { model = FeedbackLearner.apply(model, FeedbackEvent(FeedbackType.REJECTED, base.itemIds)) }
        val after = suggest(count = 1, affinity = model).single()
        assertNotEquals(base.signature, after.signature)
    }

    @Test
    fun gracefullyReturnsFewerOrNone() {
        assertTrue(suggest(garments = emptyList()).isEmpty())
        val topsOnly = closet.filter { it.category == Category.TOP }
        assertTrue(suggest(garments = topsOnly).isEmpty())
        val noShoes = closet.filter { it.category != Category.SHOES }
        assertTrue(suggest(garments = noShoes).isEmpty())

        val tiny = listOf(
            item(1, "T-shirt", FashionColor.WHITE),
            item(2, "Jeans", FashionColor.DENIM),
            item(3, "Sneakers", FashionColor.WHITE),
        )
        val outfits = suggest(garments = tiny, count = 5)
        assertEquals(1, outfits.size)
        assertEquals(listOf(1L, 2L, 3L), outfits.single().itemIds)
        assertTrue(suggest(count = 0).isEmpty())
    }

    @Test
    fun seasonFilterRelaxesWhenTooFewItems() {
        // Only summer tops in winter: still get an outfit rather than nothing.
        val winter = LocalDate.of(2026, 1, 15)
        val garments = listOf(
            item(1, "Tank top", FashionColor.WHITE),
            item(2, "Jeans", FashionColor.DENIM),
            item(3, "Trousers", FashionColor.BLACK),
            item(4, "Sneakers", FashionColor.WHITE),
            item(5, "Boots", FashionColor.BROWN),
        )
        assertTrue(suggest(date = winter, garments = garments).isNotEmpty())
        // With in-season alternatives the out-of-season piece is skipped.
        val more = garments + item(6, "Sweatshirt", FashionColor.GREY) + item(7, "Long-sleeve top", FashionColor.NAVY)
        assertTrue(suggest(date = winter, garments = more).none { 1L in it.itemIds })
    }

    @Test
    fun suggestIsFastFor300Garments() {
        val big = TestCloset.synthetic(300)
        val request = StylingRequest(
            date = TODAY, occasion = Occasion.CASUAL,
            weather = WeatherContext(5.0, 12.0, precipitationProbability = 60), count = 3, seed = 1,
        )
        OutfitGenerator.suggest(big, request.copy(seed = 99)) // warm-up
        val start = System.nanoTime()
        val outfits = OutfitGenerator.suggest(big, request)
        val ms = (System.nanoTime() - start) / 1_000_000.0
        assertEquals(3, outfits.size)
        assertTrue("suggest took ${ms}ms", ms < 1000.0)
    }
}
