package com.kintukka.hanger.engine

import java.time.temporal.ChronoUnit
import kotlin.math.ceil

/**
 * Capsule packing: planned outfits first, then generated outfits per day that strongly re-use
 * bottoms, shoes and layers already in the bag.
 */
internal object Packing {
    /** Used when a day has no forecast: mild, dry. */
    private val DEFAULT_WEATHER = WeatherContext(tempMinC = 15.0, tempMaxC = 22.0)

    // Caps (below) guarantee the capsule; boosts nudge towards re-use before a cap is reached.
    // Shoes get a smaller nudge so the second pair can be one that suits a different occasion.
    private const val REUSE_BOOST = 0.2
    private const val SHOE_REUSE_BOOST = 0.1
    private const val TOP_REWEAR_BOOST = 0.3
    private const val TOP_REPEAT_PENALTY = -0.15
    private const val LAYER_CAP = 2
    private const val OUTER_CAP = 1

    fun plan(closet: List<Garment>, trip: TripSpec): PackingPlan {
        val end = if (trip.endDate.isBefore(trip.startDate)) trip.startDate else trip.endDate
        val days = ChronoUnit.DAYS.between(trip.startDate, end).toInt() + 1

        val byId = LinkedHashMap<Long, Garment>()
        for (g in closet) byId.putIfAbsent(g.id, g)
        // Laundry will be done before leaving; items that are away can't be packed. Recency is irrelevant on a trip.
        val usable = byId.values
            .filter { it.status != GarmentStatus.AWAY }
            .map { it.copy(status = GarmentStatus.CLEAN, lastWorn = null) }
        val usableById = usable.associateBy { it.id }

        val outfits = ArrayList<List<Long>>()
        val packed = LinkedHashSet<Long>()
        for (planned in trip.plannedOutfits) {
            outfits += planned
            planned.filterTo(packed) { it in byId }
        }

        val occasions = trip.occasions.sortedBy { it.ordinal }.ifEmpty { listOf(Occasion.CASUAL) }
        val rewearTops = trip.laundryAvailable && days > 7
        val topCap = if (rewearTops) 7 else days
        val bottomCap = ceil(days / 3.0).toInt() + 1
        val baseShoeCap = 2

        var missingDays = 0
        for (day in trip.plannedOutfits.size until days) {
            val date = trip.startDate.plusDays(day.toLong())
            val weather = trip.weatherByDay.getOrNull(day) ?: DEFAULT_WEATHER
            val occasion = occasions[day % occasions.size]
            val needs = WeatherMath.needs(weather)
            val packedGarments = packed.mapNotNull { usableById[it] }
            fun packedIn(vararg c: Category) = packedGarments.count { it.category in c }

            val exclude = HashSet<Long>()
            fun excludeUnpacked(filter: (Garment) -> Boolean) {
                for (g in usable) if (g.id !in packed && filter(g)) exclude += g.id
            }
            if (packedIn(Category.BOTTOM) >= bottomCap) excludeUnpacked { it.category == Category.BOTTOM }
            if (packedIn(Category.TOP, Category.ONE_PIECE) >= topCap) {
                excludeUnpacked { it.category == Category.TOP || it.category == Category.ONE_PIECE }
            }
            val packedShoes = packedGarments.filter { it.category == Category.SHOES }
            if (packedShoes.size >= baseShoeCap) {
                val allowSporty = occasion == Occasion.ACTIVE && packedShoes.none { it.sporty }
                val allowFormal = (occasion == Occasion.FORMAL || occasion == Occasion.SMART) && packedShoes.none { it.formality >= 4 }
                excludeUnpacked {
                    it.category == Category.SHOES && !(allowSporty && it.sporty) && !(allowFormal && it.formality >= 4)
                }
            }
            if (packedIn(Category.LAYER) >= LAYER_CAP) excludeUnpacked { it.category == Category.LAYER }
            val packedOuter = packedGarments.filter { it.category == Category.OUTERWEAR }
            if (packedOuter.size >= OUTER_CAP) {
                val allowWaterproof = needs.wantsWaterproof && packedOuter.none { it.waterproof } && packedOuter.size < OUTER_CAP + 1
                excludeUnpacked { it.category == Category.OUTERWEAR && !(allowWaterproof && it.waterproof) }
            }

            val boosts = HashMap<Long, Double>()
            for (g in packedGarments) {
                boosts[g.id] = when (g.category) {
                    Category.BOTTOM, Category.LAYER, Category.OUTERWEAR -> REUSE_BOOST
                    Category.SHOES -> SHOE_REUSE_BOOST
                    Category.TOP, Category.ONE_PIECE -> if (rewearTops) TOP_REWEAR_BOOST else TOP_REPEAT_PENALTY
                    else -> 0.0
                }
            }

            val request = StylingRequest(
                date = date,
                occasion = occasion,
                weather = weather,
                hemisphere = trip.hemisphere,
                excludeItemIds = exclude,
                count = 1,
                recentSignatures = outfits.map { OutfitSignature.of(it) }.toSet(),
                includeAccessories = false,
                seed = day.toLong(),
            )
            val outfit = Generator.suggest(usable, request, AffinityModel(), boosts).firstOrNull()
                ?: Generator.suggest(usable, request.copy(excludeItemIds = emptySet()), AffinityModel(), boosts).firstOrNull()
            if (outfit == null) {
                missingDays++
                continue
            }
            outfits += outfit.itemIds
            packed += outfit.itemIds
        }

        // Clothing lines: each garment once, in category order, with the days it's worn.
        val dayUses = LinkedHashMap<Long, MutableList<Int>>()
        outfits.forEachIndexed { index, ids -> ids.distinct().forEach { dayUses.getOrPut(it) { ArrayList() } += index + 1 } }
        val clothingGarments = dayUses.keys.mapNotNull { byId[it] }.sortedBy { it.category.order }
        val items = ArrayList<PackItem>()
        for (g in clothingGarments) {
            items += PackItem(
                label = g.subcategory.trim().ifEmpty { g.category.label },
                category = PackCategory.CLOTHING,
                quantity = 1,
                garmentId = g.id,
                reason = Words.dayList(dayUses.getValue(g.id)),
            )
        }
        val packedIds = clothingGarments.mapTo(HashSet()) { it.id }

        // Essentials.
        val underwear = minOf(days + 1, if (trip.laundryAvailable) 8 else days + 1)
        items += PackItem("Underwear", PackCategory.UNDERWEAR, underwear)
        items += PackItem("Socks", PackCategory.UNDERWEAR, underwear)
        items += PackItem("Sleepwear", PackCategory.SLEEP, if (days > 5) 2 else 1)
        for (t in listOf("Toothbrush", "Toothpaste", "Deodorant", "Skincare", "Medication")) {
            items += PackItem(t, PackCategory.TOILETRIES)
        }
        items += PackItem("Phone charger", PackCategory.TECH)
        items += PackItem("Power bank", PackCategory.TECH)
        items += PackItem("Travel adapter", PackCategory.TECH, reason = "If you're crossing borders, check the plug type")
        items += PackItem("ID / passport", PackCategory.DOCUMENTS)
        items += PackItem("Tickets & bookings", PackCategory.DOCUMENTS)
        items += PackItem("Wallet & cards", PackCategory.DOCUMENTS)

        // Weather extras, linked to a wardrobe item when the closet has one.
        val forecast = trip.weatherByDay.take(days)
        val wetDays = forecast.count { WeatherMath.wantsWaterproof(it) }
        val sunny = forecast.any { maxOf(it.tempMinC, it.tempMaxC) >= 22.0 || it.uvIndex >= 5.0 }
        val freezing = forecast.any { WeatherMath.effective(it) < 3.0 }
        val swim = forecast.any { maxOf(it.tempMinC, it.tempMaxC) >= 26.0 }
        fun closetMatch(vararg names: String): Garment? = usable.firstOrNull { g ->
            g.id !in packedIds && names.any { Words.noun(g).contains(it) }
        }
        fun extra(label: String, reason: String, vararg names: String) {
            val match = if (names.isEmpty()) null else closetMatch(*names)
            items += PackItem(label, PackCategory.WEATHER, garmentId = match?.id, reason = reason)
        }
        val notes = ArrayList<String>()
        if (wetDays > 0) {
            extra("Umbrella", "Rain expected on ${Words.days(wetDays)}")
            notes += "Rain on ${Words.days(wetDays)} — umbrella added"
        }
        if (sunny) {
            extra("Sunglasses", "Sunny days ahead", "sunglass")
            extra("Sunscreen", "Strong sun on the forecast")
            notes += "Sun on the forecast — sunglasses and sunscreen added"
        }
        if (freezing) {
            extra("Gloves", "Below 3°C at times", "glove")
            extra("Warm hat", "Below 3°C at times", "beanie", "hat")
            notes += "Freezing spells — gloves and a warm hat added"
        }
        if (swim) {
            extra("Swimwear", "Warm enough for a swim", "swim")
            notes += "Warm enough to swim — swimwear added"
        }

        val outfitWord = if (outfits.size == 1) "outfit" else "outfits"
        val pieceWord = if (clothingGarments.size == 1) "piece" else "pieces"
        notes.add(0, "${outfits.size} $outfitWord from ${clothingGarments.size} $pieceWord")
        if (rewearTops) notes += "Laundry on the way — tops are planned for re-wear"
        if (days >= 3 && clothingGarments.isNotEmpty()) notes += "Bottoms, shoes and layers are re-used across days to keep the bag light"
        if (missingDays > 0) notes += "Couldn't plan outfits for ${Words.days(missingDays)} — add more pieces to your closet"

        return PackingPlan(items = items, outfits = outfits, notes = notes)
    }
}
