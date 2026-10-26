package com.kintukka.hanger.engine

import com.kintukka.hanger.engine.TestCloset.BROWN_BOOTS
import com.kintukka.hanger.engine.TestCloset.BLUE_SHIRT
import com.kintukka.hanger.engine.TestCloset.OLIVE_CHINOS
import com.kintukka.hanger.engine.TestCloset.SUNGLASSES
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import kotlin.math.ceil

class PackingPlannerTest {
    private val closet = TestCloset.sample()
    private val byId = TestCloset.byId(closet)
    private val start = LocalDate.of(2026, 10, 26)

    private val rainy = WeatherContext(12.0, 16.0, precipitationProbability = 80, condition = WeatherCondition.RAIN)
    private val sunny = WeatherContext(20.0, 27.0, condition = WeatherCondition.CLEAR, uvIndex = 6.0)
    private val hot = WeatherContext(24.0, 31.0, condition = WeatherCondition.CLEAR, uvIndex = 8.0)
    private val mild = WeatherContext(14.0, 20.0, condition = WeatherCondition.CLOUDY)

    private fun line(plan: PackingPlan, label: String) = plan.items.firstOrNull { it.label == label }

    private fun clothing(plan: PackingPlan) = plan.items.filter { it.category == PackCategory.CLOTHING }

    @Test
    fun fiveDayTripWithMixedWeather() {
        val trip = TripSpec(start, start.plusDays(4), weatherByDay = listOf(rainy, rainy, sunny, hot, mild))
        val plan = PackingPlanner.plan(closet, trip)

        assertEquals(5, plan.outfits.size)
        for (o in plan.outfits) assertTrue(o.toString(), TestCloset.isComplete(o, byId))
        assertTrue(plan.outfits.take(2).all { o -> o.any { byId.getValue(it).category == Category.OUTERWEAR } })

        // Each garment appears once, labelled with its subcategory.
        val clothes = clothing(plan)
        assertEquals(clothes.size, clothes.map { it.garmentId }.toSet().size)
        assertEquals(plan.outfits.flatten().toSet(), clothes.map { it.garmentId }.toSet())
        for (c in clothes) assertEquals(byId.getValue(c.garmentId!!).subcategory, c.label)

        // Capsule packing.
        val packed = clothes.map { byId.getValue(it.garmentId!!) }
        assertTrue(packed.count { it.category == Category.BOTTOM } <= ceil(5 / 3.0).toInt() + 1)
        assertTrue(packed.count { it.category == Category.SHOES } <= 2)

        // Essentials.
        assertEquals(6, line(plan, "Underwear")!!.quantity)
        assertEquals(6, line(plan, "Socks")!!.quantity)
        assertEquals(1, line(plan, "Sleepwear")!!.quantity)
        for (t in listOf("Toothbrush", "Toothpaste", "Deodorant", "Skincare", "Medication")) {
            assertEquals(PackCategory.TOILETRIES, line(plan, t)!!.category)
        }
        assertNotNull(line(plan, "Phone charger"))
        assertNotNull(line(plan, "Power bank"))
        assertNotNull(line(plan, "Travel adapter"))
        assertNotNull(line(plan, "ID / passport"))
        assertNotNull(line(plan, "Wallet & cards"))

        // Weather extras.
        val umbrella = line(plan, "Umbrella")!!
        assertEquals(PackCategory.WEATHER, umbrella.category)
        assertEquals("Rain expected on 2 days", umbrella.reason)
        assertEquals(SUNGLASSES, line(plan, "Sunglasses")!!.garmentId)
        assertNotNull(line(plan, "Sunscreen"))
        assertNotNull(line(plan, "Swimwear"))
        assertNull(line(plan, "Gloves"))
        assertNull(line(plan, "Warm hat"))

        assertTrue(plan.notes.toString(), plan.notes.first() == "5 outfits from ${clothes.size} pieces")
        assertTrue(plan.notes.toString(), "Rain on 2 days — umbrella added" in plan.notes)
    }

