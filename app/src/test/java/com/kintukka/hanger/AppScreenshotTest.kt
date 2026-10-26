package com.kintukka.hanger

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import com.kintukka.hanger.ui.navigation.HangerNavHost
import com.kintukka.hanger.ui.theme.HangerTheme
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.hasScrollAction
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kintukka.hanger.ui.components.ImageLoading
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import com.kintukka.hanger.demo.DemoCloset
import com.kintukka.hanger.weather.WeatherRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.shadows.ShadowLooper

/**
 * Drives the real app (real Room database, real engine, demo closet, canned forecast) through
 * every main screen and records screenshots into app/screenshots/.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = RobolectricDeviceQualifiers.Pixel7)
class AppScreenshotTest {

    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    private val app get() = ApplicationProvider.getApplicationContext<HangerApp>()

    @Before
    fun fastImages() {
        ImageLoading.synchronous = true
    }

    private fun settle() {
        repeat(4) {
            // Advance the paused main looper so delayed work (debounced evaluations) runs too.
            ShadowLooper.idleMainLooper(400, java.util.concurrent.TimeUnit.MILLISECONDS)
            compose.waitForIdle()
            Thread.sleep(100)
        }
    }

    private fun shot(name: String) {
        settle()
        compose.onRoot().captureRoboImage("screenshots/$name.png")
    }

    private fun tab(label: String) = compose.onNodeWithTag("tab-$label").performClick().also { settle() }

    private fun clickText(text: String): SemanticsNodeInteraction {
        if (compose.onAllNodesWithText(text).fetchSemanticsNodes().isEmpty()) {
            compose.onAllNodes(hasScrollAction()).onFirst().performScrollToNode(hasText(text))
            settle()
        }
        return compose.onAllNodesWithText(text).onFirst().performClick().also { settle() }
    }

    private fun scrollTop() {
        compose.onAllNodes(hasScrollAction()).onFirst().performScrollToIndex(0)
        settle()
    }

    private fun back() {
        compose.activityRule.scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
        settle()
    }

    private fun seed() = runBlocking {
        val c = app.container
        c.settings.setLocation("London", 51.5072, -0.1276)
        c.settings.setWeatherCache(
            WeatherRepository.cacheEntry(51.5072, -0.1276, System.currentTimeMillis(), TestFixtures.londonForecast()),
        )
        DemoCloset.load(c.db, c.images)
    }

    @Test
    fun walkThroughEveryScreen() {
        shot("00_today_empty")
        seed()
        // Re-open Today so it picks up the new location/forecast.
        tab("Closet"); tab("Today")
        shot("01_today")
        compose.onAllNodes(hasScrollAction()).onFirst().performScrollToNode(hasText("Dress me"))
        shot("02_today_ideas")

        tab("Closet")
        shot("03_closet")
        clickText("Breton stripe top")
        shot("04_item_detail")
        compose.onNodeWithContentDescription("Edit").performClick(); settle()
        shot("05_item_edit")
        back(); back()

        compose.onNodeWithText("Add", useUnmergedTree = true).performClick(); settle()
        shot("06_add_sheet")
        back()

        tab("Outfits")
        shot("07_outfits")
        clickText("Weekend classic")
        shot("08_outfit_detail")
        compose.onNodeWithContentDescription("Edit outfit").performClick(); settle()
        shot("09_builder")
        back(); back()

        scrollTop()
        compose.onNode(hasText("Dress me") and hasClickAction()).performClick(); settle()
        shot("10_shuffle")
        back()

        scrollTop()
        compose.onNode(hasText("Ideas") and hasClickAction()).performClick(); settle()
        shot("11_ideas")
        back()

        scrollTop()
        clickText("Capsules")
        shot("12_capsules")

        tab("Plan")
        shot("13_planner")
        clickText("Trips")
        shot("14_trips")

        tab("Insights")
        shot("15_insights")
        compose.onAllNodes(hasScrollAction()).onFirst().performScrollToNode(hasText("Most worn"))
        shot("16_insights_more")

        tab("Today")
        scrollTop()
        compose.onNodeWithContentDescription("Settings").performClick(); settle()
        shot("17_settings")
    }
}

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = RobolectricDeviceQualifiers.Pixel7)
class DarkModeScreenshotTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun darkTodayAndCloset() {
        val app = ApplicationProvider.getApplicationContext<HangerApp>()
        ImageLoading.synchronous = true
        runBlocking {
            app.container.settings.setLocation("London", 51.5072, -0.1276)
            app.container.settings.setWeatherCache(
                WeatherRepository.cacheEntry(51.5072, -0.1276, System.currentTimeMillis(), TestFixtures.londonForecast()),
            )
            DemoCloset.load(app.container.db, app.container.images)
        }
        compose.setContent { HangerTheme(darkTheme = true) { HangerNavHost() } }
        fun settle() = repeat(4) { ShadowLooper.idleMainLooper(400, java.util.concurrent.TimeUnit.MILLISECONDS); compose.waitForIdle(); Thread.sleep(100) }
        settle()
        compose.onRoot().captureRoboImage("screenshots/dark_today.png")
        compose.onNodeWithTag("tab-Closet").performClick(); settle()
        compose.onRoot().captureRoboImage("screenshots/dark_closet.png")
        compose.onNodeWithTag("tab-Outfits").performClick(); settle()
        compose.onRoot().captureRoboImage("screenshots/dark_outfits.png")
    }
}
