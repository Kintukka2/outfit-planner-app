package com.kintukka.hanger.engine

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/** "Dress me" slot machine. */
internal object ShuffleEngine {
    private const val SMART_SAMPLES = 200
    private const val SMART_JITTER = 0.06

    fun shuffle(closet: List<Garment>, request: ShuffleRequest): Map<Category, Long> {
        val rows = request.rows.distinct()
        if (rows.isEmpty()) return emptyMap()
        val rnd = Random(request.seed)
        val byId = LinkedHashMap<Long, Garment>()
        for (g in closet) byId.putIfAbsent(g.id, g)
        val garments = byId.values.toList()

        fun eligible(g: Garment, row: Category): Boolean =
            g.category == row &&
                (!request.onlyClean || g.status == GarmentStatus.CLEAN) &&
                (request.season == null || g.seasons.isEmpty() || request.season in g.seasons)

        val locked = LinkedHashMap<Category, Garment>()
        for (row in rows) {
            val id = request.locked[row] ?: continue
            byId[id]?.let { locked[row] = it } // stale locks (deleted garments) are ignored
        }
        val pools = LinkedHashMap<Category, List<Garment>>()
        for (row in rows) {
            if (row in locked) continue
            val pool = garments.filter { eligible(it, row) }
            if (pool.isNotEmpty()) pools[row] = pool
        }

        fun weight(g: Garment) = if (request.favourLeastWorn) 1.0 / (1.0 + g.wearCount.coerceAtLeast(0)) else 1.0
        fun pick(pool: List<Garment>): Garment {
            val total = pool.sumOf { weight(it) }
            var r = rnd.nextDouble() * total
            for (g in pool) {
                r -= weight(g)
                if (r <= 0.0) return g
            }
            return pool.last()
        }
        fun assemble(picks: Map<Category, Garment>): Map<Category, Long> {
            val out = LinkedHashMap<Category, Long>()
            for (row in rows) (locked[row] ?: picks[row])?.let { out[row] = it.id }
            return out
        }

        if (pools.isEmpty()) return assemble(emptyMap())
        if (!request.smart) return assemble(pools.mapValues { (_, pool) -> pick(pool) })

        val lockedItems = locked.values.toList()
        val seen = HashSet<String>()
        var best: Map<Category, Garment>? = null
        var bestScore = Double.NEGATIVE_INFINITY
        repeat(SMART_SAMPLES) {
            val picks = pools.mapValues { (_, pool) -> pick(pool) }
            val key = picks.values.joinToString(",") { it.id.toString() }
            if (!seen.add(key)) return@repeat
            val items = lockedItems + picks.values
            var score = Evaluator.run(items, null, null, AffinityModel(), explain = false).score
            score *= 1.0 + rnd.nextDouble(-SMART_JITTER, SMART_JITTER)
            if (request.favourLeastWorn) score += 0.1 * picks.values.map { weight(it) }.average()
            if (score > bestScore) {
                bestScore = score
                best = picks
            }
        }
        return assemble(best ?: emptyMap())
    }
}

internal object Feedback {
    private const val LIMIT = 2.0

    private fun delta(type: FeedbackType): Double = when (type) {
        FeedbackType.WORN -> 0.30
        FeedbackType.SAVED -> 0.20
        FeedbackType.LIKED -> 0.15
        FeedbackType.REJECTED -> -0.40
    }

    fun apply(model: AffinityModel, event: FeedbackEvent): AffinityModel {
        val ids = event.itemIds.distinct()
        if (ids.isEmpty()) return model
        val d = delta(event.type)
        val pairs = HashMap(model.pairScores)
        for (i in ids.indices) for (j in i + 1 until ids.size) {
            val key = AffinityModel.pairKey(ids[i], ids[j])
            pairs[key] = ((pairs[key] ?: 0.0) + d).coerceIn(-LIMIT, LIMIT)
        }
        val items = HashMap(model.itemScores)
        for (id in ids) items[id] = ((items[id] ?: 0.0) + d * 0.5).coerceIn(-LIMIT, LIMIT)
        return AffinityModel(pairScores = pairs.toMap(), itemScores = items.toMap())
    }
}

internal object Stats {
    private val MONTH = DateTimeFormatter.ofPattern("yyyy-MM")
    private const val TOP_N = 10

    fun costPerWear(g: Garment): Double? {
        val price = g.price ?: return null
        if (g.wearCount <= 0) return null
        return price / g.wearCount
    }

