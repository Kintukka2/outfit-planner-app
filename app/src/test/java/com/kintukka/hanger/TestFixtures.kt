package com.kintukka.hanger

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object TestFixtures {
    private const val FIXTURE_START = "2026-10-26"

    /** Real Open-Meteo response for London, with dates shifted so day 0 is [today]. */
    fun londonForecast(today: LocalDate = LocalDate.now()): String {
        val raw = javaClass.classLoader!!.getResource("forecast_london.json")!!.readText()
        val shift = ChronoUnit.DAYS.between(LocalDate.parse(FIXTURE_START), today)
        if (shift == 0L) return raw
        return Regex("\"(\\d{4}-\\d{2}-\\d{2})(T\\d{2}:\\d{2})?\"").replace(raw) { m ->
            val date = LocalDate.parse(m.groupValues[1]).plusDays(shift)
            "\"$date${m.groupValues[2]}\""
        }
    }
}
