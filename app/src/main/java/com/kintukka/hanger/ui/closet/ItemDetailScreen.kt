package com.kintukka.hanger.ui.closet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.kintukka.hanger.AppContainer
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.data.Outfit
import com.kintukka.hanger.engine.Formality
import com.kintukka.hanger.engine.GarmentStatus
import com.kintukka.hanger.engine.Warmth
import com.kintukka.hanger.ui.components.ColorDot
import com.kintukka.hanger.ui.components.ItemImage
import com.kintukka.hanger.ui.components.LabeledValue
import com.kintukka.hanger.ui.components.OutfitCollage
import com.kintukka.hanger.ui.components.SectionHeader
import com.kintukka.hanger.ui.components.StatPill
import com.kintukka.hanger.ui.formatMoney
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.navigation.BuilderRoute
import com.kintukka.hanger.ui.navigation.EditItemRoute
import com.kintukka.hanger.ui.navigation.OutfitRoute
import com.kintukka.hanger.ui.navigation.SuggestRoute
import com.kintukka.hanger.ui.relativeLabel
import com.kintukka.hanger.ui.shortLabel
import com.kintukka.hanger.ui.theme.HangerTokens
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class ItemDetailState(
    val item: Item? = null,
    val missing: Boolean = false,
    val outfits: List<Outfit> = emptyList(),
    val history: List<LocalDate> = emptyList(),
    val itemsById: Map<Long, Item> = emptyMap(),
    val currency: String = "£",
)

class ItemDetailViewModel(private val container: AppContainer, private val id: Long) : ViewModel() {
    private val repo = container.repo

