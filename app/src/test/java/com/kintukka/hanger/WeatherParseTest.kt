package com.kintukka.hanger

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kintukka.hanger.data.repo.SettingsRepository
import com.kintukka.hanger.engine.WeatherCondition
import com.kintukka.hanger.weather.WeatherRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class WeatherParseTest {
    private val repo = WeatherRepository(SettingsRepository(ApplicationProvider.getApplicationContext()))

    @Test
    fun parsesSixteenDaysWithWearingWindow() {
        val today = LocalDate.now()
        val forecast = repo.parse(TestFixtures.londonForecast(today), System.currentTimeMillis())
        assertEquals(16, forecast.days.size)
        val day = forecast.day(today)!!
        assertTrue("min <= max", day.tempMinC <= day.tempMaxC)
        // The wearing window (08–21h) sits inside the daily range, give or take rounding.
        assertTrue(day.wearing.tempMinC >= day.tempMinC - 0.5)
        assertTrue(day.wearing.tempMaxC <= day.tempMaxC + 0.5)
        assertNotNull(day.wearing.feelsLikeC)
        assertNotNull(forecast.currentTempC)
    }

    @Test
    fun mapsWmoCodes() {
        assertEquals(WeatherCondition.CLEAR, WeatherRepository.conditionFor(0))
        assertEquals(WeatherCondition.RAIN, WeatherRepository.conditionFor(61))
        assertEquals(WeatherCondition.DRIZZLE, WeatherRepository.conditionFor(55))
        assertEquals(WeatherCondition.SNOW, WeatherRepository.conditionFor(73))
        assertEquals(WeatherCondition.STORM, WeatherRepository.conditionFor(95))
    }
}
