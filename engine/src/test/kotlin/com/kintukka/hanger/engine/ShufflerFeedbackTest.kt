package com.kintukka.hanger.engine

import com.kintukka.hanger.engine.TestCloset.BLUE_SHIRT
import com.kintukka.hanger.engine.TestCloset.JEANS
import com.kintukka.hanger.engine.TestCloset.KHAKI_SHORTS
import com.kintukka.hanger.engine.TestCloset.SANDALS
import com.kintukka.hanger.engine.TestCloset.WHITE_TANK
import com.kintukka.hanger.engine.TestCloset.item
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ShufflerTest {
    private val closet = TestCloset.sample()
    private val byId = TestCloset.byId(closet)
    private val rows = listOf(Category.TOP, Category.BOTTOM, Category.SHOES)

    @Test
    fun fillsEveryRowInOrder() {
        val result = Shuffler.shuffle(closet, ShuffleRequest(rows))
        assertEquals(rows, result.keys.toList())
        for ((cat, id) in result) assertEquals(cat, byId.getValue(id).category)
    }

    @Test
    fun keepsLockedPicks() {
        for (seed in 0L..20L) {
            for (smart in listOf(true, false)) {
                val r = Shuffler.shuffle(closet, ShuffleRequest(rows, locked = mapOf(Category.TOP to BLUE_SHIRT), smart = smart, seed = seed))
                assertEquals(BLUE_SHIRT, r[Category.TOP])
                assertEquals(3, r.size)
            }
        }
        // A locked pick is kept even if it is in the laundry.
        val dirty = closet.map { if (it.id == JEANS) it.copy(status = GarmentStatus.DIRTY) else it }
        assertEquals(JEANS, Shuffler.shuffle(dirty, ShuffleRequest(rows, locked = mapOf(Category.BOTTOM to JEANS)))[Category.BOTTOM])
        // A stale lock (deleted garment) is ignored and the row is filled.
        val stale = Shuffler.shuffle(closet, ShuffleRequest(rows, locked = mapOf(Category.TOP to 9_999L)))
        assertEquals(Category.TOP, byId.getValue(stale.getValue(Category.TOP)).category)
    }

    @Test
    fun deterministicPerSeed() {
        for (smart in listOf(true, false)) {
            val a = Shuffler.shuffle(closet, ShuffleRequest(rows, smart = smart, seed = 7))
            val b = Shuffler.shuffle(closet, ShuffleRequest(rows, smart = smart, seed = 7))
            assertEquals(a, b)
            val variety = (0L..19L).map { Shuffler.shuffle(closet, ShuffleRequest(rows, smart = smart, seed = it)) }.toSet()
            assertTrue("smart=$smart variety ${variety.size}", variety.size >= 5)
        }
    }

    @Test
    fun omitsRowsWithoutEligibleGarments() {
        val noDresses = closet.filter { it.category != Category.ONE_PIECE }
        val result = Shuffler.shuffle(noDresses, ShuffleRequest(listOf(Category.ONE_PIECE, Category.SHOES, Category.OTHER)))
        assertEquals(listOf(Category.SHOES), result.keys.toList())
        assertTrue(Shuffler.shuffle(emptyList(), ShuffleRequest(rows)).isEmpty())
    }

    @Test
    fun respectsCleanAndSeasonFilters() {
        val dirtyTops = closet.map { if (it.category == Category.TOP && it.id != BLUE_SHIRT) it.copy(status = GarmentStatus.DIRTY) else it }
        for (seed in 0L..20L) {
            assertEquals(BLUE_SHIRT, Shuffler.shuffle(dirtyTops, ShuffleRequest(rows, seed = seed))[Category.TOP])
            val winter = Shuffler.shuffle(closet, ShuffleRequest(rows, season = Season.WINTER, smart = false, seed = seed))
            assertFalse(winter.values.any { it in setOf(WHITE_TANK, KHAKI_SHORTS, SANDALS) })
        }
        val anyDirty = (0L..30L).map { Shuffler.shuffle(dirtyTops, ShuffleRequest(rows, onlyClean = false, smart = false, seed = it)) }
        assertTrue(anyDirty.any { it[Category.TOP] != BLUE_SHIRT })
    }

    @Test
    fun smartModePicksBetterCombinations() {
        fun avgScore(smart: Boolean) = (0L..29L).map { seed ->
            val r = Shuffler.shuffle(closet, ShuffleRequest(rows, smart = smart, seed = seed))
            OutfitEvaluator.evaluate(r.values.map(byId::getValue)).score
        }.average()
        val smart = avgScore(true)
        val random = avgScore(false)
        assertTrue("smart $smart vs random $random", smart > random)
        assertTrue(smart > 0.8)
    }

    @Test
    fun favourLeastWornBiasesTowardsRarePieces() {
        val tops = listOf(
            item(1, "T-shirt", FashionColor.WHITE, wearCount = 0),
            item(2, "T-shirt", FashionColor.BLACK, wearCount = 60),
        )
        val picks = (0L..399L).map { Shuffler.shuffle(tops, ShuffleRequest(listOf(Category.TOP), smart = false, favourLeastWorn = true, seed = it)) }
        val rare = picks.count { it[Category.TOP] == 1L }
        assertTrue("rare picked $rare/400", rare > 300)
        val unbiased = (0L..399L).count {
            Shuffler.shuffle(tops, ShuffleRequest(listOf(Category.TOP), smart = false, seed = it))[Category.TOP] == 1L
        }
        assertTrue("unbiased $unbiased/400", unbiased in 140..260)
    }
}

