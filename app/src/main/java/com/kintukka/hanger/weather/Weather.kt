package com.kintukka.hanger.weather

import com.kintukka.hanger.data.repo.SettingsRepository
import com.kintukka.hanger.engine.WeatherCondition
import com.kintukka.hanger.engine.WeatherContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.time.LocalDate
import java.time.LocalDateTime

data class DayWeather(
    val date: LocalDate,
    val condition: WeatherCondition,
    val tempMinC: Double,
    val tempMaxC: Double,
    val precipitationProbability: Int,
    val windKph: Double,
    val uvIndex: Double,
    /** Conditions during the hours you're out (08:00–21:00), what the engine dresses for. */
    val wearing: WeatherContext,
)

data class Forecast(
    val fetchedAt: Long,
    val currentTempC: Double?,
    val currentCondition: WeatherCondition?,
    val days: List<DayWeather>,
) {
    fun day(date: LocalDate): DayWeather? = days.firstOrNull { it.date == date }
}

data class Place(val name: String, val region: String, val country: String, val latitude: Double, val longitude: Double) {
    val label: String get() = listOf(name, region.takeIf { it.isNotBlank() && it != name }, country).filterNotNull()
        .filter { it.isNotBlank() }.joinToString(", ")
}

/** Open-Meteo client: free, keyless, no account. */
class WeatherRepository(private val settings: SettingsRepository) {
    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }
    private val mutex = Mutex()
    private var memory: Pair<String, Forecast>? = null

    suspend fun forecast(latitude: Double, longitude: Double, force: Boolean = false): Forecast? = mutex.withLock {
        val key = "%.3f,%.3f".format(latitude, longitude)
        val now = System.currentTimeMillis()
        memory?.let { (k, f) -> if (!force && k == key && now - f.fetchedAt < CACHE_MS) return f }

        val cached = settings.weatherCache()?.let { runCatching { json.decodeFromString<CachedForecast>(it) }.getOrNull() }
            ?.takeIf { it.key == key }
        if (!force && cached != null && now - cached.fetchedAt < CACHE_MS) {
            return parse(cached.body, cached.fetchedAt).also { memory = key to it }
        }

        val fresh = runCatching { fetch(latitude, longitude) }.getOrNull()
        if (fresh != null) {
            settings.setWeatherCache(json.encodeToString(CachedForecast(key, now, fresh)))
            return parse(fresh, now).also { memory = key to it }
        }
        // Offline: fall back to the last forecast we had, even if stale.
        return cached?.let { parse(it.body, it.fetchedAt) }?.also { memory = key to it }
    }

    suspend fun searchPlaces(query: String): List<Place> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        val url = "https://geocoding-api.open-meteo.com/v1/search?count=8&language=en&format=json&name=" +
            URLEncoder.encode(query.trim(), "UTF-8")
        val body = runCatching { get(url) }.getOrNull() ?: return@withContext emptyList()
        val parsed = runCatching { json.decodeFromString<GeoResponse>(body) }.getOrNull() ?: return@withContext emptyList()
        parsed.results.orEmpty().map { Place(it.name, it.admin1.orEmpty(), it.country.orEmpty(), it.latitude, it.longitude) }
    }

    private suspend fun fetch(latitude: Double, longitude: Double): String = withContext(Dispatchers.IO) {
        val url = "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude" +
            "&current=temperature_2m,weather_code" +
            "&hourly=temperature_2m,apparent_temperature,precipitation_probability,weather_code,wind_speed_10m" +
            "&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max,wind_speed_10m_max,uv_index_max" +
            "&timezone=auto&forecast_days=16"
        get(url)
    }

    private fun get(url: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connectTimeout = 10_000
        connection.readTimeout = 15_000
        connection.setRequestProperty("User-Agent", "Hanger/1.0 (personal wardrobe app)")
        try {
            if (connection.responseCode !in 200..299) error("HTTP ${connection.responseCode}")
            return connection.inputStream.bufferedReader().use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }

    internal fun parse(body: String, fetchedAt: Long): Forecast {
        val r = json.decodeFromString<ForecastResponse>(body)
        val hourly = r.hourly
        val hours = hourly?.time.orEmpty().mapIndexed { i, t -> i to LocalDateTime.parse(t) }
        val days = r.daily?.time.orEmpty().mapIndexed { i, t ->
            val date = LocalDate.parse(t)
            val d = r.daily!!
            val window = hours.filter { (_, dt) -> dt.toLocalDate() == date && dt.hour in 8..21 }.map { it.first }
            val temps = window.mapNotNull { hourly?.temperature?.getOrNull(it) }
            val feels = window.mapNotNull { hourly?.apparent?.getOrNull(it) }
            val precip = window.mapNotNull { hourly?.precipitation?.getOrNull(it) }
            val wind = window.mapNotNull { hourly?.wind?.getOrNull(it) }
            val dayMin = d.tempMin?.getOrNull(i) ?: temps.minOrNull() ?: 15.0
            val dayMax = d.tempMax?.getOrNull(i) ?: temps.maxOrNull() ?: 15.0
            val condition = conditionFor(d.code?.getOrNull(i) ?: 0)
            val feelsLike = if (feels.isEmpty()) null else feels.min() * 0.6 + feels.average() * 0.4
            DayWeather(
                date = date,
                condition = condition,
                tempMinC = dayMin,
                tempMaxC = dayMax,
                precipitationProbability = d.precipitation?.getOrNull(i) ?: precip.maxOrNull() ?: 0,
                windKph = d.wind?.getOrNull(i) ?: 0.0,
                uvIndex = d.uv?.getOrNull(i) ?: 0.0,
                wearing = WeatherContext(
                    tempMinC = temps.minOrNull() ?: dayMin,
                    tempMaxC = temps.maxOrNull() ?: dayMax,
                    feelsLikeC = feelsLike,
                    precipitationProbability = precip.maxOrNull() ?: d.precipitation?.getOrNull(i) ?: 0,
                    windKph = wind.maxOrNull() ?: d.wind?.getOrNull(i) ?: 0.0,
                    condition = condition,
                    uvIndex = d.uv?.getOrNull(i) ?: 0.0,
                ),
            )
        }
        return Forecast(
            fetchedAt = fetchedAt,
            currentTempC = r.current?.temperature,
            currentCondition = r.current?.code?.let(::conditionFor),
            days = days,
        )
    }

    companion object {
        private const val CACHE_MS = 60 * 60 * 1000L

        /** WMO weather interpretation codes. */
        fun conditionFor(code: Int): WeatherCondition = when (code) {
            0 -> WeatherCondition.CLEAR
            1, 2 -> WeatherCondition.PARTLY_CLOUDY
            3 -> WeatherCondition.CLOUDY
            45, 48 -> WeatherCondition.FOG
            in 51..57 -> WeatherCondition.DRIZZLE
            in 61..67, in 80..82 -> WeatherCondition.RAIN
            in 71..77, 85, 86 -> WeatherCondition.SNOW
            in 95..99 -> WeatherCondition.STORM
            else -> WeatherCondition.CLOUDY
        }
    }
}

