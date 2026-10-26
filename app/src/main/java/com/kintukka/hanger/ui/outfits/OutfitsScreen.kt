package com.kintukka.hanger.ui.outfits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.outlined.ViewModule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.kintukka.hanger.AppContainer
import com.kintukka.hanger.data.Capsule
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.data.Outfit
import com.kintukka.hanger.engine.CapsuleAnalyzer
import com.kintukka.hanger.engine.Occasion
import com.kintukka.hanger.ui.components.EmptyState
import com.kintukka.hanger.ui.components.ItemImage
import com.kintukka.hanger.ui.components.OutfitCollage
import com.kintukka.hanger.ui.components.PageTitle
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.navigation.BuilderRoute
import com.kintukka.hanger.ui.navigation.CapsuleRoute
import com.kintukka.hanger.ui.navigation.OutfitRoute
import com.kintukka.hanger.ui.navigation.ShuffleRoute
import com.kintukka.hanger.ui.navigation.SuggestRoute
import com.kintukka.hanger.ui.plural
import com.kintukka.hanger.ui.relativeLabel
import com.kintukka.hanger.ui.theme.HangerTokens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CapsuleSummary(val capsule: Capsule, val outfitCount: Int?)

data class OutfitsUi(
    val outfits: List<Outfit> = emptyList(),
    val items: Map<Long, Item> = emptyMap(),
    val capsules: List<CapsuleSummary> = emptyList(),
    val filter: Occasion? = null,
    val favourites: Boolean = false,
    val loaded: Boolean = false,
)

class OutfitsViewModel(private val container: AppContainer) : ViewModel() {
    private val filter = MutableStateFlow<Pair<Occasion?, Boolean>>(null to false)

