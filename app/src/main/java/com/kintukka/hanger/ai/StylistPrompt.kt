package com.kintukka.hanger.ai

import com.kintukka.hanger.data.Item
import com.kintukka.hanger.data.repo.Settings
import com.kintukka.hanger.engine.Formality
import com.kintukka.hanger.engine.GarmentStatus
import com.kintukka.hanger.ui.longLabel
import com.kintukka.hanger.weather.DayWeather
import java.time.LocalDate
import kotlin.math.roundToInt

/** Builds the stylist's fixed context: who it's styling, the date and weather, and the whole wardrobe. */
object StylistPrompt {

    fun build(items: List<Item>, settings: Settings, today: LocalDate, weather: DayWeather?): String = buildString {
        appendLine("You are the personal stylist inside Hanger, a wardrobe app used by one person. You can see their entire wardrobe below; every piece has a numeric id.")
        appendLine()
        appendLine("How to help:")
        appendLine("- Build outfits only from the pieces listed, referring to them by id in `outfits`. Every outfit needs shoes plus either a top and a bottom or a one-piece; add a layer, outerwear, a bag or an accessory when it helps.")
        appendLine("- Prefer pieces marked clean. Never use pieces marked away. If the ideal piece is in the laundry, say so in the reply and offer an alternative.")
        appendLine("- Dress for the weather and the occasion. Favour pieces that haven't been worn for a while when they work just as well.")
        appendLine("- Keep `reply` short, warm and specific: under 120 words, plain sentences, naming pieces the way the owner does. Explain the idea behind a look in its `why` (one sentence).")
        appendLine("- Return 0 to 3 outfits. Return none when the question isn't asking for a look (care questions, shopping advice, general style questions).")
        appendLine("- You may recommend things to buy in the reply text, but outfits only ever contain pieces from this wardrobe.")
        appendLine()
        appendLine("Context:")
        appendLine("- Today is ${today.longLabel()}.")
        settings.locationName?.let { appendLine("- Location: $it.") }
        weather?.let { w ->
            appendLine(
                "- Weather today: ${w.condition.label.lowercase()}, ${w.tempMinC.roundToInt()}–${w.tempMaxC.roundToInt()}°C, " +
                    "${w.precipitationProbability}% chance of rain, wind up to ${w.windKph.roundToInt()} km/h.",
            )
        }
        appendLine("- The owner shows temperatures in ${if (settings.tempUnit.name == "FAHRENHEIT") "°F" else "°C"} and prices in ${settings.currency}.")
        appendLine()
        appendLine("Wardrobe (id | name | type | colours | pattern | formality 1–5 | warmth 1–5 | seasons | state | times worn | last worn):")
        items.sortedWith(compareBy({ it.category.order }, { it.name })).forEach { item ->
            val e = item.entity
            val state = when (e.status) {
                GarmentStatus.CLEAN -> "clean"
                GarmentStatus.DIRTY -> "in laundry"
                GarmentStatus.AWAY -> "away"
            }
            val type = listOf(e.category.label, e.subcategory).filter { it.isNotBlank() }.joinToString("/")
            val colours = e.colors.joinToString("+") { it.label.lowercase() }.ifBlank { "unknown" }
            val seasons = if (e.seasons.isEmpty()) "all year" else e.seasons.sorted().joinToString("/") { it.label.lowercase() }
            appendLine(
                "${e.id} | ${e.name} | $type | $colours | ${e.pattern.label.lowercase()} | ${e.formality} (${Formality.label(e.formality).lowercase()}) | " +
                    "${e.warmth} | $seasons | $state${if (e.favorite) ", favourite" else ""} | ${item.wears} | ${item.lastWorn?.toString() ?: "never"}",
            )
        }
    }
}
