package com.kintukka.hanger.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kintukka.hanger.ui.closet.AddItemsScreen
import com.kintukka.hanger.ui.closet.ClosetScreen
import com.kintukka.hanger.ui.closet.EditItemScreen
import com.kintukka.hanger.ui.closet.ItemDetailScreen
import com.kintukka.hanger.ui.insights.InsightsScreen
import com.kintukka.hanger.ui.outfits.CapsuleScreen
import com.kintukka.hanger.ui.outfits.OutfitBuilderScreen
import com.kintukka.hanger.ui.outfits.OutfitDetailScreen
import com.kintukka.hanger.ui.outfits.OutfitsScreen
import com.kintukka.hanger.ui.outfits.ShuffleScreen
import com.kintukka.hanger.ui.outfits.SuggestScreen
import com.kintukka.hanger.ui.planner.DayScreen
import com.kintukka.hanger.ui.planner.PlannerScreen
import com.kintukka.hanger.ui.planner.TripEditScreen
import com.kintukka.hanger.ui.planner.TripScreen
import com.kintukka.hanger.ui.settings.SettingsScreen
import com.kintukka.hanger.ui.today.TodayScreen
import kotlinx.serialization.Serializable
import java.time.LocalDate
import kotlin.reflect.KClass

const val NO_DATE = Long.MIN_VALUE

@Serializable object TodayRoute
@Serializable object ClosetRoute
@Serializable object OutfitsRoute
@Serializable object PlannerRoute
@Serializable object InsightsRoute
@Serializable object SettingsRoute
@Serializable object AddItemsRoute
@Serializable data class ItemRoute(val id: Long)
@Serializable data class EditItemRoute(val id: Long)
@Serializable data class OutfitRoute(val id: Long)
@Serializable data class CapsuleRoute(val id: Long)
@Serializable data class DayRoute(val dateEpoch: Long)
@Serializable data class TripRoute(val id: Long)
@Serializable data class TripEditRoute(val id: Long = 0)

/** Canvas builder. Prefill with an existing outfit, or a list of items, optionally for a calendar day. */
@Serializable data class BuilderRoute(
    val outfitId: Long = 0,
    val dateEpoch: Long = NO_DATE,
    val itemIds: String = "",
)

@Serializable data class ShuffleRoute(val dateEpoch: Long = NO_DATE)

/** Generated suggestions, optionally around one anchor item and/or for a calendar day. */
@Serializable data class SuggestRoute(val dateEpoch: Long = NO_DATE, val anchorItemId: Long = 0)

fun Long.toDateOrNull(): LocalDate? = if (this == NO_DATE) null else LocalDate.ofEpochDay(this)
fun LocalDate?.toEpochOrNone(): Long = this?.toEpochDay() ?: NO_DATE

private data class Tab(val route: Any, val routeClass: KClass<*>, val label: String, val icon: ImageVector, val selectedIcon: ImageVector)

private val tabs = listOf(
    Tab(TodayRoute, TodayRoute::class, "Today", Icons.Outlined.WbSunny, Icons.Filled.WbSunny),
    Tab(ClosetRoute, ClosetRoute::class, "Closet", Icons.Outlined.Checkroom, Icons.Filled.Checkroom),
    Tab(OutfitsRoute, OutfitsRoute::class, "Outfits", Icons.Outlined.Style, Icons.Filled.Style),
    Tab(PlannerRoute, PlannerRoute::class, "Plan", Icons.Outlined.CalendarMonth, Icons.Filled.CalendarMonth),
    Tab(InsightsRoute, InsightsRoute::class, "Insights", Icons.Outlined.Insights, Icons.Filled.Insights),
)

@Composable
fun HangerNavHost(navController: NavHostController = rememberNavController()) {
    val backStack by navController.currentBackStackEntryAsState()
    val destination = backStack?.destination
    val onTopLevel = tabs.any { tab -> destination?.hierarchy?.any { it.hasRoute(tab.routeClass) } == true }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (onTopLevel) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainerLow) {
                    tabs.forEach { tab ->
                        val selected = destination?.hierarchy?.any { it.hasRoute(tab.routeClass) } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(if (selected) tab.selectedIcon else tab.icon, contentDescription = null) },
                            label = { Text(tab.label) },
                            modifier = Modifier.testTag("tab-${tab.label}"),
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            ),
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = TodayRoute,
            modifier = Modifier.padding(padding),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
        ) {
            composable<TodayRoute> { TodayScreen(navController) }
            composable<ClosetRoute> { ClosetScreen(navController) }
            composable<OutfitsRoute> { OutfitsScreen(navController) }
            composable<PlannerRoute> { PlannerScreen(navController) }
            composable<InsightsRoute> { InsightsScreen(navController) }
            composable<SettingsRoute> { SettingsScreen(navController) }
            composable<AddItemsRoute> { AddItemsScreen(navController) }
            composable<ItemRoute> { ItemDetailScreen(navController, it.toRoute<ItemRoute>().id) }
            composable<EditItemRoute> { EditItemScreen(navController, it.toRoute<EditItemRoute>().id) }
            composable<OutfitRoute> { OutfitDetailScreen(navController, it.toRoute<OutfitRoute>().id) }
            composable<CapsuleRoute> { CapsuleScreen(navController, it.toRoute<CapsuleRoute>().id) }
            composable<DayRoute> { DayScreen(navController, LocalDate.ofEpochDay(it.toRoute<DayRoute>().dateEpoch)) }
            composable<TripRoute> { TripScreen(navController, it.toRoute<TripRoute>().id) }
            composable<TripEditRoute> { TripEditScreen(navController, it.toRoute<TripEditRoute>().id) }
            composable<BuilderRoute>(
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
            ) {
                val r = it.toRoute<BuilderRoute>()
                OutfitBuilderScreen(
                    navController,
                    outfitId = r.outfitId,
                    date = r.dateEpoch.toDateOrNull(),
                    initialItemIds = r.itemIds.split(",").mapNotNull(String::toLongOrNull),
                )
            }
            composable<ShuffleRoute> { ShuffleScreen(navController, it.toRoute<ShuffleRoute>().dateEpoch.toDateOrNull()) }
            composable<SuggestRoute> {
                val r = it.toRoute<SuggestRoute>()
                SuggestScreen(navController, r.dateEpoch.toDateOrNull(), r.anchorItemId.takeIf { id -> id > 0 })
            }
        }
    }
}