class FeedbackLearnerTest {

    @Test
    fun appliesDeltasToPairsAndItems() {
        val m = FeedbackLearner.apply(AffinityModel(), FeedbackEvent(FeedbackType.WORN, listOf(1, 2, 3)))
        assertEquals(3, m.pairScores.size)
        assertEquals(0.30, m.pair(1, 2), 1e-9)
        assertEquals(0.30, m.pair(3, 1), 1e-9)
        assertEquals(0.15, m.item(2), 1e-9)

        val saved = FeedbackLearner.apply(m, FeedbackEvent(FeedbackType.SAVED, listOf(1, 2)))
        assertEquals(0.50, saved.pair(1, 2), 1e-9)
        assertEquals(0.30, saved.pair(2, 3), 1e-9)
        assertEquals(0.25, saved.item(1), 1e-9)

        val liked = FeedbackLearner.apply(AffinityModel(), FeedbackEvent(FeedbackType.LIKED, listOf(4, 5)))
        assertEquals(0.15, liked.pair(4, 5), 1e-9)
        assertEquals(0.075, liked.item(5), 1e-9)

        val rejected = FeedbackLearner.apply(AffinityModel(), FeedbackEvent(FeedbackType.REJECTED, listOf(4, 5)))
        assertEquals(-0.40, rejected.pair(4, 5), 1e-9)
        assertEquals(-0.20, rejected.item(4), 1e-9)
    }

    @Test
    fun clampsScores() {
        var m = AffinityModel()
        repeat(12) { m = FeedbackLearner.apply(m, FeedbackEvent(FeedbackType.REJECTED, listOf(1, 2))) }
        assertEquals(-2.0, m.pair(1, 2), 1e-9)
        assertEquals(-2.0, m.item(1), 1e-9)
        repeat(30) { m = FeedbackLearner.apply(m, FeedbackEvent(FeedbackType.WORN, listOf(1, 2))) }
        assertEquals(2.0, m.pair(1, 2), 1e-9)
        assertEquals(2.0, m.item(2), 1e-9)
    }

    @Test
    fun isImmutableAndIgnoresDuplicates() {
        val original = AffinityModel()
        val updated = FeedbackLearner.apply(original, FeedbackEvent(FeedbackType.WORN, listOf(7, 7, 8)))
        assertTrue(original.pairScores.isEmpty())
        assertEquals(setOf(AffinityModel.pairKey(7, 8)), updated.pairScores.keys)
        assertEquals(0.15, updated.item(7), 1e-9)
        assertEquals(updated, FeedbackLearner.apply(updated, FeedbackEvent(FeedbackType.LIKED, emptyList())))
    }
}
