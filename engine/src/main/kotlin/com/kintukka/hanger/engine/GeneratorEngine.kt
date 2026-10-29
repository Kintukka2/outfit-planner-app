package com.kintukka.hanger.engine

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.EnumMap
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.tanh
import kotlin.random.Random

/**
 * Outfit suggestion pipeline:
 * eligibility + season filter -> per-item priors -> top-K per slot -> core combos scored by [Evaluator]
 * -> weather/accessory add-ons -> diverse (MMR-style) selection -> stylist reasons.
 */
internal object Generator {
    private const val K_CORE = 10
    private const val K_ADDON = 6
    private const val MIN_KEEP = 40
    private const val PER_BASE = 2
    private const val JITTER = 0.05
    private const val ITEM_JITTER = 0.03
    /** An alternative only counts for diversity if it is at least this good relative to the best remaining. */
    private const val QUALITY_FLOOR = 0.90
    /** Ranking lift for outfits that follow one of the style guide's proven formulas for the occasion. */
    private const val FORMULA_BONUS = 0.05

    private val CORE_SLOTS = setOf(Category.TOP, Category.BOTTOM, Category.ONE_PIECE, Category.SHOES)
    private val ADD_ON_SLOTS = setOf(Category.LAYER, Category.OUTERWEAR, Category.BAG, Category.ACCESSORY, Category.OTHER)
    private val CLOTHING_SLOTS = setOf(Category.TOP, Category.BOTTOM, Category.ONE_PIECE, Category.LAYER)

    private class Ctx(
        val date: LocalDate,
        val occasion: Occasion,
        val weather: WeatherCtx?,
        val affinity: AffinityModel,
        val extraPrior: Map<Long, Double>,
    ) {
        val needs: WeatherNeeds? = weather?.needs
        val wantLayer: Boolean = needs?.let { it.suggestsLayer || (it.needsOuterwear && it.targetWarmth >= 4.0) } ?: false
    }

    /** Lightweight scored core combination (no strings or sets until shortlisted). */
    private class Core(val items: List<Garment>, val final: Double, val jitter: Double)

    private class Combo(val core: Core) {
        val coreIds: Set<Long> = core.items.mapTo(HashSet()) { it.id }
        val coreSig: String = OutfitSignature.of(coreIds)
        var items: List<Garment> = core.items
        var final: Double = core.final
        var quality: Double = 0.0
        var signature: String = coreSig
        var recent: Boolean = false
    }

    private fun baseKey(core: Core): String = OutfitSignature.of(core.items.filter { it.category != Category.SHOES }.map { it.id })

