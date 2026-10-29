package com.kintukka.hanger.data.repo

import com.kintukka.hanger.data.Item
import com.kintukka.hanger.engine.Garment
import com.kintukka.hanger.engine.Occasion
import com.kintukka.hanger.engine.OutfitEvaluation
import com.kintukka.hanger.engine.OutfitEvaluator
import com.kintukka.hanger.engine.OutfitGenerator
import com.kintukka.hanger.engine.PackingPlan
import com.kintukka.hanger.engine.PackingPlanner
import com.kintukka.hanger.engine.StylingRequest
import com.kintukka.hanger.engine.SuggestedOutfit
import com.kintukka.hanger.engine.TripSpec
import com.kintukka.hanger.engine.WeatherContext
import com.kintukka.hanger.weather.DayWeather
import com.kintukka.hanger.weather.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.time.LocalDate

/** Glue between stored data, live weather and the pure styling engine. */
class Stylist(
    private val repo: WardrobeRepository,
    private val weather: WeatherRepository,
    private val settings: SettingsRepository,
) {
    suspend fun weatherFor(date: LocalDate, force: Boolean = false): DayWeather? {
        val s = settings.current()
        val lat = s.latitude ?: return null
        val lon = s.longitude ?: return null
        return weather.forecast(lat, lon, force)?.day(date)
    }

    suspend fun weatherRange(lat: Double?, lon: Double?, start: LocalDate, end: LocalDate): List<WeatherContext> {
        if (lat == null || lon == null) return emptyList()
        val forecast = weather.forecast(lat, lon) ?: return emptyList()
        val days = generateSequence(start) { it.plusDays(1) }.takeWhile { !it.isAfter(end) }.toList()
        val contexts = days.map { forecast.day(it)?.wearing }
        // Only useful when we have the whole trip; otherwise let the engine assume mild weather.
        return if (contexts.all { it != null }) contexts.filterNotNull() else emptyList()
    }

    private suspend fun garments(): List<Garment> = repo.snapshotCloset().map(Item::toGarment)

    suspend fun suggest(
        date: LocalDate,
        occasion: Occasion,
        seed: Long,
        anchors: Set<Long> = emptySet(),
        count: Int = 3,
        weatherContext: WeatherContext? = null,
    ): List<SuggestedOutfit> {
        val closet = garments()
        val wx = weatherContext ?: weatherFor(date)?.wearing
        val recent = repo.snapshotDayEntries()
            .filter { !it.date.isBefore(date.minusDays(14)) && !it.date.isAfter(date) }
            .map { com.kintukka.hanger.engine.OutfitSignature.of(it.itemIds) }
            .toSet()
        val affinity = settings.affinity.first()
        val s = settings.current()
        return withContext(Dispatchers.Default) {
            runCatching {
                OutfitGenerator.suggest(
                    closet,
                    StylingRequest(
                        date = date,
                        occasion = occasion,
                        weather = wx,
                        hemisphere = s.hemisphere,
                        anchorItemIds = anchors,
                        count = count,
                        recentSignatures = recent,
                        seed = seed,
                    ),
                    affinity,
                )
            }.getOrDefault(emptyList())
        }
    }

    suspend fun evaluate(items: List<Item>, date: LocalDate, occasion: Occasion?): OutfitEvaluation? {
        if (items.isEmpty()) return null
        val wx = weatherFor(date)?.wearing
        val affinity = settings.affinity.first()
        return withContext(Dispatchers.Default) {
            runCatching { OutfitEvaluator.evaluate(items.map(Item::toGarment), wx, occasion, affinity, date) }.getOrNull()
        }
    }

    suspend fun packingPlan(spec: TripSpec): PackingPlan = withContext(Dispatchers.Default) {
        PackingPlanner.plan(garments(), spec)
    }
}
