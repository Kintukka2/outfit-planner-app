package com.kintukka.hanger.ui.insights

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.kintukka.hanger.ui.components.EmptyState
import com.kintukka.hanger.ui.components.ItemTile
import com.kintukka.hanger.ui.components.PageTitle
import com.kintukka.hanger.ui.components.SectionHeader
import com.kintukka.hanger.ui.components.StatPill
import com.kintukka.hanger.ui.formatMoney
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.navigation.ClosetRoute
import com.kintukka.hanger.ui.navigation.ItemRoute
import com.kintukka.hanger.ui.navigation.SettingsRoute
import com.kintukka.hanger.ui.navigation.SuggestRoute
import com.kintukka.hanger.ui.plural
import java.text.NumberFormat
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt

private val PagePadding = 20.dp
private val TileWidth = 96.dp

@Composable
fun InsightsScreen(navController: NavHostController) {
    val vm = hangerViewModel { c -> InsightsViewModel(c.repo, c.settings) }
    val state by vm.state.collectAsStateWithLifecycle()

    InsightsContent(
        state = state,
        onOpenItem = { id -> navController.navigate(ItemRoute(id)) },
        onStyleItem = { id -> navController.navigate(SuggestRoute(anchorItemId = id)) },
        onOpenCloset = {
            navController.navigate(ClosetRoute) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
        onOpenSettings = { navController.navigate(SettingsRoute) },
    )
}

/** Stateless page body, driven entirely by [state]. */
@Composable
internal fun InsightsContent(
    state: InsightsUiState,
    onOpenItem: (Long) -> Unit,
    onStyleItem: (Long) -> Unit,
    onOpenCloset: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val statusBar = WindowInsets.statusBars.asPaddingValues()
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = statusBar.calculateTopPadding() + 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item(key = "title") {
            PageTitle(
                "Insights",
                subtitle = "Your wardrobe, by the numbers",
                modifier = Modifier.padding(horizontal = PagePadding),
                trailing = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                },
            )
        }

        when {
            state.loading -> item(key = "loading") {
                Box(Modifier.fillMaxWidth().padding(vertical = 64.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.isEmpty -> item(key = "empty") {
                EmptyState(
                    icon = Icons.Outlined.Insights,
                    title = "Nothing to count yet",
                    body = "Add a few pieces to your closet and log what you wear. Your stats, streaks and forgotten favourites will show up here.",
                    action = {
                        Button(onClick = onOpenCloset, modifier = Modifier.heightIn(min = 48.dp)) { Text("Go to your closet") }
                    },
                )
            }
            else -> insightsContent(state, onOpenItem, onStyleItem)
        }
    }
}

private fun LazyListScope.insightsContent(
    state: InsightsUiState,
    openItem: (Long) -> Unit,
    styleItem: (Long) -> Unit,
) {
    item(key = "stats") { StatGrid(state, Modifier.padding(horizontal = PagePadding)) }

    state.streaks?.let { streaks ->
        item(key = "streak") { StreakCard(streaks, Modifier.padding(horizontal = PagePadding)) }
    }

    if (state.months.isNotEmpty()) {
        item(key = "chart") { MonthlySection(state.months, Modifier.padding(horizontal = PagePadding)) }
    }

    if (state.categories.isNotEmpty() || state.colors.isNotEmpty()) {
        item(key = "closet-mix") { ClosetMixSection(state, Modifier.padding(horizontal = PagePadding)) }
    }

    itemRow("most-worn", "Most worn", "Your hardest-working pieces.", state.mostWorn, openItem)
    itemRow("hidden-gems", "Hidden gems", "Worn only now and then. Worth another look.", state.hiddenGems, openItem)
    itemRow("never-worn", "Never worn", "Still waiting for their first outing.", state.neverWorn, openItem)
    itemRow("best-cpw", "Best cost per wear", "Price divided by wears: money well spent.", state.bestCostPerWear, openItem)
    itemRow("worst-cpw", "Worst cost per wear", "Wear these more and the number drops.", state.worstCostPerWear, openItem)

    if (state.forgotten.isNotEmpty()) {
        item(key = "forgotten-header") {
            SectionIntro(
                "Forgotten",
                "Not worn in two months or more. Tap one to see outfits built around it.",
            )
        }
        item(key = "forgotten-row") {
            LazyRow(
                contentPadding = PaddingValues(horizontal = PagePadding),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.forgotten, key = { it.item.id }) { ranked ->
                    ForgottenTile(ranked, onClick = { styleItem(ranked.item.id) })
                }
            }
        }
    }

    state.outfitCount?.let { count ->
        item(key = "closet-power") { ClosetPowerCard(count, Modifier.padding(horizontal = PagePadding)) }
    }

    if (state.outfitCount != null || state.wishlist.isNotEmpty()) {
        item(key = "wishlist-header") {
            SectionIntro(
                "Wishlist impact",
                when {
                    state.wishlist.isEmpty() ->
                        "Add pieces you're considering to your wishlist to see how many new outfits each one would unlock."
                    state.wishlist.all { it.impact == null } -> "Pieces you're thinking about adding."
                    else -> "How many new combinations each piece would unlock with what you already own."
                },
            )
        }
        items(state.wishlist, key = { "wish-${it.item.id}" }) { row ->
            WishlistImpactRow(row, onClick = { openItem(row.item.id) }, modifier = Modifier.padding(horizontal = PagePadding))
        }
    }
}

// ---- Top stats ----

@Composable
private fun StatGrid(state: InsightsUiState, modifier: Modifier = Modifier) {
    val value = state.closetValue?.let { formatMoney(it, state.currency) } ?: "—"
    val cpw = state.averageCostPerWear?.let { formatMoney(it, state.currency) } ?: "—"
    val utilisation = state.utilisation90?.let { "${(it * 100).roundToInt()}%" } ?: "—"
    Column(modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatPill("Items", state.itemCount.toString(), Modifier.weight(1f))
            StatPill("Closet value", value, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatPill("Avg cost per wear", cpw, Modifier.weight(1f))
            StatPill("Worn in 90 days", utilisation, Modifier.weight(1f))
        }
        if (state.closetValue != null && state.itemsWithPrice < state.itemCount) {
            Text(
                "Value counts the ${plural(state.itemsWithPrice, "piece")} with a price.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun StreakCard(streaks: Streaks, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        shape = MaterialTheme.shapes.large,
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.LocalFireDepartment, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Logging streak", style = MaterialTheme.typography.titleSmall)
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth()) {
                StreakFigure(streaks.current, "day", "Current", Modifier.weight(1f))
                StreakFigure(streaks.longest, "day", "Longest", Modifier.weight(1f))
                StreakFigure(streaks.daysLogged, "day", "Logged in total", Modifier.weight(1f))
            }
            if (streaks.daysLogged == 0) {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Mark an outfit as worn from Today or the planner to start a streak.",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun StreakFigure(value: Int, unit: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier.clearAndSetSemantics { contentDescription = "$label: ${plural(value, unit)}" }) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(value.toString(), style = MaterialTheme.typography.displaySmall)
            Spacer(Modifier.width(4.dp))
            Text(
                if (value == 1) unit else unit + "s",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 6.dp),
            )
        }
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}

// ---- Monthly chart ----

@Composable
private fun MonthlySection(months: List<MonthBar>, modifier: Modifier = Modifier) {
    val total = months.sumOf { it.count }
    val busiest = months.filter { it.count > 0 }.maxByOrNull { it.count }
    val thisMonth = months.lastOrNull { it.current }
    Column(modifier) {
        SectionIntro("Outfits logged", "Days you recorded what you wore, month by month.", padded = false)
        Spacer(Modifier.height(12.dp))
        CardSurface {
            Column(Modifier.padding(16.dp)) {
                MonthlyBarChart(months)
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(Modifier.height(12.dp))
                Text(
                    when {
                        total == 0 -> "Nothing logged in the last year yet. Each outfit you mark as worn adds to this chart."
                        else -> buildString {
                            append(plural(total, "day"))
                            append(" logged in 12 months")
                            if (thisMonth != null) append(" · ${thisMonth.count} this month")
                            if (busiest != null) {
                                append(" · busiest: ")
                                append(busiest.month.month.getDisplayName(TextStyle.FULL, Locale.UK))
                            }
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

// ---- Closet mix ----

@Composable
private fun ClosetMixSection(state: InsightsUiState, modifier: Modifier = Modifier) {
    Column(modifier) {
        SectionIntro("What's in your closet", "By category and by main colour.", padded = false)
        Spacer(Modifier.height(12.dp))
        CardSurface {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                if (state.categories.isNotEmpty()) {
                    CategoryBars(state.categories, total = state.categories.sumOf { it.second })
                }
                if (state.colors.isNotEmpty()) {
                    if (state.categories.isNotEmpty()) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Text("Colour palette", style = MaterialTheme.typography.titleSmall)
                    ColorPalette(state.colors)
                }
            }
        }
    }
}

// ---- Item rows ----

private fun LazyListScope.itemRow(
    key: String,
    title: String,
    caption: String,
    items: List<RankedItem>,
    onClick: (Long) -> Unit,
) {
    if (items.isEmpty()) return
    item(key = "$key-header") { SectionIntro(title, caption) }
    item(key = "$key-row") {
        LazyRow(
            contentPadding = PaddingValues(horizontal = PagePadding),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(items, key = { it.item.id }) { ranked ->
                ItemTile(
                    ranked.item,
                    modifier = Modifier.width(TileWidth),
                    showName = true,
                    badge = ranked.badge,
                    onClick = { onClick(ranked.item.id) },
                )
            }
        }
    }
}

@Composable
private fun ForgottenTile(ranked: RankedItem, onClick: () -> Unit) {
    Column(
        Modifier
            .width(TileWidth + 8.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClickLabel = "Suggest outfits with ${ranked.item.name}", role = Role.Button, onClick = onClick),
    ) {
        ItemTile(ranked.item, modifier = Modifier.fillMaxWidth(), badge = ranked.badge)
        Text(
            ranked.item.name,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 6.dp, start = 2.dp, end = 2.dp),
        )
        Row(
            Modifier.heightIn(min = 32.dp).padding(horizontal = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Outlined.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(14.dp),
            )
            Spacer(Modifier.width(4.dp))
            Text("Style it", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}

// ---- Closet power ----

@Composable
private fun ClosetPowerCard(count: Int, modifier: Modifier = Modifier) {
    val formatted = NumberFormat.getIntegerInstance(Locale.UK).format(count)
    Surface(
        modifier = modifier.fillMaxWidth().semantics(mergeDescendants = true) {},
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = MaterialTheme.shapes.large,
    ) {
        Column(Modifier.padding(horizontal = 20.dp, vertical = 22.dp)) {
            Text("CLOSET POWER", style = MaterialTheme.typography.labelMedium, modifier = Modifier.semantics { heading() })
            Spacer(Modifier.height(8.dp))
            Text("Your closet makes", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(formatted, style = MaterialTheme.typography.displayLarge)
                Spacer(Modifier.width(8.dp))
                Text(
                    if (count == 1) "outfit" else "outfits",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 6.dp),
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                if (count == 0) {
                    "Add a top, a bottom and a pair of shoes (or a dress and shoes) and the combinations start adding up."
                } else {
                    "Every distinct top + bottom + shoes, and dress + shoes, you can put together today. Layers, bags and accessories multiply it further."
                },
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun WishlistImpactRow(row: WishlistRow, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val impact = row.impact
    val description = when (impact) {
        null -> row.item.name
        0 -> "${row.item.name}. Wouldn't unlock new combinations with what you own yet."
        else -> "${row.item.name}. Adding this would unlock ${plural(impact, "new combination")}."
    }
    CardSurface(
        modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClickLabel = "Open ${row.item.name}", role = Role.Button, onClick = onClick)
            .clearAndSetSemantics { contentDescription = description },
    ) {
        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            ItemTile(row.item, modifier = Modifier.width(64.dp))
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(row.item.name, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (impact != null) {
                    Text(
                        if (impact == 0) "Wouldn't unlock new combinations yet"
                        else "Adding this would unlock ${plural(impact, "new combination")}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            if (impact != null) {
                Spacer(Modifier.width(10.dp))
                Surface(
                    shape = CircleShape,
                    color = if (impact > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = if (impact > 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                ) {
                    Text(
                        "+$impact ${if (impact == 1) "outfit" else "outfits"}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    )
                }
            } else {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

// ---- Shared bits ----

@Composable
private fun SectionIntro(title: String, caption: String, padded: Boolean = true) {
    Column(
        Modifier
            .fillMaxWidth()
            .then(if (padded) Modifier.padding(horizontal = PagePadding) else Modifier),
    ) {
        SectionHeader(title, Modifier.semantics { heading() })
        Text(
            caption,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun CardSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        content = content,
    )
}
