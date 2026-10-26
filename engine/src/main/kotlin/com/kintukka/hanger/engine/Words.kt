package com.kintukka.hanger.engine

import kotlin.math.roundToInt

/** A stylist note or warning with a priority used to pick the most relevant ones. */
internal class Note(val text: String, val priority: Double)

/** Small text helpers so every note reads naturally ("the olive chinos", "Sandals look chilly"). */
internal object Words {
    private val numbers = arrayOf("Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten")

    fun count(n: Int): String = numbers.getOrElse(n) { n.toString() }

    fun defaultNoun(category: Category): String = when (category) {
        Category.TOP -> "top"
        Category.BOTTOM -> "bottoms"
        Category.ONE_PIECE -> "dress"
        Category.LAYER -> "layer"
        Category.OUTERWEAR -> "jacket"
        Category.SHOES -> "shoes"
        Category.BAG -> "bag"
        Category.ACCESSORY -> "accessory"
        Category.OTHER -> "piece"
    }

    /** "chinos", "knit sweater", or a category noun when no subcategory is set. */
    fun noun(g: Garment): String = g.subcategory.trim().ifEmpty { defaultNoun(g.category) }.lowercase()

    /** "olive chinos" — primary colour plus noun. */
    fun phrase(g: Garment): String {
        val n = noun(g)
        val c = g.colors.firstOrNull()?.label?.lowercase() ?: return n
        return if (n.contains(c)) n else "$c $n"
    }

    fun isPlural(noun: String): Boolean {
        val last = noun.substringAfterLast(' ')
        return last.endsWith("s") && !last.endsWith("ss")
    }

    /** Verb agreement: verb("sandals", "look", "looks"). */
    fun verb(noun: String, plural: String, singular: String) = if (isPlural(noun)) plural else singular

    fun cap(s: String): String = s.replaceFirstChar { it.uppercaseChar() }

    fun temp(celsius: Double): String = "${celsius.roundToInt()}°C"

    fun days(n: Int): String = if (n == 1) "1 day" else "$n days"

    fun occasionPhrase(o: Occasion): String = when (o) {
        Occasion.LOUNGE -> "lounging"
        Occasion.ACTIVE -> "an active day"
        Occasion.CASUAL -> "a casual day"
        Occasion.WORK -> "work"
        Occasion.SMART -> "a smart occasion"
        Occasion.DATE -> "date night"
        Occasion.FORMAL -> "a formal event"
        Occasion.TRAVEL -> "travel"
    }

    /** "Day 2" or "Days 1, 3 & 5". */
    fun dayList(days: List<Int>): String {
        if (days.size == 1) return "Day ${days[0]}"
        return "Days " + days.dropLast(1).joinToString(", ") + " & " + days.last()
    }
}

/** Garment shape heuristics shared by the evaluator, generator and packing planner. */
internal object Shapes {
    private val openShoeWords = listOf("sandal", "flip", "slide", "espadrille", "mule")

    fun isShortsLike(g: Garment): Boolean {
        val n = Words.noun(g)
        return when (g.category) {
            Category.BOTTOM -> n.contains("short")
            Category.ONE_PIECE -> n.contains("playsuit") || n.contains("romper")
            else -> false
        }
    }

    fun isOpenShoe(g: Garment): Boolean {
        if (g.category != Category.SHOES) return false
        val n = Words.noun(g)
        return g.warmth <= 1 || openShoeWords.any { n.contains(it) }
    }

    fun isSunglasses(g: Garment): Boolean = g.category == Category.ACCESSORY && Words.noun(g).contains("sunglass")

    /** Scarves, beanies, gloves — accessories that add warmth. */
    fun isWarmAccessory(g: Garment): Boolean = g.category == Category.ACCESSORY && g.warmth >= 4
}
