package com.kintukka.hanger.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class SeasonsWeatherTest {

    @Test
    fun meteorologicalSeasonsNorth() {
        assertEquals(Season.WINTER, Seasons.forDate(LocalDate.of(2026, 1, 15)))
        assertEquals(Season.WINTER, Seasons.forDate(LocalDate.of(2026, 2, 28)))
        assertEquals(Season.SPRING, Seasons.forDate(LocalDate.of(2026, 3, 1)))
        assertEquals(Season.SPRING, Seasons.forDate(LocalDate.of(2026, 5, 31)))
        assertEquals(Season.SUMMER, Seasons.forDate(LocalDate.of(2026, 6, 1)))
        assertEquals(Season.SUMMER, Seasons.forDate(LocalDate.of(2026, 8, 31)))
        assertEquals(Season.AUTUMN, Seasons.forDate(LocalDate.of(2026, 9, 1)))
        assertEquals(Season.AUTUMN, Seasons.forDate(LocalDate.of(2026, 11, 30)))
        assertEquals(Season.WINTER, Seasons.forDate(LocalDate.of(2026, 12, 1)))
    }

    @Test
    fun meteorologicalSeasonsSouth() {
        val s = Hemisphere.SOUTH
        assertEquals(Season.SUMMER, Seasons.forDate(LocalDate.of(2026, 1, 15), s))
        assertEquals(Season.AUTUMN, Seasons.forDate(LocalDate.of(2026, 4, 10), s))
        assertEquals(Season.WINTER, Seasons.forDate(LocalDate.of(2026, 7, 4), s))
        assertEquals(Season.SPRING, Seasons.forDate(LocalDate.of(2026, 10, 26), s))
        assertEquals(Season.SUMMER, Seasons.forDate(LocalDate.of(2026, 12, 25), s))
    }

    private fun needs(min: Double, max: Double, feels: Double? = null, precip: Int = 0, wind: Double = 0.0,
                      condition: WeatherCondition = WeatherCondition.CLOUDY) =
        WeatherAdvisor.needs(WeatherContext(min, max, feels, precip, wind, condition))

    @Test
    fun hotBand() {
        val n = needs(25.0, 33.0) // effective 28.2
        assertEquals(1.3, n.targetWarmth, 0.01)
        assertTrue(n.allowsShortsAndOpenShoes)
        assertFalse(n.needsOuterwear)
        assertFalse(n.suggestsLayer)
    }

    @Test
    fun warmBand() {
        val n = needs(20.0, 28.0) // 23.2
        assertEquals(1.8, n.targetWarmth, 0.01)
        assertTrue(n.allowsShortsAndOpenShoes)
        assertFalse(n.needsOuterwear)
    }

    @Test
    fun mildBandSuggestsLayerOnlyWhenMorningsAreCool() {
        val settled = needs(16.0, 22.0) // 18.4, min 16
        assertEquals(2.4, settled.targetWarmth, 0.01)
        assertFalse(settled.suggestsLayer)
        assertFalse(settled.allowsShortsAndOpenShoes)
        val coolMorning = needs(12.0, 27.0) // 18.0, min 12
        assertEquals(2.4, coolMorning.targetWarmth, 0.01)
        assertTrue(coolMorning.suggestsLayer)
    }

    @Test
    fun coolChillyColdFreezingBands() {
        val cool = needs(10.0, 18.0) // 13.2
        assertEquals(3.0, cool.targetWarmth, 0.01)
        assertTrue(cool.suggestsLayer)
        assertFalse(cool.needsOuterwear)

        val chilly = needs(5.0, 12.0) // 7.8
        assertEquals(3.6, chilly.targetWarmth, 0.01)
        assertTrue(chilly.needsOuterwear)
        assertFalse(chilly.allowsShortsAndOpenShoes)

        val cold = needs(0.0, 6.0) // 2.4
        assertEquals(4.2, cold.targetWarmth, 0.01)
        assertTrue(cold.needsOuterwear)
        assertTrue(cold.suggestsLayer)

        val freezing = needs(-8.0, -2.0)
        assertEquals(4.7, freezing.targetWarmth, 0.01)
        assertTrue(freezing.needsOuterwear)
    }

    @Test
    fun effectiveTemperatureLeansTowardsTheColdPartOfTheDay() {
        // Plain average would be 17 (mild); weighting to the minimum makes it cool.
        assertEquals(3.0, needs(12.0, 22.0).targetWarmth, 0.01)
    }

    @Test
    fun feelsLikeOverridesTheRange() {
        val n = needs(10.0, 14.0, feels = 30.0)
        assertEquals(1.3, n.targetWarmth, 0.01)
        assertTrue(n.allowsShortsAndOpenShoes)
    }

    @Test
    fun windNudgesWarmth() {
        assertEquals(3.3, needs(10.0, 18.0, wind = 35.0).targetWarmth, 0.01)
        assertEquals(3.0, needs(10.0, 18.0, wind = 29.0).targetWarmth, 0.01)
    }

    @Test
    fun waterproofRules() {
        assertTrue(needs(10.0, 15.0, precip = 60).wantsWaterproof)
        assertTrue(needs(10.0, 15.0, precip = 50).wantsWaterproof)
        assertFalse(needs(10.0, 15.0, precip = 40).wantsWaterproof)
        assertTrue(needs(10.0, 15.0, precip = 10, condition = WeatherCondition.RAIN).wantsWaterproof)
        assertTrue(needs(-2.0, 1.0, condition = WeatherCondition.SNOW).wantsWaterproof)
        assertTrue(needs(10.0, 15.0, condition = WeatherCondition.STORM).wantsWaterproof)
        assertFalse(needs(10.0, 15.0, precip = 30, condition = WeatherCondition.DRIZZLE).wantsWaterproof)
        assertTrue(needs(10.0, 15.0, precip = 45, condition = WeatherCondition.DRIZZLE).wantsWaterproof)
    }

    @Test
    fun friendlySummary() {
        assertEquals(
            "Mild with showers likely — bring a light layer and something waterproof.",
            needs(14.0, 24.0, precip = 70).summary,
        )
        assertEquals("Cold — wear a warm coat.", needs(0.0, 6.0).summary)
        assertTrue(needs(25.0, 33.0, condition = WeatherCondition.CLEAR).summary.startsWith("Hot and sunny"))
        assertTrue(needs(-8.0, -2.0, condition = WeatherCondition.SNOW).summary.contains("snow"))
    }
}
