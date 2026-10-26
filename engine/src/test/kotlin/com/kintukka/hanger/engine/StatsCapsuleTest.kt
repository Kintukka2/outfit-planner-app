package com.kintukka.hanger.engine

import com.kintukka.hanger.engine.TestCloset.TODAY
import com.kintukka.hanger.engine.TestCloset.item
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class StatsCalculatorTest {
    private val coat = item(1, "Coat", FashionColor.CAMEL, price = 300.0, wearCount = 10, lastWorn = TODAY.minusDays(2))
    private val tee = item(2, "T-shirt", FashionColor.WHITE, price = 20.0, wearCount = 40, lastWorn = TODAY)
    private val jeans = item(3, "Jeans", FashionColor.DENIM, price = 100.0, wearCount = 4, lastWorn = TODAY.minusDays(70))
    private val dress = item(4, "Dress", FashionColor.NAVY, price = 150.0, wearCount = 0)
    private val sneakers = item(5, "Sneakers", FashionColor.WHITE, wearCount = 25, lastWorn = TODAY.minusDays(40))
    private val closet = listOf(coat, tee, jeans, dress, sneakers)

    private fun log(daysAgo: Long, vararg ids: Long) = WearRecord(TODAY.minusDays(daysAgo), ids.toList())

    private val wearLog = listOf(
        log(0, 2, 1),
        log(1, 2),
        log(2, 1),
        log(2, 2), // same day twice counts once
        // gap on day 3
        log(4, 2), log(5, 2), log(6, 2), log(7, 2), log(8, 2), // 5-day run
        log(40, 5, 2),
        log(70, 3),
        log(400, 3),
        WearRecord(TODAY.plusDays(3), listOf(4)), // planned, not worn yet
        WearRecord(TODAY.minusDays(10), emptyList()),
    )

    @Test
    fun costPerWear() {
        assertEquals(30.0, StatsCalculator.costPerWear(coat)!!, 1e-9)
        assertEquals(0.5, StatsCalculator.costPerWear(tee)!!, 1e-9)
        assertNull(StatsCalculator.costPerWear(dress)) // never worn
        assertNull(StatsCalculator.costPerWear(sneakers)) // no price
    }

    @Test
    fun totalsAndCostPerWearLists() {
        val s = StatsCalculator.compute(closet, wearLog, TODAY)
        assertEquals(5, s.totalItems)
        assertEquals(570.0, s.closetValue, 1e-9)
        assertEquals(4, s.itemsWithPrice)
        assertEquals((300.0 + 20.0 + 100.0) / (10 + 40 + 4), s.averageCostPerWear!!, 1e-9)
        assertEquals(listOf(2L, 3L, 1L), s.bestCostPerWear.map { it.garmentId }) // 0.5, 25, 30
        assertEquals(listOf(1L, 3L, 2L), s.worstCostPerWear.map { it.garmentId })
        assertEquals(0.5, s.bestCostPerWear.first().value, 1e-9)
    }

    @Test
    fun wearRankings() {
        val s = StatsCalculator.compute(closet, wearLog, TODAY)
        assertEquals(listOf(2L, 5L, 1L, 3L), s.mostWorn.map { it.garmentId })
        assertEquals(40.0, s.mostWorn.first().value, 0.0)
        assertEquals(listOf(3L, 1L, 5L, 2L), s.leastWorn.map { it.garmentId })
        assertEquals(listOf(4L), s.neverWorn)
        assertEquals(listOf(3L), s.forgotten) // worn 70 days ago; sneakers only 40
    }

    @Test
    fun utilisationUsesTheWearLog() {
        val s = StatsCalculator.compute(closet, wearLog, TODAY)
        assertEquals(2.0 / 5, s.utilisation30, 1e-9) // coat + tee
        assertEquals(4.0 / 5, s.utilisation90, 1e-9) // + sneakers (40d) + jeans (70d)
    }

    @Test
    fun streaksAndMonthlySeries() {
        val s = StatsCalculator.compute(closet, wearLog, TODAY)
        assertEquals(3, s.currentStreak) // today, yesterday, 2 days ago
        assertEquals(5, s.longestStreak)
        assertEquals(11, s.daysLogged)
        assertEquals(12, s.wearsPerMonth.size)
        assertEquals("2026-10", s.wearsPerMonth.last().first)
        assertEquals("2025-11", s.wearsPerMonth.first().first)
        // October 2026: days 0..8 ago minus the gap = 8 distinct dates.
        assertEquals(8, s.wearsPerMonth.last().second)
        assertEquals(1, s.wearsPerMonth.first { it.first == "2026-09" }.second) // 40 days ago = 16 Sep
        assertEquals(1, s.wearsPerMonth.first { it.first == "2026-08" }.second) // 70 days ago = 17 Aug
        assertEquals(10, s.wearsPerMonth.sumOf { it.second }) // 400 days ago falls outside the window

        // Not logged today yet: the streak still counts from yesterday.
        val noToday = StatsCalculator.compute(closet, wearLog.filter { it.date != TODAY }, TODAY)
        assertEquals(2, noToday.currentStreak)
        // Broken streak.
        val broken = StatsCalculator.compute(closet, wearLog.filter { it.date.isBefore(TODAY.minusDays(1)) }, TODAY)
        assertEquals(0, broken.currentStreak)
        assertEquals(5, broken.longestStreak)
    }

    @Test
    fun breakdowns() {
        val s = StatsCalculator.compute(closet, wearLog, TODAY)
        assertEquals(mapOf(Category.TOP to 1, Category.BOTTOM to 1, Category.ONE_PIECE to 1, Category.OUTERWEAR to 1, Category.SHOES to 1), s.byCategory)
        assertEquals(2, s.byColor[FashionColor.WHITE])
        assertEquals(FashionColor.WHITE, s.byColor.keys.first())
        assertEquals(1, s.byColor[FashionColor.CAMEL])
    }

    @Test
    fun emptyClosetIsSafe() {
        val s = StatsCalculator.compute(emptyList(), emptyList(), TODAY)
        assertEquals(0, s.totalItems)
        assertEquals(0.0, s.utilisation30, 0.0)
        assertNull(s.averageCostPerWear)
        assertEquals(0, s.currentStreak)
        assertEquals(0, s.longestStreak)
        assertEquals(12, s.wearsPerMonth.size)
        assertTrue(s.wearsPerMonth.all { it.second == 0 })
    }
}