    @Test
    fun coldTripAddsGlovesAndHat() {
        val freezing = WeatherContext(-6.0, 1.0, condition = WeatherCondition.SNOW)
        val plan = PackingPlanner.plan(closet, TripSpec(start, start.plusDays(2), weatherByDay = List(3) { freezing }))
        assertNotNull(line(plan, "Gloves"))
        assertNotNull(line(plan, "Warm hat"))
        assertNotNull(line(plan, "Umbrella"))
        assertNull(line(plan, "Sunscreen"))
        assertNull(line(plan, "Swimwear"))
        assertTrue(plan.outfits.all { o -> o.any { byId.getValue(it).category == Category.OUTERWEAR } })
    }

    @Test
    fun longTripWithLaundryRewearsTops() {
        val days = 12
        val plan = PackingPlanner.plan(closet, TripSpec(start, start.plusDays(days - 1L), laundryAvailable = true))
        assertEquals(days, plan.outfits.size)
        assertEquals(8, line(plan, "Underwear")!!.quantity)
        assertEquals(2, line(plan, "Sleepwear")!!.quantity)
        val packed = clothing(plan).map { byId.getValue(it.garmentId!!) }
        assertTrue(packed.count { it.category == Category.TOP || it.category == Category.ONE_PIECE } <= 7)
        assertTrue(packed.count { it.category == Category.BOTTOM } <= ceil(days / 3.0).toInt() + 1)
        assertTrue(packed.count { it.category == Category.SHOES } <= 2)
        assertTrue(plan.notes.any { it.startsWith("Laundry on the way") })
        // No forecast: no weather extras.
        assertTrue(plan.items.none { it.category == PackCategory.WEATHER })

        val noLaundry = PackingPlanner.plan(closet, TripSpec(start, start.plusDays(days - 1L)))
        assertEquals(days + 1, line(noLaundry, "Underwear")!!.quantity)
    }

    @Test
    fun usesPlannedOutfitsFirst() {
        val planned = listOf(BLUE_SHIRT, OLIVE_CHINOS, BROWN_BOOTS)
        val plan = PackingPlanner.plan(closet, TripSpec(start, start.plusDays(2), plannedOutfits = listOf(planned)))
        assertEquals(3, plan.outfits.size)
        assertEquals(planned, plan.outfits.first())
        assertEquals("Day 1", clothing(plan).first { it.garmentId == BLUE_SHIRT }.reason)
    }

    @Test
    fun workAndActiveOccasionsGetTheRightShoes() {
        val plan = PackingPlanner.plan(
            closet,
            TripSpec(start, start.plusDays(5), occasions = setOf(Occasion.WORK, Occasion.ACTIVE)),
        )
        assertEquals(6, plan.outfits.size)
        val shoes = clothing(plan).map { byId.getValue(it.garmentId!!) }.filter { it.category == Category.SHOES }
        assertTrue(shoes.size <= 3)
        assertTrue("sporty shoes for active days: $shoes", shoes.any { it.sporty })
    }

    @Test
    fun singleDayAndDirtyItems() {
        val dirtyCloset = closet.map { if (it.category == Category.TOP) it.copy(status = GarmentStatus.DIRTY) else it }
        val plan = PackingPlanner.plan(dirtyCloset, TripSpec(start, start))
        assertEquals(1, plan.outfits.size)
        assertEquals(2, line(plan, "Underwear")!!.quantity)
        assertEquals("1 outfit from ${clothing(plan).size} pieces", plan.notes.first())

        val awayCloset = closet.map { if (it.category == Category.SHOES) it.copy(status = GarmentStatus.AWAY) else it }
        val none = PackingPlanner.plan(awayCloset, TripSpec(start, start.plusDays(1)))
        assertTrue(none.outfits.isEmpty())
        assertTrue(none.notes.any { it.startsWith("Couldn't plan outfits for 2 days") })
        assertNotNull(line(none, "Toothbrush"))
    }
}