    fun suggest(
        closet: List<Garment>,
        request: StylingRequest,
        affinity: AffinityModel,
        extraPrior: Map<Long, Double> = emptyMap(),
    ): List<SuggestedOutfit> {
        if (request.count <= 0 || closet.isEmpty()) return emptyList()
        val rnd = Random(request.seed)
        val weather = request.weather?.let { WeatherCtx.of(it) }
        val ctx = Ctx(request.date, request.occasion, weather, affinity, extraPrior)
        val season = SeasonMath.forDate(request.date, request.hemisphere)

        val byId = LinkedHashMap<Long, Garment>()
        for (g in closet) byId.putIfAbsent(g.id, g)

        // Anchors are explicit requests: they skip the clean/season filters (but not exclusions).
        val anchors = request.anchorItemIds.filter { it !in request.excludeItemIds }.mapNotNull { byId[it] }.sortedBy { it.id }
        val anchorIds = anchors.mapTo(HashSet()) { it.id }
        val available = byId.values
            .filter { it.status == GarmentStatus.CLEAN && it.id !in request.excludeItemIds && it.id !in anchorIds }
            .sortedBy { it.id }

        fun inSeason(g: Garment) = g.seasons.isEmpty() || season in g.seasons
        val inSeasonBy = available.filter(::inSeason).groupBy { it.category }
        val outOfSeasonBy = available.filterNot(::inSeason).groupBy { it.category }
        fun inSeasonCount(c: Category) = inSeasonBy[c]?.size ?: 0
        fun relax(c: Category): Boolean = when (c) {
            Category.TOP, Category.BOTTOM, Category.SHOES -> inSeasonCount(c) < 2
            Category.ONE_PIECE -> inSeasonCount(c) == 0 && (inSeasonCount(Category.TOP) == 0 || inSeasonCount(Category.BOTTOM) == 0)
            Category.LAYER -> inSeasonCount(c) == 0 && ctx.wantLayer
            Category.OUTERWEAR -> inSeasonCount(c) == 0 && (ctx.needs?.needsOuterwear == true || ctx.needs?.wantsWaterproof == true)
            else -> false
        }

        // Priors and per-slot ranking (small seeded jitter so "regenerate" explores different pieces).
        val priors = HashMap<Long, Double>()
        val ranked = EnumMap<Category, List<Garment>>(Category::class.java)
        for (c in Category.entries) {
            val scored = ArrayList<Pair<Garment, Double>>()
            inSeasonBy[c]?.forEach { g ->
                val p = prior(g, ctx, outOfSeason = false)
                priors[g.id] = p
                scored += g to p + rnd.nextDouble(-ITEM_JITTER, ITEM_JITTER)
            }
            if (relax(c)) outOfSeasonBy[c]?.forEach { g ->
                val p = prior(g, ctx, outOfSeason = true)
                priors[g.id] = p
                scored += g to p + rnd.nextDouble(-ITEM_JITTER, ITEM_JITTER)
            }
            ranked[c] = scored.sortedWith(compareByDescending<Pair<Garment, Double>> { it.second }.thenBy { it.first.id }).map { it.first }
        }
        for (a in anchors) priors[a.id] = prior(a, ctx, outOfSeason = false)

        // Anchors: core anchors fix their slot (several in one slot are alternatives), others are forced add-ons.
        val anchorBy = anchors.groupBy { it.category }
        val topAnchors = anchorBy[Category.TOP].orEmpty()
        val bottomAnchors = anchorBy[Category.BOTTOM].orEmpty()
        val shoeAnchors = anchorBy[Category.SHOES].orEmpty()
        val onePieceAnchors = anchorBy[Category.ONE_PIECE].orEmpty()
        val forced = ArrayList<Garment>(anchors.filter { it.category in ADD_ON_SLOTS })
        val separatesTemplate = onePieceAnchors.isEmpty() || topAnchors.isNotEmpty() || bottomAnchors.isNotEmpty()
        val onePieceTemplate = topAnchors.isEmpty() && bottomAnchors.isEmpty()
        if (onePieceAnchors.isNotEmpty() && separatesTemplate) forced += onePieceAnchors

        val tops = topAnchors.ifEmpty { ranked.getValue(Category.TOP).take(K_CORE) }
        val bottoms = bottomAnchors.ifEmpty { ranked.getValue(Category.BOTTOM).take(K_CORE) }
        val shoes = shoeAnchors.ifEmpty { ranked.getValue(Category.SHOES).take(K_CORE) }
        val onePieces = onePieceAnchors.ifEmpty { ranked.getValue(Category.ONE_PIECE).take(K_CORE) }
        val layerPool = ranked.getValue(Category.LAYER).take(K_ADDON)
        val outerPool = ranked.getValue(Category.OUTERWEAR).take(K_ADDON)
        val bagPool = ranked.getValue(Category.BAG).take(K_ADDON)
        val accessoryPool = ranked.getValue(Category.ACCESSORY).take(K_ADDON)

        val needs = ctx.needs
        fun forcedHas(c: Category) = forced.any { it.category == c }
        val willAddOuter = needs != null && needs.needsOuterwear && !forcedHas(Category.OUTERWEAR) && outerPool.isNotEmpty()
        val willAddLayer = ctx.wantLayer && !forcedHas(Category.LAYER) && layerPool.isNotEmpty()
        val assumedWarmth = (if (willAddOuter) 0.9 else 0.0) + (if (willAddLayer) 0.5 else 0.0)
        val deferOuter = willAddOuter ||
            (needs?.wantsWaterproof == true && !forcedHas(Category.OUTERWEAR) && outerPool.any { it.waterproof })

        // The same combination is ranked several times (core, add-on candidates, final), so match each once.
        val boosts = HashMap<String, Double>()
        fun formulaBoost(items: List<Garment>): Double = boosts.getOrPut(OutfitSignature.of(items.map { it.id })) {
            val f = Formulas.match(items, ctx.occasion)
            if (f != null && (f.occasions.isEmpty() || ctx.occasion in f.occasions)) 1.0 + FORMULA_BONUS else 1.0
        }

        fun priorFactor(items: List<Garment>): Double {
            var wSum = 0.0
            var pSum = 0.0
            for (g in items) {
                val w = priorWeight(g.category)
                pSum += w * (priors[g.id] ?: 0.0)
                wSum += w
            }
            return max(0.05, 1.0 + if (wSum > 0) pSum / wSum else 0.0)
        }

        // Core combinations, scored with the add-ons the weather will bring assumed in.
        val cores = ArrayList<Core>()
        fun scoreCore(core: List<Garment>) {
            val items = if (forced.isEmpty()) core else core + forced
            val r = Evaluator.run(items, weather, ctx.occasion, affinity, explain = false, assumedWarmth, deferOuter)
            val jitter = rnd.nextDouble(-JITTER, JITTER)
            cores += Core(core, r.score * priorFactor(items) * formulaBoost(items) * (1.0 + jitter), jitter)
        }
        if (separatesTemplate) for (t in tops) for (b in bottoms) for (s in shoes) scoreCore(listOf(t, b, s))
        if (onePieceTemplate) for (d in onePieces) for (s in shoes) scoreCore(listOf(d, s))

        // Proven formulas for this occasion and season, filled from the closet, join the pool even when
        // their pieces sit outside the top-ranked few in each slot.
        if (anchors.isEmpty()) {
            val seen = cores.mapTo(HashSet()) { c -> OutfitSignature.of(c.items.map { it.id }) }
            val corePool = listOf(Category.TOP, Category.BOTTOM, Category.ONE_PIECE, Category.SHOES).flatMap { ranked.getValue(it) }
            for (f in Formulas.seeds(ctx.occasion, season)) {
                val coreOnly = f.copy(slots = f.required.filter { it.category in CORE_SLOTS })
                val picks = Formulas.fill(coreOnly, corePool) ?: continue
                if (seen.add(OutfitSignature.of(picks.map { it.id }))) scoreCore(picks.sortedBy { it.category.order })
            }
        }
        if (cores.isEmpty()) return emptyList()

        // Shortlist: best few shoe options per top+bottom (or dress) so the pool stays varied.
        val keep = max(MIN_KEEP, request.count * 10)
        val perBase = HashMap<String, Int>()
        val shortlist = ArrayList<Combo>(keep)
        for (core in cores.sortedWith(compareByDescending<Core> { it.final }.thenBy { OutfitSignature.of(it.items.map { g -> g.id }) })) {
            val key = baseKey(core)
            val n = (perBase[key] ?: 0) + 1
            perBase[key] = n
            if (n > PER_BASE) continue
            shortlist += Combo(core)
            if (shortlist.size >= keep) break
        }

        // Add-ons: outerwear and layers for the weather, then a bag and an accessory that keep the score.
        for (c in shortlist) {
            var items: List<Garment> = if (forced.isEmpty()) c.core.items else c.core.items + forced
            fun finalOf(list: List<Garment>) =
                Evaluator.run(list, weather, ctx.occasion, affinity, explain = false).score * priorFactor(list) *
                    formulaBoost(list) * (1.0 + c.core.jitter)
            var current = finalOf(items)
            if (needs != null && items.none { it.category == Category.OUTERWEAR }) {
                val options = if (needs.needsOuterwear) outerPool else if (needs.wantsWaterproof) outerPool.filter { it.waterproof } else emptyList()
                best(options) { finalOf(items + it) }?.let { (g, value) ->
                    if (needs.needsOuterwear || value >= current - 0.03) {
                        items = items + g
                        current = value
                    }
                }
            }
            if (ctx.wantLayer && items.none { it.category == Category.LAYER }) {
                best(layerPool) { finalOf(items + it) }?.let { (g, value) ->
                    if (value >= current - 0.02) {
                        items = items + g
                        current = value
                    }
                }
            }
            if (request.includeAccessories) {
                for (pool in listOf(bagPool, accessoryPool)) {
                    if (pool.isEmpty() || items.any { it.category == pool[0].category }) continue
                    val base = Evaluator.run(items, weather, ctx.occasion, affinity, explain = false).score
                    // The accessory's own prior (scarf in the cold, sunglasses in the sun) may outweigh a small colour cost.
                    best(pool) { Evaluator.run(items + it, weather, ctx.occasion, affinity, explain = false).score + 0.25 * (priors[it.id] ?: 0.0) }
                        ?.let { (g, gain) -> if (gain >= base - 0.005) items = items + g }
                }
            }
            c.items = items
            val eval = Evaluator.run(items, weather, ctx.occasion, affinity, explain = false)
            c.quality = eval.score
            c.final = eval.score * priorFactor(items) * formulaBoost(items) * (1.0 + c.core.jitter)
            c.signature = OutfitSignature.of(items.map { it.id })
        }

        // Diverse selection: prefer good alternatives that share at most one core piece with earlier picks,
        // never repeat a recent look while any alternative exists.
        val recent = request.recentSignatures
        val recentCores = recent.mapNotNull { sig ->
            val ids = sig.split('-').mapNotNull { it.toLongOrNull() }.filter { byId[it]?.category in CORE_SLOTS }
            if (ids.isEmpty()) null else OutfitSignature.of(ids)
        }.toSet()
        for (c in shortlist) c.recent = c.signature in recent || c.coreSig in recentCores

        val ordered = shortlist.sortedWith(compareByDescending<Combo> { it.final }.thenBy { it.coreSig })
        val chosen = ArrayList<Combo>()
        while (chosen.size < request.count) {
            val remaining = ordered.filter { c -> chosen.none { it.coreSig == c.coreSig } }
            if (remaining.isEmpty()) break
            val bestFresh = remaining.filter { !it.recent }.maxOfOrNull { it.final }
            val floor = (bestFresh ?: 0.0) * QUALITY_FLOOR
            fun maxShared(c: Combo) = chosen.maxOfOrNull { other -> c.coreIds.count { it in other.coreIds } } ?: 0
            val tiers: List<(Combo) -> Boolean> = listOf(
                { c -> !c.recent && c.final >= floor && maxShared(c) <= 1 },
                { c -> !c.recent && c.final >= floor },
                { c -> !c.recent },
                { _ -> true },
            )
            var pick: Combo? = null
            for (tier in tiers) {
                pick = remaining.filter(tier).maxByOrNull { c ->
                    val sharedAddOns = chosen.sumOf { other ->
                        c.items.count { g -> g.category !in CORE_SLOTS && other.items.any { it.id == g.id } }
                    }
                    c.final - 0.06 * maxShared(c) - 0.02 * sharedAddOns
                }
                if (pick != null) break
            }
            chosen += pick ?: break
        }

        return chosen.map { c ->
            val items = c.items.sortedBy { it.category.order }
            SuggestedOutfit(
                itemIds = items.map { it.id },
                score = c.quality.coerceIn(0.0, 1.0),
                reasons = reasons(items, ctx),
            )
        }
    }