class CapsuleAnalyzerTest {
    private val whiteTee = item(1, "T-shirt", FashionColor.WHITE)
    private val blackTee = item(2, "T-shirt", FashionColor.BLACK)
    private val jeans = item(3, "Jeans", FashionColor.DENIM)
    private val chinos = item(4, "Chinos", FashionColor.BEIGE)
    private val sneakers = item(5, "Sneakers", FashionColor.WHITE)
    private val dress = item(6, "Dress", FashionColor.NAVY)
    private val capsule = listOf(whiteTee, blackTee, jeans, chinos, sneakers)

    @Test
    fun countsNeutralCapsule() {
        assertEquals(4, CapsuleAnalyzer.outfitCount(capsule))
        assertEquals(5, CapsuleAnalyzer.outfitCount(capsule + dress))
        assertEquals(0, CapsuleAnalyzer.outfitCount(capsule - sneakers))
        assertEquals(0, CapsuleAnalyzer.outfitCount(emptyList()))
        // Duplicate ids are counted once.
        assertEquals(4, CapsuleAnalyzer.outfitCount(capsule + whiteTee))
    }

    @Test
    fun ignoresLaundryStatus() {
        val dirty = capsule.map { it.copy(status = GarmentStatus.DIRTY) }
        assertEquals(4, CapsuleAnalyzer.outfitCount(dirty))
    }

    @Test
    fun rejectsClashesAndFormalityGaps() {
        val loud = item(10, "T-shirt", FashionColor.RED, FashionColor.GREEN, FashionColor.PURPLE, FashionColor.YELLOW)
        assertEquals(4, CapsuleAnalyzer.outfitCount(capsule + loud))
        val hoodie = item(11, "Hoodie", FashionColor.GREY)
        val trousers = item(12, "Trousers", FashionColor.CHARCOAL)
        // hoodie + trousers is a 3-level formality gap; trousers work with the tees.
        assertEquals(4 + 2 + 2, CapsuleAnalyzer.outfitCount(capsule + hoodie + trousers))
    }

    @Test
    fun wishlistImpact() {
        val boots = item(20, "Chelsea boots", FashionColor.BROWN)
        val impact = CapsuleAnalyzer.wishlistImpact(capsule + dress, boots)
        assertEquals(CapsuleAnalyzer.outfitCount(capsule + dress + boots) - CapsuleAnalyzer.outfitCount(capsule + dress), impact)
        assertEquals(5, impact)
        assertEquals(2, CapsuleAnalyzer.wishlistImpact(capsule, item(21, "Shirt", FashionColor.LIGHT_BLUE)))
        assertEquals(0, CapsuleAnalyzer.wishlistImpact(capsule, item(22, "Watch", FashionColor.SILVER)))
        assertEquals(0, CapsuleAnalyzer.wishlistImpact(capsule, whiteTee)) // already owned
        assertEquals(1, CapsuleAnalyzer.wishlistImpact(capsule, item(23, "Jumpsuit", FashionColor.OLIVE)))
    }

    @Test
    fun wishlistImpactMatchesCountDifferenceOnLargeClosets() {
        val closet = TestCloset.synthetic(80, seed = 3)
        for (candidate in TestCloset.synthetic(12, seed = 4).map { it.copy(id = it.id + 10_000) }) {
            assertEquals(
                CapsuleAnalyzer.outfitCount(closet + candidate) - CapsuleAnalyzer.outfitCount(closet),
                CapsuleAnalyzer.wishlistImpact(closet, candidate),
            )
        }
    }

    @Test
    fun fastCountMatchesTheFullEvaluator() {
        val closet = TestCloset.synthetic(90, seed = 21) + capsule.map { it.copy(id = it.id + 5_000) }
        fun valid(items: List<Garment>): Boolean {
            val r = Evaluator.run(items, null, null, AffinityModel(), explain = false)
            return r.score >= 0.55 && r.formalitySpread <= 2
        }
        val by = closet.groupBy { it.category }
        var expected = 0
        for (t in by[Category.TOP].orEmpty()) for (b in by[Category.BOTTOM].orEmpty()) for (s in by[Category.SHOES].orEmpty()) {
            if (valid(listOf(t, b, s))) expected++
        }
        for (d in by[Category.ONE_PIECE].orEmpty()) for (s in by[Category.SHOES].orEmpty()) if (valid(listOf(d, s))) expected++
        assertTrue(expected > 0)
        assertEquals(expected, CapsuleAnalyzer.outfitCount(closet))
    }

    @Test
    fun outfitCountIsFastFor300Garments() {
        val big = TestCloset.synthetic(300)
        CapsuleAnalyzer.outfitCount(big) // warm-up
        val start = System.nanoTime()
        val count = CapsuleAnalyzer.outfitCount(big)
        val ms = (System.nanoTime() - start) / 1_000_000.0
        assertTrue(count > 0)
        assertTrue("outfitCount took ${ms}ms", ms < 1000.0)
    }
}
