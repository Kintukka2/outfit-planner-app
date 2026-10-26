package com.kintukka.hanger.ui.closet

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.LocalLaundryService
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.kintukka.hanger.AppContainer
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.engine.Category
import com.kintukka.hanger.engine.GarmentStatus
import com.kintukka.hanger.ui.components.EmptyState
import com.kintukka.hanger.ui.components.ItemTile
import com.kintukka.hanger.ui.components.PageTitle
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.navigation.AddItemsRoute
import com.kintukka.hanger.ui.navigation.EditItemRoute
import com.kintukka.hanger.ui.navigation.ItemRoute
import com.kintukka.hanger.ui.plural
import com.kintukka.hanger.ui.theme.HangerTokens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

enum class ClosetView(val label: String) { WARDROBE("Wardrobe"), WISHLIST("Wishlist"), ARCHIVE("Archive") }

enum class ClosetSort(val label: String) {
    RECENT("Recently added"), MOST_WORN("Most worn"), LEAST_WORN("Least worn"),
    LAST_WORN("Longest unworn"), COST_PER_WEAR("Best cost per wear"), COLOR("Colour"), NAME("Name"),
}

data class ClosetFilters(
    val view: ClosetView = ClosetView.WARDROBE,
    val category: Category? = null,
    val query: String = "",
    val sort: ClosetSort = ClosetSort.RECENT,
    val onlyClean: Boolean = false,
    val favourites: Boolean = false,
)

data class ClosetUiState(
    val items: List<Item> = emptyList(),
    val counts: Map<Category?, Int> = emptyMap(),
    val dirtyCount: Int = 0,
    val total: Int = 0,
    val filters: ClosetFilters = ClosetFilters(),
    val loaded: Boolean = false,
)

class ClosetViewModel(private val container: AppContainer) : ViewModel() {
    private val filters = MutableStateFlow(ClosetFilters())

