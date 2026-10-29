package com.kintukka.hanger.engine

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

/** Matches outfits and closets against the guide's outfit formulas. */
internal object Formulas {
    private val guide get() = StyleKnowledge.bundled


    private fun categoryMask(items: List<Garment>): Int {
        var m = 0
        for (g in items) m = m or (1 shl g.category.ordinal)
        return m
    }

    /** Assign every slot a distinct item (tiny backtracking search; outfits have a handful of pieces). */
    private fun assign(slots: List<FormulaSlot>, pick: (FormulaSlot) -> List<Garment>, index: Int, used: MutableList<Garment>): Boolean {
        if (index == slots.size) return true
        for (g in pick(slots[index])) {
            if (used.any { it.id == g.id }) continue
            used += g
            if (assign(slots, pick, index + 1, used)) return true
            used.removeAt(used.size - 1)
        }
        return false
    }

    /** The most specific formula [items] fulfil, preferring ones meant for [occasion]. */
    fun match(items: List<Garment>, occasion: Occasion? = null): Formula? {
        if (items.size < 2) return null
        val mask = categoryMask(items)
        var best: PreparedFormula? = null
        var bestFits = false
        for (p in guide.preparedFormulas) {
            if (p.categoryMask and mask != p.categoryMask) continue
            if (best != null && p.specificity < best.specificity && bestFits) break
            if (!assign(p.slots, { s -> items.filter(s::accepts) }, 0, ArrayList())) continue
            val fits = occasion == null || p.formula.occasions.isEmpty() || occasion in p.formula.occasions
            if (best == null || (fits && !bestFits) || (fits == bestFits && p.specificity > best.specificity)) {
                best = p
                bestFits = fits
            }
        }
        return best?.formula
    }

    /** Formulas whose occasion and season fit, for seeding the generator. */
    fun seeds(occasion: Occasion, season: Season): List<Formula> =
        guide.formulas.filter { f ->
            (f.occasions.isEmpty() || occasion in f.occasions) && (f.seasons.isEmpty() || season in f.seasons)
        }

    /** Fill [formula]'s required slots from [pool], best-ranked first; null when a slot can't be filled. */
    fun fill(formula: Formula, pool: List<Garment>): List<Garment>? {
        val used = ArrayList<Garment>()
        val slots = formula.required.sortedByDescending { it.constraint }
        return if (assign(slots, { s -> pool.filter(s::accepts).take(6) }, 0, used)) used else null
    }

    fun coverage(closet: List<Garment>): FormulaCoverage {
        val ready = ArrayList<FormulaMatch>()
        val oneAway = ArrayList<FormulaGap>()
        for (f in guide.formulas) {
            val picks = fill(f, closet)
            if (picks != null) {
                ready += FormulaMatch(f, picks.sortedBy { it.category.order }.map { it.id })
                continue
            }
            val empty = f.required.filter { s -> closet.none(s::accepts) }
            if (empty.size == 1) {
                val rest = f.copy(slots = f.slots.filter { it !== empty[0] })
                val picks2 = fill(rest, closet) ?: continue
                oneAway += FormulaGap(f, empty[0], picks2.sortedBy { it.category.order }.map { it.id })
            }
        }
        return FormulaCoverage(ready, oneAway)
    }
}

/** Computes closet statistics and runs the guide's closet checks over them. */
internal object ClosetChecks {
    private val guide get() = StyleKnowledge.bundled
    private val SMART_SHOES = setOf("loafers", "dress shoes", "heels", "chelsea boots", "ankle boots", "flats")