    fun compute(closet: List<Garment>, wearLog: List<WearRecord>, today: LocalDate): StatsReport {
        val garments = LinkedHashMap<Long, Garment>().also { m -> closet.forEach { m.putIfAbsent(it.id, it) } }.values.toList()
        val ids = garments.mapTo(HashSet()) { it.id }
        // Only real wear: entries up to today that actually list garments.
        val log = wearLog.filter { !it.date.isAfter(today) && it.itemIds.isNotEmpty() }
        val loggedDates = log.mapTo(sortedSetOf()) { it.date }

        fun utilisation(days: Long): Double {
            if (garments.isEmpty()) return 0.0
            val from = today.minusDays(days - 1)
            val worn = HashSet<Long>()
            for (r in log) if (!r.date.isBefore(from)) r.itemIds.filterTo(worn) { it in ids }
            return worn.size.toDouble() / garments.size
        }

        val worn = garments.filter { it.wearCount > 0 }
        val mostWorn = worn.sortedWith(compareByDescending<Garment> { it.wearCount }.thenBy { it.id })
            .take(TOP_N).map { CountedGarment(it.id, it.wearCount.toDouble()) }
        val leastWorn = worn.sortedWith(compareBy<Garment> { it.wearCount }.thenBy { it.id })
            .take(TOP_N).map { CountedGarment(it.id, it.wearCount.toDouble()) }
        val cpw = garments.mapNotNull { g -> costPerWear(g)?.let { g to it } }
        val best = cpw.sortedWith(compareBy<Pair<Garment, Double>> { it.second }.thenBy { it.first.id })
            .take(TOP_N).map { CountedGarment(it.first.id, it.second) }
        val worst = cpw.sortedWith(compareByDescending<Pair<Garment, Double>> { it.second }.thenBy { it.first.id })
            .take(TOP_N).map { CountedGarment(it.first.id, it.second) }
        val priced = garments.filter { it.price != null }
        val pricedWorn = priced.filter { it.wearCount > 0 }
        val averageCpw = if (pricedWorn.isEmpty()) null
        else pricedWorn.sumOf { it.price!! } / pricedWorn.sumOf { it.wearCount }

        val forgottenCutoff = today.minusDays(60)
        val forgotten = garments.filter { g -> g.lastWorn?.let { !it.isAfter(forgottenCutoff) } == true }
            .sortedWith(compareBy<Garment> { it.lastWorn }.thenBy { it.id })
            .map { it.id }

        val byCategory = LinkedHashMap<Category, Int>()
        for (c in Category.entries.sortedBy { it.order }) {
            val n = garments.count { it.category == c }
            if (n > 0) byCategory[c] = n
        }
        val colorCounts = garments.mapNotNull { it.colors.firstOrNull() }.groupingBy { it }.eachCount()
        val byColor = LinkedHashMap<FashionColor, Int>()
        colorCounts.entries.sortedWith(compareByDescending<Map.Entry<FashionColor, Int>> { it.value }.thenBy { it.key.ordinal })
            .forEach { byColor[it.key] = it.value }

        val currentMonth = YearMonth.from(today)
        val perMonth = loggedDates.groupingBy { YearMonth.from(it) }.eachCount()
        val wearsPerMonth = (11 downTo 0).map { back ->
            val ym = currentMonth.minusMonths(back.toLong())
            ym.format(MONTH) to (perMonth[ym] ?: 0)
        }

        var longest = 0
        var run = 0
        var previous: LocalDate? = null
        for (d in loggedDates) {
            run = if (previous != null && ChronoUnit.DAYS.between(previous, d) == 1L) run + 1 else 1
            longest = maxOf(longest, run)
            previous = d
        }
        var current = 0
        var cursor = if (today in loggedDates) today else today.minusDays(1)
        while (cursor in loggedDates) {
            current++
            cursor = cursor.minusDays(1)
        }

        return StatsReport(
            totalItems = garments.size,
            closetValue = priced.sumOf { it.price!! },
            itemsWithPrice = priced.size,
            averageCostPerWear = averageCpw,
            utilisation30 = utilisation(30),
            utilisation90 = utilisation(90),
            mostWorn = mostWorn,
            leastWorn = leastWorn,
            neverWorn = garments.filter { it.wearCount <= 0 }.map { it.id },
            bestCostPerWear = best,
            worstCostPerWear = worst,
            forgotten = forgotten,
            byCategory = byCategory,
            byColor = byColor,
            wearsPerMonth = wearsPerMonth,
            currentStreak = current,
            longestStreak = longest,
            daysLogged = loggedDates.size,
        )
    }
}

/**
 * Counts valid core outfits; status is ignored because capsule planning includes laundry.
 *
 * A complete core outfit scored without weather, occasion or affinity depends only on colour, pattern and
 * formality, so the loops use precomputed per-garment features and the evaluator's shared scoring helpers
 * (identical results to [Evaluator.run], at a fraction of the cost).
 */
internal object Capsule {
    const val MIN_SCORE = 0.55
    const val MAX_SPREAD = 2

    private class Feat(g: Garment) {
        val mask = Harmony.maskOf(g.colors)
        val bold = g.pattern.bold
        val pattern = g.pattern
        val formality = g.formality
        val type = StyleKnowledge.bundled.typeIndex(g.subcategory)
    }