    /** Best element by [value], evaluating each element once; ties keep the earlier (higher-ranked) one. */
    private inline fun best(pool: List<Garment>, value: (Garment) -> Double): Pair<Garment, Double>? {
        var bestG: Garment? = null
        var bestV = Double.NEGATIVE_INFINITY
        for (g in pool) {
            val v = value(g)
            if (v > bestV) { bestV = v; bestG = g }
        }
        return bestG?.let { it to bestV }
    }

    private fun priorWeight(c: Category): Double = when (c) {
        Category.TOP, Category.BOTTOM, Category.ONE_PIECE -> 1.0
        Category.SHOES -> 0.8
        Category.LAYER, Category.OUTERWEAR -> 0.7
        Category.BAG, Category.ACCESSORY -> 0.4
        Category.OTHER -> 0.3
    }

    /** Coats, shoes, bags and accessories are fine to repeat, so recency and neglect matter less for them. */
    private fun rotationScale(c: Category): Double = when (c) {
        Category.TOP, Category.BOTTOM, Category.ONE_PIECE, Category.LAYER -> 1.0
        Category.SHOES -> 0.35
        else -> 0.15
    }

    /** Per-item preference before outfit-level scoring; ~0 is neutral, ±0.1 ≈ ±10%. */
    private fun prior(g: Garment, ctx: Ctx, outOfSeason: Boolean): Double {
        var p = 0.0
        val occasion = ctx.occasion
        val flex = when (g.category) {
            Category.SHOES, Category.OUTERWEAR, Category.BAG, Category.ACCESSORY -> 1
            else -> 0
        }
        val lo = occasion.formality.first
        val hi = occasion.formality.last + flex
        val dist = when {
            g.formality < lo -> lo - g.formality
            g.formality > hi -> g.formality - hi
            else -> 0
        }
        val formalityWeight = when (g.category) {
            Category.BAG, Category.ACCESSORY -> 0.5
            Category.OTHER -> 0.0
            else -> 1.0
        }
        p -= 0.25 * dist * formalityWeight
        if (dist == 0 && (g.category in CLOTHING_SLOTS || g.category == Category.SHOES)) {
            p -= 0.03 * abs(g.formality - occasion.ideal)
        }
        when (occasion) {
            Occasion.ACTIVE -> when {
                g.sporty -> p += 0.15
                g.category == Category.SHOES -> p -= 0.25
                g.category in CORE_SLOTS || g.category in setOf(Category.LAYER, Category.BAG, Category.ACCESSORY) -> p -= 0.1
            }
            Occasion.WORK, Occasion.SMART, Occasion.FORMAL, Occasion.DATE -> if (g.sporty) p -= 0.4
            else -> {}
        }
        ctx.weather?.let { p += weatherPrior(g, it) }

        val last = g.lastWorn
        val rotation = rotationScale(g.category)
        if (last != null) {
            val days = ChronoUnit.DAYS.between(last, ctx.date)
            val penalty = when {
                days == 0L -> 0.45
                days in 1..7 -> 0.40 * (1.0 - (days - 1) / 7.0)
                else -> 0.0
            }
            p -= penalty * rotation
            if (days >= 21) p += 0.12 * max(0.5, rotation)
        } else if (g.wearCount == 0) {
            p += 0.12 * max(0.5, rotation)
        }
        if (g.favorite) p += 0.10
        p += 0.1 * tanh(ctx.affinity.item(g.id))
        p += ctx.extraPrior[g.id] ?: 0.0
        if (outOfSeason) p -= 0.2
        return p
    }