    val state: StateFlow<OutfitsUi> = combine(
        container.repo.outfits, container.repo.allItems, container.repo.capsules, filter,
    ) { outfits, items, capsules, (occasion, favs) ->
        val byId = items.associateBy { it.id }
        OutfitsUi(
            outfits = outfits.filter { (occasion == null || it.entity.occasion == occasion) && (!favs || it.entity.favorite) },
            items = byId,
            capsules = capsules.map { c ->
                val garments = c.itemIds.mapNotNull { byId[it]?.toGarment() }
                CapsuleSummary(c, runCatching { CapsuleAnalyzer.outfitCount(garments) }.getOrNull())
            },
            filter = occasion,
            favourites = favs,
            loaded = true,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OutfitsUi())

    fun setFilter(occasion: Occasion?) { filter.value = occasion to filter.value.second }
    fun toggleFavourites() { filter.value = filter.value.first to !filter.value.second }

    fun createCapsule(name: String, onCreated: (Long) -> Unit) = viewModelScope.launch {
        onCreated(container.repo.createCapsule(name.ifBlank { "New capsule" }, "", emptyList()))
    }
}

@Composable
fun OutfitsScreen(navController: NavHostController) {
    val vm = hangerViewModel { OutfitsViewModel(it) }
    val state by vm.state.collectAsStateWithLifecycle()
    var tab by rememberSaveable { mutableStateOf(0) }
    var newCapsule by rememberSaveable { mutableStateOf(false) }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }, key = "header") {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Spacer(Modifier.height(4.dp))
                PageTitle("Outfits", subtitle = plural(state.outfits.size, "saved look"))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickAction(Icons.Outlined.Dashboard, "Build", "Canvas", Modifier.weight(1f)) { navController.navigate(BuilderRoute()) }
                    QuickAction(Icons.Outlined.Shuffle, "Dress me", "Shuffle", Modifier.weight(1f)) { navController.navigate(ShuffleRoute()) }
                    QuickAction(Icons.Outlined.AutoAwesome, "Ideas", "Stylist", Modifier.weight(1f)) { navController.navigate(SuggestRoute()) }
                }
                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    listOf("Saved outfits", "Capsules").forEachIndexed { i, label ->
                        SegmentedButton(selected = tab == i, onClick = { tab = i }, shape = SegmentedButtonDefaults.itemShape(i, 2)) { Text(label) }
                    }
                }
                if (tab == 0) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item { FilterChip(selected = state.filter == null && !state.favourites, onClick = { vm.setFilter(null); if (state.favourites) vm.toggleFavourites() }, label = { Text("All") }) }
                        item { FilterChip(selected = state.favourites, onClick = vm::toggleFavourites, label = { Text("Favourites") }) }
                        items(Occasion.entries, key = { it.name }) { o ->
                            FilterChip(selected = state.filter == o, onClick = { vm.setFilter(if (state.filter == o) null else o) }, label = { Text(o.label) })
                        }
                    }
                }
            }
        }

        if (tab == 0) {
            if (state.loaded && state.outfits.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }, key = "empty") {
                    EmptyState(
                        Icons.Outlined.Style,
                        "No saved outfits yet",
                        "Build one on the canvas, shuffle a few combinations, or save a suggestion you like.",
                    )
                }
            }
            items(state.outfits, key = { it.id }) { outfit ->
                Column(Modifier.clickable { navController.navigate(OutfitRoute(outfit.id)) }) {
                    androidx.compose.foundation.layout.Box {
                        OutfitCollage(outfit.placements, state.items, Modifier.fillMaxWidth())
                        if (outfit.entity.favorite) {
                            Icon(
                                Icons.Filled.Favorite, contentDescription = "Favourite", tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.align(Alignment.TopEnd).padding(10.dp).size(16.dp),
                            )
                        }
                    }
                    Text(outfit.name, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 8.dp, start = 2.dp))
                    val meta = listOfNotNull(
                        outfit.entity.occasion?.label,
                        if (outfit.timesWorn > 0) "Worn ${outfit.timesWorn}×" else "Not worn yet",
                        outfit.lastWorn?.relativeLabel(),
                    ).joinToString(" · ")
                    Text(meta, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, modifier = Modifier.padding(start = 2.dp))
                }
            }
        } else {
            item(span = { GridItemSpan(maxLineSpan) }, key = "capsule-intro") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "A capsule is a small edit of pieces that all work together — for a season, a trip or a work week. Hanger counts how many outfits each capsule makes and which closet piece would add the most.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    TextButton(onClick = { newCapsule = true }) {
                        Icon(Icons.Outlined.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("New capsule", modifier = Modifier.padding(start = 6.dp))
                    }
                }
            }
            items(state.capsules, key = { "cap-${it.capsule.entity.id}" }, span = { GridItemSpan(maxLineSpan) }) { summary ->
                CapsuleCard(summary, state.items) { navController.navigate(CapsuleRoute(summary.capsule.entity.id)) }
            }
        }
    }

    if (newCapsule) {
        var name by rememberSaveable { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { newCapsule = false },
            title = { Text("New capsule") },
            text = { OutlinedTextField(name, { name = it }, label = { Text("Name, e.g. Autumn workweek") }, singleLine = true) },
            confirmButton = {
                TextButton(onClick = {
                    newCapsule = false
                    vm.createCapsule(name) { id -> navController.navigate(CapsuleRoute(id)) }
                }) { Text("Create") }
            },
            dismissButton = { TextButton(onClick = { newCapsule = false }) { Text("Cancel") } },
        )
    }
}

@Composable
private fun QuickAction(icon: ImageVector, title: String, subtitle: String, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = MaterialTheme.shapes.large,
        modifier = modifier,
    ) {
        Column(Modifier.padding(14.dp)) {
            Icon(icon, contentDescription = null)
            Spacer(Modifier.height(10.dp))
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun CapsuleCard(summary: CapsuleSummary, items: Map<Long, Item>, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.ViewModule, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(summary.capsule.entity.name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f).padding(start = 10.dp))
            }
            Text(
                listOfNotNull(plural(summary.capsule.itemIds.size, "piece"), summary.outfitCount?.let { plural(it, "outfit") }).joinToString(" · "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
            if (summary.capsule.itemIds.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = 10.dp)) {
                    items(summary.capsule.itemIds.take(12), key = { it }) { id ->
                        androidx.compose.foundation.layout.Box(
                            Modifier.size(48.dp).padding(0.dp),
                        ) {
                            Surface(color = HangerTokens.extras.tile, shape = MaterialTheme.shapes.small, modifier = Modifier.fillMaxSize()) {
                                ItemImage(items[id], Modifier.fillMaxSize().padding(4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