    val state: StateFlow<ClosetUiState> = combine(container.repo.allItems, filters) { all, f ->
        val inView = all.filter {
            when (f.view) {
                ClosetView.WARDROBE -> !it.entity.archived && !it.entity.wishlist
                ClosetView.WISHLIST -> it.entity.wishlist && !it.entity.archived
                ClosetView.ARCHIVE -> it.entity.archived
            }
        }
        val q = f.query.trim().lowercase()
        val filtered = inView.filter { item ->
            (f.category == null || item.category == f.category) &&
                (!f.onlyClean || item.status == GarmentStatus.CLEAN) &&
                (!f.favourites || item.entity.favorite) &&
                (q.isEmpty() || listOf(item.name, item.entity.subcategory, item.entity.brand, item.entity.material, item.entity.notes)
                    .plus(item.entity.colors.map { it.label })
                    .plus(item.entity.tags)
                    .any { it.lowercase().contains(q) })
        }
        val sorted = when (f.sort) {
            ClosetSort.RECENT -> filtered.sortedByDescending { it.entity.createdAt }
            ClosetSort.MOST_WORN -> filtered.sortedByDescending { it.wears }
            ClosetSort.LEAST_WORN -> filtered.sortedBy { it.wears }
            ClosetSort.LAST_WORN -> filtered.sortedBy { it.lastWorn?.toEpochDay() ?: Long.MIN_VALUE }
            ClosetSort.COST_PER_WEAR -> filtered.sortedBy { it.costPerWear ?: Double.MAX_VALUE }
            ClosetSort.COLOR -> filtered.sortedWith(compareBy({ it.entity.colors.firstOrNull()?.ordinal ?: 999 }, { it.name }))
            ClosetSort.NAME -> filtered.sortedBy { it.name.lowercase() }
        }
        val counts = buildMap<Category?, Int> {
            put(null, inView.size)
            inView.groupingBy { it.category }.eachCount().forEach { (k, v) -> put(k, v) }
        }
        ClosetUiState(
            items = sorted,
            counts = counts,
            dirtyCount = all.count { it.status == GarmentStatus.DIRTY && !it.entity.archived },
            total = all.count { !it.entity.archived && !it.entity.wishlist },
            filters = f,
            loaded = true,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ClosetUiState())

    fun update(transform: (ClosetFilters) -> ClosetFilters) { filters.value = transform(filters.value) }

    fun washAll() = viewModelScope.launch { container.repo.washAll() }

    fun queueCaptures(uris: List<Uri>) { container.pendingCaptures.value = uris }

    fun newCaptureUri(): Uri {
        val dir = File(container.context.cacheDir, "captures").apply { mkdirs() }
        val file = File(dir, "capture_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(container.context, "${container.context.packageName}.files", file)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClosetScreen(navController: NavHostController) {
    val vm = hangerViewModel { ClosetViewModel(it) }
    val state by vm.state.collectAsStateWithLifecycle()
    var showAdd by rememberSaveable { mutableStateOf(false) }
    var searching by rememberSaveable { mutableStateOf(false) }
    var sortOpen by remember { mutableStateOf(false) }
    var pendingCamera by rememberSaveable { mutableStateOf<String?>(null) }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(30)) { uris ->
        if (uris.isNotEmpty()) {
            vm.queueCaptures(uris)
            navController.navigate(AddItemsRoute)
        }
    }
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        val uri = pendingCamera?.let(Uri::parse)
        if (ok && uri != null) {
            vm.queueCaptures(listOf(uri))
            navController.navigate(AddItemsRoute)
        }
        pendingCamera = null
    }

    Box(Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(104.dp),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 96.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }, key = "header") {
                Column {
                    Spacer(Modifier.height(16.dp))
                    PageTitle(
                        title = "Closet",
                        subtitle = plural(state.total, "piece"),
                        trailing = {
                            Row {
                                IconButton(onClick = { searching = !searching; if (!searching) vm.update { it.copy(query = "") } }) {
                                    Icon(if (searching) Icons.Outlined.Close else Icons.Outlined.Search, contentDescription = if (searching) "Close search" else "Search closet")
                                }
                                Box {
                                    IconButton(onClick = { sortOpen = true }) {
                                        Icon(Icons.AutoMirrored.Outlined.Sort, contentDescription = "Sort")
                                    }
                                    DropdownMenu(expanded = sortOpen, onDismissRequest = { sortOpen = false }) {
                                        ClosetSort.entries.forEach { sort ->
                                            DropdownMenuItem(
                                                text = { Text(sort.label, color = if (sort == state.filters.sort) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface) },
                                                onClick = { vm.update { it.copy(sort = sort) }; sortOpen = false },
                                            )
                                        }
                                    }
                                }
                            }
                        },
                    )
                    if (searching) {
                        OutlinedTextField(
                            value = state.filters.query,
                            onValueChange = { q -> vm.update { it.copy(query = q) } },
                            placeholder = { Text("Search by name, colour, brand, tag…") },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                        ClosetView.entries.forEachIndexed { index, view ->
                            SegmentedButton(
                                selected = state.filters.view == view,
                                onClick = { vm.update { it.copy(view = view, category = null) } },
                                shape = SegmentedButtonDefaults.itemShape(index, ClosetView.entries.size),
                            ) { Text(view.label) }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item(key = "all") {
                            FilterChip(
                                selected = state.filters.category == null,
                                onClick = { vm.update { it.copy(category = null) } },
                                label = { Text("All ${state.counts[null] ?: 0}") },
                            )
                        }
                        items(pickerCategories, key = { it.name }) { c ->
                            val count = state.counts[c] ?: 0
                            if (count > 0 || state.filters.category == c) {
                                FilterChip(
                                    selected = state.filters.category == c,
                                    onClick = { vm.update { it.copy(category = if (it.category == c) null else c) } },
                                    label = { Text("${c.label} $count") },
                                )
                            }
                        }
                        item(key = "clean") {
                            FilterChip(selected = state.filters.onlyClean, onClick = { vm.update { it.copy(onlyClean = !it.onlyClean) } }, label = { Text("Clean only") })
                        }
                        item(key = "fav") {
                            FilterChip(selected = state.filters.favourites, onClick = { vm.update { it.copy(favourites = !it.favourites) } }, label = { Text("Favourites") })
                        }
                    }
                    if (state.dirtyCount > 0 && state.filters.view == ClosetView.WARDROBE) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        ) {
                            Row(Modifier.padding(start = 14.dp, end = 4.dp, top = 4.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.LocalLaundryService, contentDescription = null, tint = HangerTokens.extras.laundry, modifier = Modifier.size(20.dp))
                                Text(
                                    "${plural(state.dirtyCount, "piece")} in the laundry",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1f).padding(start = 10.dp),
                                )
                                TextButton(onClick = vm::washAll) { Text("Mark all washed") }
                            }
                        }
                    }
                }
            }

            if (state.loaded && state.items.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }, key = "empty") {
                    val message = when {
                        state.filters.query.isNotBlank() -> "Nothing matches \"${state.filters.query}\"."
                        state.filters.view == ClosetView.WISHLIST -> "Save pieces you're considering. Insights shows how many new outfits each would unlock."
                        state.filters.view == ClosetView.ARCHIVE -> "Archived pieces live here — sold, donated or retired, with their history kept."
                        else -> "Snap or pick photos of your clothes. Backgrounds are removed and colours tagged automatically, right on your phone."
                    }
                    EmptyState(
                        icon = Icons.Outlined.Checkroom,
                        title = if (state.filters.view == ClosetView.WARDROBE && state.filters.query.isBlank()) "Start your closet" else "Nothing here yet",
                        body = message,
                    )
                }
            }

            items(state.items, key = { it.id }) { item ->
                ItemTile(item, showName = true, onClick = { navController.navigate(ItemRoute(item.id)) })
            }
        }

        ExtendedFloatingActionButton(
            onClick = { showAdd = true },
            icon = { Icon(Icons.Outlined.Add, contentDescription = null) },
            text = { Text("Add") },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
        )
    }

    if (showAdd) {
        ModalBottomSheet(onDismissRequest = { showAdd = false }) {
            Column(Modifier.padding(bottom = 24.dp)) {
                Text("Add to your closet", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))
                AddOption(Icons.Outlined.AddAPhoto, "Take a photo", "Lay it flat or hang it up — plain backgrounds cut out best") {
                    showAdd = false
                    val uri = vm.newCaptureUri()
                    pendingCamera = uri.toString()
                    camera.launch(uri)
                }
                AddOption(Icons.Outlined.PhotoLibrary, "Choose photos", "Add up to 30 at once, including outfit photos") {
                    showAdd = false
                    picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
                AddOption(Icons.Outlined.Brush, "Add without a photo", "Describe it and get an illustration — great for wishlists") {
                    showAdd = false
                    navController.navigate(EditItemRoute(0))
                }
            }
        }
    }
}

@Composable
private fun AddOption(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, body: String, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(title, style = MaterialTheme.typography.titleMedium) },
        supportingContent = { Text(body) },
        leadingContent = { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        modifier = Modifier.padding(horizontal = 8.dp).clickable(onClick = onClick),
    )
}