    private fun weatherPrior(g: Garment, wc: WeatherCtx): Double {
        val needs = wc.needs
        val target = needs.targetWarmth
        var p = 0.0
        when (g.category) {
            Category.TOP, Category.BOTTOM, Category.ONE_PIECE -> {
                val expectedAddOns = (if (needs.needsOuterwear) 0.9 else 0.0) +
                    (if (needs.suggestsLayer || (needs.needsOuterwear && target >= 4.0)) 0.5 else 0.0)
                val ideal = (target - expectedAddOns).coerceIn(1.0, 3.5)
                p -= 0.18 * max(0.0, abs(g.warmth - ideal) - 0.6)
                if (!needs.allowsShortsAndOpenShoes && Shapes.isShortsLike(g)) p -= 0.6
            }
            Category.SHOES -> {
                if (!needs.allowsShortsAndOpenShoes && Shapes.isOpenShoe(g)) p -= 0.6
                if (target >= 3.6 && g.warmth >= 4) p += 0.08
                if (target >= 4.0 && g.warmth <= 2) p -= 0.06
                if (target <= 1.8 && g.warmth >= 4) p -= 0.2
                if (needs.wantsWaterproof && g.waterproof) p += 0.08
            }
            Category.LAYER -> {
                val ideal = if (target >= 4.0) 4.0 else 3.0
                p -= 0.08 * abs(g.warmth - ideal)
            }
            Category.OUTERWEAR -> {
                val ideal = when {
                    target >= 4.5 -> 5.0
                    target >= 4.0 -> 4.5
                    target >= 3.3 -> 3.8
                    else -> 3.0
                }
                p -= 0.1 * abs(g.warmth - ideal)
                if (needs.wantsWaterproof) p += if (g.waterproof) 0.2 else -0.05
            }
            Category.ACCESSORY -> {
                if (Shapes.isWarmAccessory(g)) {
                    p += when {
                        target >= 4.0 -> 0.2
                        target >= 3.6 -> 0.1
                        target >= 3.0 -> -0.15
                        else -> -0.5
                    }
                }
                if (Shapes.isSunglasses(g)) {
                    if (wc.isSunny) p += 0.12 else if (wc.isGloomy) p -= 0.4
                }
            }
            else -> {}
        }
        return p
    }