    fun metrics(closet: List<Garment>, wearLog: List<WearRecord>, today: LocalDate, wishlist: Int): Map<String, Double> {
        val m = LinkedHashMap<String, Double>()
        val n = closet.size
        m["items"] = n.toDouble()
        m["wishlist"] = wishlist.toDouble()
        if (n == 0) return m
        fun count(c: Category) = closet.count { it.category == c }.toDouble()
        val withColor = closet.filter { it.colors.isNotEmpty() }
        if (withColor.isNotEmpty()) {
            m["neutralShare"] = withColor.count { it.colors.first().neutral }.toDouble() / withColor.size
            m["accentColours"] = withColor.map { it.colors.first() }.filter { !it.neutral }.distinct().size.toDouble()
        }
        m["boldPatternShare"] = closet.count { it.pattern.bold }.toDouble() / n
        m["tops"] = count(Category.TOP)
        m["bottoms"] = count(Category.BOTTOM)
        m["onePieces"] = count(Category.ONE_PIECE)
        m["layers"] = count(Category.LAYER)
        m["outerwear"] = count(Category.OUTERWEAR)
        m["shoes"] = count(Category.SHOES)
        m["bags"] = count(Category.BAG)
        m["accessories"] = count(Category.ACCESSORY)
        if (count(Category.BOTTOM) > 0) m["topsPerBottom"] = count(Category.TOP) / count(Category.BOTTOM)
        m["waterproofOuterwear"] = closet.count {
            it.category == Category.OUTERWEAR && (it.waterproof || it.subcategory.equals("Raincoat", ignoreCase = true))
        }.toDouble()
        m["warmOuterwear"] = closet.count { it.category == Category.OUTERWEAR && it.warmth >= 4 }.toDouble()
        m["smartShoes"] = closet.count { it.category == Category.SHOES && it.subcategory.lowercase() in SMART_SHOES }.toDouble()
        m["formalPieces"] = closet.count { it.formality >= 4 }.toDouble()
        m["casualShare"] = closet.count { it.formality <= 2 }.toDouble() / n
        val lastWorn = HashMap<Long, LocalDate>()
        for (r in wearLog) for (id in r.itemIds) if (lastWorn[id]?.isBefore(r.date) != false) lastWorn[id] = r.date
        fun worn(days: Long) = closet.count { g ->
            val d = maxOfNullable(lastWorn[g.id], g.lastWorn)
            d != null && ChronoUnit.DAYS.between(d, today) in 0 until days
        }.toDouble() / n
        m["wornShare30"] = worn(30)
        m["wornShare90"] = worn(90)
        val settled = closet.filter { it.added != null && ChronoUnit.DAYS.between(it.added, today) > 60 }
        if (settled.isNotEmpty()) m["neverWornShare"] = settled.count { it.wearCount == 0 && lastWorn[it.id] == null }.toDouble() / settled.size
        val cpw = closet.mapNotNull { g -> g.price?.takeIf { g.wearCount > 0 }?.div(g.wearCount) }
        if (cpw.isNotEmpty()) m["avgCostPerWear"] = cpw.average()
        m["summerPieces"] = closet.count { Season.SUMMER in it.seasons || it.warmth <= 2 }.toDouble()
        m["winterPieces"] = closet.count { Season.WINTER in it.seasons || it.warmth >= 4 }.toDouble()
        m["duplicates"] = closet.filter { it.subcategory.isNotBlank() && it.colors.isNotEmpty() }
            .groupingBy { it.subcategory.lowercase() to it.colors.first() }.eachCount().values.maxOrNull()?.toDouble() ?: 0.0
        m["laundryShare"] = closet.count { it.status == GarmentStatus.DIRTY }.toDouble() / n
        return m
    }

    private fun maxOfNullable(a: LocalDate?, b: LocalDate?): LocalDate? = when {
        a == null -> b
        b == null -> a
        else -> if (a.isAfter(b)) a else b
    }

    fun run(metrics: Map<String, Double>, currency: String): List<ClosetInsight> {
        val items = metrics["items"]?.toInt() ?: 0
        return guide.wardrobe.checks.mapNotNull { c ->
            val v = metrics[c.metric] ?: return@mapNotNull null
            if (items < c.minItems) return@mapNotNull null
            val fires = when (c.op) {
                "<" -> v < c.value
                "<=" -> v <= c.value
                ">" -> v > c.value
                ">=" -> v >= c.value
                "==" -> v == c.value
                else -> false
            }
            if (!fires) null else ClosetInsight(c, v, c.message.replace("{value}", format(c.metric, v, currency)))
        }
    }

    fun format(metric: String, v: Double, currency: String): String = when {
        metric.endsWith("Share") || metric.startsWith("wornShare") -> "${(v * 100).roundToInt()}%"
        metric == "avgCostPerWear" -> currency + String.format(java.util.Locale.ROOT, "%.2f", v)
        metric == "topsPerBottom" -> String.format(java.util.Locale.ROOT, "%.1f", v)
        else -> v.roundToInt().toString()
    }
}