@Serializable
private data class CachedForecast(val key: String, val fetchedAt: Long, val body: String)

@Serializable
internal data class ForecastResponse(
    val current: Current? = null,
    val hourly: Hourly? = null,
    val daily: Daily? = null,
) {
    @Serializable
    data class Current(
        @SerialName("temperature_2m") val temperature: Double? = null,
        @SerialName("weather_code") val code: Int? = null,
    )

    @Serializable
    data class Hourly(
        val time: List<String> = emptyList(),
        @SerialName("temperature_2m") val temperature: List<Double?> = emptyList(),
        @SerialName("apparent_temperature") val apparent: List<Double?> = emptyList(),
        @SerialName("precipitation_probability") val precipitation: List<Int?> = emptyList(),
        @SerialName("wind_speed_10m") val wind: List<Double?> = emptyList(),
    )

    @Serializable
    data class Daily(
        val time: List<String> = emptyList(),
        @SerialName("weather_code") val code: List<Int?>? = null,
        @SerialName("temperature_2m_max") val tempMax: List<Double?>? = null,
        @SerialName("temperature_2m_min") val tempMin: List<Double?>? = null,
        @SerialName("precipitation_probability_max") val precipitation: List<Int?>? = null,
        @SerialName("wind_speed_10m_max") val wind: List<Double?>? = null,
        @SerialName("uv_index_max") val uv: List<Double?>? = null,
    )
}

@Serializable
private data class GeoResponse(val results: List<GeoResult>? = null)

@Serializable
private data class GeoResult(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String? = null,
    val admin1: String? = null,
)