    private fun ago(days: Long): String = if (days < 63) "${days / 7} weeks" else "${days / 30} months"

    /** 2–4 short stylist notes: best evaluator notes plus the story of a piece (neglected, new, favourite). */
    private fun reasons(items: List<Garment>, ctx: Ctx): List<String> {
        val eval = Evaluator.run(items, ctx.weather, ctx.occasion, ctx.affinity, explain = true, date = ctx.date)
        val notes = eval.notes.map { it.text }
        val neglected = ArrayList<Pair<Long, String>>()
        val firstOutings = ArrayList<String>()
        val favourites = ArrayList<String>()
        for (g in items) {
            if (g.category == Category.BAG || g.category == Category.ACCESSORY || g.category == Category.OTHER) continue
            val last = g.lastWorn
            if (last != null) {
                val days = ChronoUnit.DAYS.between(last, ctx.date)
                if (days >= 21) neglected += days to "You haven't worn the ${Words.phrase(g)} in ${ago(days)}"
            } else if (g.wearCount == 0) {
                firstOutings += "First outing for the ${Words.phrase(g)}"
            }
            if (g.favorite) favourites += "Your favourite ${Words.phrase(g)}"
        }
        val stories = neglected.sortedByDescending { it.first }.map { it.second } + firstOutings + favourites

        val out = LinkedHashSet<String>()
        notes.take(2).forEach { out += it }
        stories.take(1).forEach { out += it }
        for (s in notes.drop(2) + stories.drop(1)) {
            if (out.size >= 4) break
            out += s
        }
        if (out.size < 2) out += "A balanced, easy combination"
        if (out.size < 2) out += "Every piece is clean and ready to wear"
        return out.take(4)
    }
}
