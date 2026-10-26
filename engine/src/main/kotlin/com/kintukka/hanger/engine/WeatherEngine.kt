package com.kintukka.hanger.engine

import java.time.LocalDate

internal object SeasonMath {
    fun forDate(date: LocalDate, hemisphere: Hemisphere): Season {
        val north = when (date.monthValue) {
            12, 1, 2 -> Season.WINTER
            3, 4, 5 -> Season.SPRING
            6, 7, 8 -> Season.SUMMER
            else -> Season.AUTUMN
        }
        if (hemisphere == Hemisphere.NORTH) return north
        return when (north) {
            Season.WINTER -> Season.SUMMER
            Season.SUMMER -> Season.WINTER
            Season.SPRING -> Season.AUTUMN
            Season.AUTUMN -> Season.SPRING
        }
    }
}

/** Weather plus the derived needs and effective temperature, computed once per request. */
internal class WeatherCtx(val weather: WeatherContext, val needs: WeatherNeeds, val effective: Double) {
    val isSunny: Boolean
        get() = weather.uvIndex >= 5.0 ||
            (weather.condition in setOf(WeatherCondition.CLEAR, WeatherCondition.PARTLY_CLOUDY) && effective >= 15.0)

    val isGloomy: Boolean
        get() = weather.condition in setOf(
            WeatherCondition.RAIN, WeatherCondition.STORM, WeatherCondition.SNOW,
            WeatherCondition.DRIZZLE, WeatherCondition.FOG,
        ) || effective < 8.0

    companion object {
        fun of(w: WeatherContext) = WeatherCtx(w, WeatherMath.needs(w), WeatherMath.effective(w))
    }
}

internal object WeatherMath {
    private enum class Band(val target: Double, val word: String) {
        HOT(1.3, "Hot"), WARM(1.8, "Warm"), MILD(2.4, "Mild"), COOL(3.0, "Cool"),
        CHILLY(3.6, "Chilly"), COLD(4.2, "Cold"), FREEZING(4.7, "Freezing"),
    }

    /** Feels-like if known, else the day's range weighted towards the cold end. */
    fun effective(w: WeatherContext): Double {
        w.feelsLikeC?.let { return it }
        val lo = minOf(w.tempMinC, w.tempMaxC)
        val hi = maxOf(w.tempMinC, w.tempMaxC)
        return 0.6 * lo + 0.4 * hi
    }

    private fun band(t: Double): Band = when {
        t >= 27.0 -> Band.HOT
        t >= 22.0 -> Band.WARM
        t >= 17.0 -> Band.MILD
        t >= 12.0 -> Band.COOL
        t >= 6.0 -> Band.CHILLY
        t >= 0.0 -> Band.COLD
        else -> Band.FREEZING
    }

    fun wantsWaterproof(w: WeatherContext): Boolean = when (w.condition) {
        WeatherCondition.RAIN, WeatherCondition.STORM, WeatherCondition.SNOW -> true
        WeatherCondition.DRIZZLE -> w.precipitationProbability >= 40
        else -> w.precipitationProbability >= 50
    }

    fun needs(w: WeatherContext): WeatherNeeds {
        val t = effective(w)
        val band = band(t)
        val minTemp = minOf(w.tempMinC, w.tempMaxC)
        val windy = w.windKph >= 30.0
        val target = (band.target + if (windy) 0.3 else 0.0).coerceIn(1.0, 5.0)
        val needsOuterwear = band >= Band.CHILLY
        val suggestsLayer = when (band) {
            Band.HOT, Band.WARM -> false
            Band.MILD -> minTemp < 15.0
            Band.COOL -> true
            Band.CHILLY -> t < 9.0
            Band.COLD, Band.FREEZING -> true
        }
        val allowsShorts = band <= Band.WARM
        val waterproof = wantsWaterproof(w)
        return WeatherNeeds(
            targetWarmth = target,
            needsOuterwear = needsOuterwear,
            suggestsLayer = suggestsLayer,
            allowsShortsAndOpenShoes = allowsShorts,
            wantsWaterproof = waterproof,
            summary = summary(w, band, suggestsLayer, waterproof, windy),
        )
    }

    private fun summary(w: WeatherContext, band: Band, layer: Boolean, waterproof: Boolean, windy: Boolean): String {
        val sky = when {
            w.condition == WeatherCondition.STORM -> " with storms about"
            w.condition == WeatherCondition.SNOW -> " with snow"
            w.condition == WeatherCondition.RAIN || (waterproof && w.condition != WeatherCondition.DRIZZLE) -> " with showers likely"
            w.condition == WeatherCondition.DRIZZLE -> " with drizzle"
            w.condition == WeatherCondition.FOG -> " and foggy"
            windy -> " and windy"
            w.uvIndex >= 6.0 || (w.condition == WeatherCondition.CLEAR && band <= Band.MILD) -> " and sunny"
            else -> ""
        }
        val advice = when (band) {
            Band.HOT -> "keep it light and breathable"
            Band.WARM -> "light fabrics and open shoes are fine"
            Band.MILD -> if (layer) "bring a light layer" else "easy dressing today"
            Band.COOL -> "bring a light layer"
            Band.CHILLY -> "wear a jacket"
            Band.COLD -> "wear a warm coat"
            Band.FREEZING -> "bundle up in your warmest coat"
        }
        val rain = if (waterproof) " and something waterproof" else ""
        val sun = if (band <= Band.WARM && w.uvIndex >= 6.0 && !waterproof) ", plus sunscreen" else ""
        return "${band.word}$sky — $advice$rain$sun."
    }
}