    /** Top+bottom (or one-piece) features, computed once per pair. */
    private class CorePart(a: Feat, b: Feat?) {
        val coreMax = if (b == null) a.formality else max(a.formality, b.formality)
        val coreMin = if (b == null) a.formality else min(a.formality, b.formality)
        val mask = a.mask or (b?.mask ?: 0)
        val typeA = a.type
        val typeB = b?.type ?: -1
        val pairScore = if (b == null) 0 else StyleKnowledge.bundled.typeScore(a.type, b.type)
        var bold = 0
        var p1: Pattern? = null
        var p2: Pattern? = null

        init {
            add(a)
            if (b != null) add(b)
        }

        private fun add(f: Feat) {
            if (!f.bold) return
            bold++
            if (p1 == null) p1 = f.pattern else if (p2 == null) p2 = f.pattern
        }
    }

    private fun valid(core: CorePart, shoe: Feat): Boolean {
        val shoeFormality = Evaluator.adjustedFormality(shoe.formality, Category.SHOES, core.coreMax)
        val spread = max(core.coreMax, shoeFormality) - min(core.coreMin, shoeFormality)
        if (spread > MAX_SPREAD) return false
        var bold = core.bold
        var p1 = core.p1
        var p2 = core.p2
        if (shoe.bold) {
            bold++
            if (p1 == null) p1 = shoe.pattern else if (p2 == null) p2 = shoe.pattern
        }
        val guide = StyleKnowledge.bundled
        val withA = guide.typeScore(core.typeA, shoe.type)
        val withB = guide.typeScore(core.typeB, shoe.type)
        val lift = Evaluator.typeLift(core.pairScore) + Evaluator.typeLift(withA) + Evaluator.typeLift(withB)
        val cost = Evaluator.typeCost(core.pairScore) + Evaluator.typeCost(withA) + Evaluator.typeCost(withB)
        val score = Evaluator.combine(
            Evaluator.colorScore(core.mask or shoe.mask),
            Evaluator.patternScore(bold, p1, p2),
            Evaluator.formalityScore(spread),
        ) + Evaluator.typeAdjustment(lift, cost)
        return score >= MIN_SCORE
    }

    private class Split(garments: List<Garment>) {
        val tops = garments.filter { it.category == Category.TOP }.map(::Feat)
        val bottoms = garments.filter { it.category == Category.BOTTOM }.map(::Feat)
        val shoes = garments.filter { it.category == Category.SHOES }.map(::Feat)
        val onePieces = garments.filter { it.category == Category.ONE_PIECE }.map(::Feat)
    }

    private fun distinct(garments: List<Garment>): List<Garment> {
        val m = LinkedHashMap<Long, Garment>()
        for (g in garments) m.putIfAbsent(g.id, g)
        return m.values.toList()
    }

    /** Core formality gap already too wide: shoes can only widen it. */
    private fun pairTooFar(a: Feat, b: Feat) = abs(a.formality - b.formality) > MAX_SPREAD

    private fun countWithShoes(core: CorePart, shoes: List<Feat>): Int {
        var n = 0
        for (s in shoes) if (valid(core, s)) n++
        return n
    }

    fun outfitCount(garments: List<Garment>): Int {
        val s = Split(distinct(garments))
        if (s.shoes.isEmpty()) return 0
        var count = 0
        for (t in s.tops) for (b in s.bottoms) {
            if (!pairTooFar(t, b)) count += countWithShoes(CorePart(t, b), s.shoes)
        }
        for (d in s.onePieces) count += countWithShoes(CorePart(d, null), s.shoes)
        return count
    }

    /** Only combinations that include [candidate] can be new, so count just those. */
    fun wishlistImpact(closet: List<Garment>, candidate: Garment): Int {
        val existing = distinct(closet)
        if (existing.any { it.id == candidate.id }) return 0
        val s = Split(existing)
        val c = Feat(candidate)
        var count = 0
        when (candidate.category) {
            Category.TOP -> for (b in s.bottoms) if (!pairTooFar(c, b)) count += countWithShoes(CorePart(c, b), s.shoes)
            Category.BOTTOM -> for (t in s.tops) if (!pairTooFar(t, c)) count += countWithShoes(CorePart(t, c), s.shoes)
            Category.ONE_PIECE -> count += countWithShoes(CorePart(c, null), s.shoes)
            Category.SHOES -> {
                val shoe = listOf(c)
                for (t in s.tops) for (b in s.bottoms) if (!pairTooFar(t, b)) count += countWithShoes(CorePart(t, b), shoe)
                for (d in s.onePieces) count += countWithShoes(CorePart(d, null), shoe)
            }
            else -> {}
        }
        return count
    }
}