    val state: StateFlow<ItemDetailState> = combine(
        repo.allItems, repo.outfits, repo.dayEntries, container.settings.settings,
    ) { items, outfits, days, settings ->
        val item = items.firstOrNull { it.id == id }
        ItemDetailState(
            item = item,
            missing = item == null && items.isNotEmpty(),
            outfits = outfits.filter { o -> o.itemIds.contains(id) },
            history = days.filter { it.worn && id in it.itemIds }.map { it.date }.sortedDescending(),
            itemsById = items.associateBy { it.id },
            currency = settings.currency,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ItemDetailState())

    fun setStatus(status: GarmentStatus) = viewModelScope.launch { repo.setStatus(id, status) }
    fun toggleFavorite() = viewModelScope.launch { repo.toggleFavorite(id) }
    fun setArchived(archived: Boolean) = viewModelScope.launch { repo.setArchived(id, archived) }
    fun moveToCloset() = viewModelScope.launch {
        repo.item(id)?.let { repo.updateItem(it.copy(wishlist = false)) }
    }
    fun delete(onDone: () -> Unit) = viewModelScope.launch { repo.deleteItem(id); onDone() }
    fun logWornToday() = viewModelScope.launch { repo.logWorn(LocalDate.now(), listOf(id)) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(navController: NavHostController, id: Long) {
    val vm = hangerViewModel(key = "item-$id") { ItemDetailViewModel(it, id) }
    val state by vm.state.collectAsStateWithLifecycle()
    var menu by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }
    var leaving by remember { mutableStateOf(false) }

    LaunchedEffect(state.missing) {
        if (state.missing && !leaving) { leaving = true; navController.popBackStack() }
    }
    val item = state.item

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back") }
                },
                actions = {
                    if (item != null) {
                        IconButton(onClick = vm::toggleFavorite) {
                            Icon(
                                if (item.entity.favorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = if (item.entity.favorite) "Remove from favourites" else "Add to favourites",
                                tint = if (item.entity.favorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        IconButton(onClick = { navController.navigate(EditItemRoute(id)) }) { Icon(Icons.Outlined.Edit, contentDescription = "Edit") }
                        Box {
                            IconButton(onClick = { menu = true }) { Icon(Icons.Outlined.MoreVert, contentDescription = "More options") }
                            DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                                if (item.entity.wishlist) {
                                    DropdownMenuItem(text = { Text("I bought it — move to closet") }, onClick = { menu = false; vm.moveToCloset() })
                                }
                                DropdownMenuItem(
                                    text = { Text(if (item.entity.archived) "Restore to closet" else "Archive (sold, donated…)") },
                                    onClick = { menu = false; vm.setArchived(!item.entity.archived) },
                                )
                                DropdownMenuItem(text = { Text("Delete", color = MaterialTheme.colorScheme.error) }, onClick = { menu = false; confirmDelete = true })
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        },
    ) { padding ->
        if (item == null) return@Scaffold
        val e = item.entity
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            item(key = "hero") {
                Box(
                    Modifier.fillMaxWidth().aspectRatio(1.1f).clip(MaterialTheme.shapes.extraLarge).background(HangerTokens.extras.tile),
                ) { ItemImage(item, Modifier.fillMaxSize().padding(28.dp)) }
            }
            item(key = "title") {
                Column {
                    Text(e.name, style = MaterialTheme.typography.headlineLarge)
                    val subtitle = listOf(e.subcategory.ifBlank { e.category.label }, e.brand).filter { it.isNotBlank() }.joinToString(" · ")
                    Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (e.wishlist) {
                        Text("On your wishlist", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
            if (!e.wishlist) {
                item(key = "status") {
                    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                        val options = listOf(GarmentStatus.CLEAN to "Clean", GarmentStatus.DIRTY to "Laundry", GarmentStatus.AWAY to "Away")
                        options.forEachIndexed { i, (status, label) ->
                            SegmentedButton(
                                selected = e.status == status,
                                onClick = { vm.setStatus(status) },
                                shape = SegmentedButtonDefaults.itemShape(i, options.size),
                            ) { Text(label) }
                        }
                    }
                }
                item(key = "stats") {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatPill("Times worn", item.wears.toString(), Modifier.weight(1f))
                        StatPill("Last worn", item.lastWorn?.relativeLabel() ?: "Never", Modifier.weight(1f))
                        StatPill("Cost / wear", item.costPerWear?.let { formatMoney(it, state.currency) } ?: "—", Modifier.weight(1f))
                    }
                }
            }
            item(key = "actions") {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = { navController.navigate(SuggestRoute(anchorItemId = id)) }, modifier = Modifier.weight(1f).height(48.dp)) {
                        Icon(Icons.Outlined.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Style it")
                    }
                    OutlinedButton(onClick = { navController.navigate(BuilderRoute(itemIds = id.toString())) }, modifier = Modifier.weight(1f).height(48.dp)) {
                        Text("Build around it")
                    }
                }
                if (!e.wishlist) {
                    TextButton(onClick = vm::logWornToday, modifier = Modifier.padding(top = 4.dp)) {
                        Icon(Icons.Outlined.EventAvailable, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Wore it today")
                    }
                }
            }
            item(key = "details") {
                Column {
                    SectionHeader("Details")
                    if (e.colors.isNotEmpty()) {
                        Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("Colours", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(120.dp))
                            e.colors.forEach { c ->
                                ColorDot(c, 16.dp)
                                Text(c.label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 6.dp, end = 12.dp))
                            }
                        }
                    }
                    LabeledValue("Pattern", e.pattern.label)
                    LabeledValue("Formality", Formality.label(e.formality))
                    LabeledValue("Warmth", Warmth.label(e.warmth))
                    LabeledValue("Seasons", if (e.seasons.isEmpty()) "All year" else e.seasons.sorted().joinToString { it.label })
                    LabeledValue(
                        "Laundry",
                        if (e.wearsBeforeWash == 0) "Doesn't need regular washing"
                        else "Wash every ${e.wearsBeforeWash} wear${if (e.wearsBeforeWash == 1) "" else "s"} · worn ${e.wearsSinceWash} since last wash",
                    )
                    if (e.price != null) LabeledValue("Price", formatMoney(e.price, state.currency))
                    if (e.purchaseDate != null) LabeledValue("Bought", e.purchaseDate.shortLabel())
                    if (e.size.isNotBlank()) LabeledValue("Size", e.size)
                    if (e.material.isNotBlank()) LabeledValue("Material", e.material)
                    if (e.tags.isNotEmpty()) LabeledValue("Tags", e.tags.joinToString())
                    if (e.notes.isNotBlank()) LabeledValue("Notes", e.notes)
                }
            }
            item(key = "style-notes") { com.kintukka.hanger.ui.guide.StyleNotes(e) }
            if (state.outfits.isNotEmpty()) {
                item(key = "outfits") {
                    Column {
                        SectionHeader("In ${state.outfits.size} outfit${if (state.outfits.size == 1) "" else "s"}")
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(top = 8.dp)) {
                            items(state.outfits, key = { it.id }) { o ->
                                Column(Modifier.width(120.dp).clickable { navController.navigate(OutfitRoute(o.id)) }) {
                                    OutfitCollage(o.placements, state.itemsById, Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium)
                                    Text(o.name, style = MaterialTheme.typography.labelMedium, maxLines = 1, modifier = Modifier.padding(top = 4.dp))
                                }
                            }
                        }
                    }
                }
            }
            if (state.history.isNotEmpty()) {
                item(key = "history") {
                    Column {
                        SectionHeader("Wear history")
                        state.history.take(12).forEach { d ->
                            Text("${d.shortLabel()} · ${d.relativeLabel()}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }
            }
        }
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete ${item?.name ?: "item"}?") },
            text = { Text("It disappears from outfits and history too. Archive it instead to keep its stats.") },
            confirmButton = {
                TextButton(onClick = { confirmDelete = false; leaving = true; vm.delete { navController.popBackStack() } }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Cancel") } },
        )
    }
}
