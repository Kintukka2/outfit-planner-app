@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.kintukka.hanger.ui.planner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.kintukka.hanger.AppContainer
import com.kintukka.hanger.data.DayEntry
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.data.Trip
import com.kintukka.hanger.data.db.PackingItemEntity
import com.kintukka.hanger.data.repo.TempUnit
import com.kintukka.hanger.engine.PackCategory
import com.kintukka.hanger.engine.WeatherCondition
import com.kintukka.hanger.ui.components.ItemsCollage
import com.kintukka.hanger.ui.components.SectionHeader
import com.kintukka.hanger.ui.components.weatherIcon
import com.kintukka.hanger.ui.formatTemp
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.navigation.DayRoute
import com.kintukka.hanger.ui.navigation.TripEditRoute
import com.kintukka.hanger.ui.plural
import com.kintukka.hanger.ui.shortLabel
import com.kintukka.hanger.ui.theme.HangerTokens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

// ---------------------------------------------------------------------------------------------
// State
// ---------------------------------------------------------------------------------------------

@Immutable
internal data class PackingGroup(val category: PackCategory, val lines: List<PackingItemEntity>)

@Immutable
internal data class TripUiState(
    val loading: Boolean = true,
    val trip: Trip? = null,
    val itemsById: Map<Long, Item> = emptyMap(),
    /** The trip's planned/worn day entries, by date. */
    val outfits: List<DayEntry> = emptyList(),
    val groups: List<PackingGroup> = emptyList(),
    val tempUnit: TempUnit = TempUnit.CELSIUS,
) {
    val missing: Boolean get() = !loading && trip == null
}

/** Forecast summary over the trip days the forecast covers. */
@Immutable
internal data class TripWeather(
    val minC: Double,
    val maxC: Double,
    val condition: WeatherCondition,
    val wetDays: Int,
    val coveredDays: Int,
    val totalDays: Int,
)

private data class WeatherKey(val lat: Double, val lon: Double, val start: LocalDate, val end: LocalDate)

internal class TripViewModel(private val id: Long, private val container: AppContainer) : MessagingViewModel() {
    private val repo = container.repo
    private val tripFlow = repo.trip(id)

    val state: StateFlow<TripUiState> = combine(
        tripFlow,
        repo.allItems,
        repo.dayEntries,
        container.settings.settings,
    ) { trip, items, entries, settings ->
        TripUiState(
            loading = false,
            trip = trip,
            itemsById = items.associateBy { it.id },
            outfits = entries.filter { it.entity.tripId == id }.sortedWith(compareBy({ it.date }, { it.entity.createdAt })),
            groups = trip?.packing.orEmpty()
                .groupBy { it.category }
                .toSortedMap(compareBy { it.ordinal })
                .map { (category, lines) -> PackingGroup(category, lines) },
            tempUnit = settings.tempUnit,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TripUiState())

    val weather: StateFlow<TripWeather?> = tripFlow
        .map { trip ->
            val e = trip?.entity ?: return@map null
            val lat = e.latitude ?: return@map null
            val lon = e.longitude ?: return@map null
            if (!overlapsForecast(e.startDate, e.endDate)) return@map null
            WeatherKey(lat, lon, e.startDate, e.endDate)
        }
        .distinctUntilChanged()
        .map { key -> key?.let { loadWeather(it) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _regenerating = MutableStateFlow(false)
    val regenerating: StateFlow<Boolean> = _regenerating.asStateFlow()

    private suspend fun loadWeather(key: WeatherKey): TripWeather? {
        val forecast = catching { container.weather.forecast(key.lat, key.lon) }.getOrNull() ?: return null
        val days = forecast.days.filter { !it.date.isBefore(key.start) && !it.date.isAfter(key.end) }
        if (days.isEmpty()) return null
        val wet = setOf(WeatherCondition.DRIZZLE, WeatherCondition.RAIN, WeatherCondition.STORM, WeatherCondition.SNOW)
        return TripWeather(
            minC = days.minOf { it.tempMinC },
            maxC = days.maxOf { it.tempMaxC },
            condition = days.groupingBy { it.condition }.eachCount().maxBy { it.value }.key,
            wetDays = days.count { it.condition in wet || it.precipitationProbability >= 50 },
            coveredDays = days.size,
            totalDays = (key.end.toEpochDay() - key.start.toEpochDay() + 1).toInt(),
        )
    }

    fun setPacked(line: PackingItemEntity, packed: Boolean) = write("Couldn't update ${line.label}") {
        repo.setPacked(line, packed)
    }

    fun deleteLine(line: PackingItemEntity) = write("Couldn't remove ${line.label}") {
        repo.deletePackingLine(line)
    }

    fun addLine(label: String, category: PackCategory) {
        val clean = label.trim()
        if (clean.isEmpty()) return
        write("Couldn't add $clean") { repo.addPackingLine(id, clean, category) }
    }

    fun regenerate() {
        if (_regenerating.value) return
        viewModelScope.launch {
            _regenerating.value = true
            val trip = catching { repo.tripEntity(id) }.getOrNull()
            if (trip == null) {
                _regenerating.value = false
                return@launch
            }
            val result = generatePackingPlan(trip, repo, container.stylist, container.settings)
            _regenerating.value = false
            say(if (result.isSuccess) "Packing list rebuilt" else "Couldn't build a packing list right now")
        }
    }

    /** Deleting makes [state] report the trip as missing, which pops the screen. */
    fun delete() = write("Couldn't delete the trip") { repo.deleteTrip(id) }
}

// ---------------------------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------------------------

@Composable
fun TripScreen(navController: NavHostController, id: Long) {
    val vm = hangerViewModel(key = "trip-$id") { TripViewModel(id, it) }
    val state by vm.state.collectAsStateWithLifecycle()
    val weather by vm.weather.collectAsStateWithLifecycle()
    val regenerating by vm.regenerating.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    CollectMessages(vm.messages, snackbar)
    CollectHandedMessages(snackbar)
    val popSelf = rememberPopSelf(navController)

    LaunchedEffect(state.missing) {
        if (state.missing) popSelf()
    }

    var menuOpen by remember { mutableStateOf(false) }
    var confirmRegenerate by rememberSaveable { mutableStateOf(false) }
    var confirmDelete by rememberSaveable { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val showTitleInBar by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val trip = state.trip

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    if (showTitleInBar && trip != null) {
                        Text(trip.entity.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                },
                actions = {
                    if (trip != null) {
                        Box {
                            IconButton(onClick = { menuOpen = true }) {
                                Icon(Icons.Filled.MoreVert, contentDescription = "More options")
                            }
                            DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                                DropdownMenuItem(
                                    text = { Text("Edit trip") },
                                    leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) },
                                    onClick = {
                                        menuOpen = false
                                        navController.navigate(TripEditRoute(id))
                                    },
                                )
                                DropdownMenuItem(
                                    text = { Text("Regenerate plan") },
                                    leadingIcon = { Icon(Icons.Outlined.Autorenew, contentDescription = null) },
                                    enabled = !regenerating,
                                    onClick = {
                                        menuOpen = false
                                        confirmRegenerate = true
                                    },
                                )
                                DropdownMenuItem(
                                    text = { Text("Delete trip", color = MaterialTheme.colorScheme.error) },
                                    leadingIcon = {
                                        Icon(Icons.Outlined.DeleteOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                    },
                                    onClick = {
                                        menuOpen = false
                                        confirmDelete = true
                                    },
                                )
                            }
                        }
                    }
                },
            )
        },
    ) { padding ->
        if (trip == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                if (state.loading) CircularProgressIndicator()
            }
            return@Scaffold
        }
        val packedCount = trip.packedCount
        val totalCount = trip.packing.size
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(top = 4.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item(key = "header") {
                TripHeader(trip, weather, state.tempUnit, Modifier.padding(horizontal = 20.dp))
            }

            item(key = "outfits-header") {
                SectionHeader(
                    "Outfits",
                    Modifier.padding(horizontal = 20.dp).padding(top = 12.dp).semantics { heading() },
                )
            }
            item(key = "outfits") {
                if (state.outfits.isEmpty()) {
                    Text(
                        if (regenerating) "Planning outfits…" else "No outfits planned for this trip yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(state.outfits, key = { it.id }) { entry ->
                            TripOutfitCard(
                                entry = entry,
                                dayNumber = (entry.date.toEpochDay() - trip.entity.startDate.toEpochDay() + 1).toInt(),
                                itemsById = state.itemsById,
                                onClick = { navController.navigate(DayRoute(entry.date.toEpochDay())) },
                            )
                        }
                    }
                }
            }

            item(key = "packing-header") {
                Column(
                    Modifier.padding(horizontal = 20.dp).padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    SectionHeader("Packing list", Modifier.semantics { heading() })
                    if (totalCount > 0) {
                        Text(
                            if (packedCount == totalCount) "All $totalCount packed" else "$packedCount of $totalCount packed",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        LinearProgressIndicator(
                            progress = { packedCount.toFloat() / totalCount },
                            modifier = Modifier.fillMaxWidth().clearAndSetSemantics { },
                            color = if (packedCount == totalCount) HangerTokens.extras.success else MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        )
                    }
                }
            }

            if (totalCount == 0) {
                item(key = "packing-empty") {
                    PackingEmpty(regenerating, onGenerate = vm::regenerate, modifier = Modifier.padding(horizontal = 20.dp))
                }
            }

            state.groups.forEach { group ->
                item(key = "cat-${group.category.name}") {
                    Text(
                        group.category.label.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 12.dp)
                            .semantics { heading() },
                    )
                }
                items(group.lines, key = { "line-${it.id}" }) { line ->
                    PackingRow(
                        line = line,
                        item = line.itemId?.let { state.itemsById[it] },
                        onPackedChange = { vm.setPacked(line, it) },
                        onDelete = { vm.deleteLine(line) },
                    )
                }
            }

            item(key = "add-line") {
                AddPackingLine(onAdd = vm::addLine, modifier = Modifier.padding(horizontal = 20.dp).padding(top = 16.dp))
            }
        }
    }

    if (confirmRegenerate) {
        AlertDialog(
            onDismissRequest = { confirmRegenerate = false },
            icon = { Icon(Icons.Outlined.Autorenew, contentDescription = null) },
            title = { Text("Regenerate plan?") },
            text = {
                Text(
                    "Hanger rebuilds the packing list and planned outfits from your closet and the latest forecast. " +
                        "Things you've already ticked off stay packed, and lines you added yourself are kept.",
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    confirmRegenerate = false
                    vm.regenerate()
                }) { Text("Regenerate") }
            },
            dismissButton = { TextButton(onClick = { confirmRegenerate = false }) { Text("Cancel") } },
        )
    }

    if (confirmDelete && trip != null) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            icon = { Icon(Icons.Outlined.DeleteOutline, contentDescription = null) },
            title = { Text("Delete ${trip.entity.name}?") },
            text = {
                Text("The packing list and planned outfits go with it. Outfits you've logged as worn stay in your calendar.")
            },
            confirmButton = {
                TextButton(onClick = {
                    confirmDelete = false
                    vm.delete()
                }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Cancel") } },
        )
    }
}

// ---------------------------------------------------------------------------------------------
// Pieces
// ---------------------------------------------------------------------------------------------

@Composable
private fun TripHeader(trip: Trip, weather: TripWeather?, unit: TempUnit, modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    val e = trip.entity
    Column(modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(e.name, style = MaterialTheme.typography.displaySmall, modifier = Modifier.semantics { heading() })
        if (e.destination.isNotBlank()) {
            IconLine(Icons.Outlined.Place, e.destination)
        }
        IconLine(Icons.Outlined.CalendarMonth, "${dateRangeLabel(e.startDate, e.endDate)} · ${plural(trip.days, "day")}")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            InfoPill(tripTiming(e.startDate, e.endDate))
            e.occasions.sortedBy { it.ordinal }.forEach { InfoPill(it.label) }
            if (e.laundry) InfoPill("Laundry available")
        }
        if (weather != null) {
            Surface(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                shape = MaterialTheme.shapes.large,
                color = scheme.surfaceContainerLow,
                border = BorderStroke(1.dp, scheme.outlineVariant),
            ) {
                Row(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(44.dp).background(scheme.secondaryContainer, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(weatherIcon(weather.condition), contentDescription = null, tint = scheme.onSecondaryContainer)
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Lows of ${formatTemp(weather.minC, unit)}, highs of ${formatTemp(weather.maxC, unit)}",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        val parts = buildList {
                            add("Mostly ${weather.condition.label.lowercase()}")
                            if (weather.wetDays > 0) add("wet on ${plural(weather.wetDays, "day")}")
                            if (weather.coveredDays < weather.totalDays) {
                                add("forecast for ${weather.coveredDays} of ${weather.totalDays} days")
                            }
                        }
                        Text(parts.joinToString(" · "), style = MaterialTheme.typography.bodyMedium, color = scheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun IconLine(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun TripOutfitCard(entry: DayEntry, dayNumber: Int, itemsById: Map<Long, Item>, onClick: () -> Unit) {
    Column(
        Modifier
            .width(120.dp)
            .clip(MaterialTheme.shapes.large)
            .clickable(onClickLabel = "Open day", onClick = onClick),
    ) {
        ItemsCollage(entry.itemIds, itemsById, Modifier.fillMaxWidth().clearAndSetSemantics { })
        Text(
            "Day $dayNumber",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 8.dp),
        )
        Text(
            entry.date.shortLabel() + if (entry.worn) " · Worn" else "",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp),
        )
    }
}

@Composable
private fun PackingEmpty(regenerating: Boolean, onGenerate: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Luggage, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(12.dp))
                Text("No packing list yet", style = MaterialTheme.typography.titleSmall)
            }
            Text(
                "Build one from your closet, or add things below as you think of them.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            FilledTonalButton(onClick = onGenerate, enabled = !regenerating) {
                if (regenerating) {
                    CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(10.dp))
                    Text("Building…")
                } else {
                    Icon(Icons.Outlined.Autorenew, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Build packing list")
                }
            }
        }
    }
}

@Composable
private fun PackingRow(
    line: PackingItemEntity,
    item: Item?,
    onPackedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    Row(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .padding(start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            Modifier
                .weight(1f)
                .heightIn(min = 56.dp)
                .clip(MaterialTheme.shapes.medium)
                .toggleable(value = line.packed, role = Role.Checkbox, onValueChange = onPackedChange)
                .padding(end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = line.packed, onCheckedChange = null, modifier = Modifier.padding(horizontal = 12.dp))
            if (item != null) {
                ItemThumb(item, size = 36.dp)
                Spacer(Modifier.width(12.dp))
            }
            Column(Modifier.weight(1f).padding(vertical = 8.dp)) {
                val textColor = if (line.packed) scheme.onSurfaceVariant else scheme.onSurface
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        line.label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor,
                        textDecoration = if (line.packed) TextDecoration.LineThrough else null,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    if (line.quantity > 1) {
                        Spacer(Modifier.width(6.dp))
                        Text("×${line.quantity}", style = MaterialTheme.typography.labelLarge, color = scheme.primary)
                    }
                }
                if (!line.reason.isNullOrBlank()) {
                    Text(
                        line.reason,
                        style = MaterialTheme.typography.bodySmall,
                        color = scheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Outlined.Close, contentDescription = "Remove ${line.label}", tint = scheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AddPackingLine(onAdd: (String, PackCategory) -> Unit, modifier: Modifier = Modifier) {
    var text by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf(PackCategory.OTHER) }
    var menuOpen by remember { mutableStateOf(false) }
    val submit = {
        if (text.isNotBlank()) {
            onAdd(text, category)
            text = ""
        }
    }
    Column(modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Add item", style = MaterialTheme.typography.titleSmall, modifier = Modifier.semantics { heading() })
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("e.g. Phone charger") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { submit() }),
            modifier = Modifier.fillMaxWidth(),
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.weight(1f)) {
                OutlinedButton(onClick = { menuOpen = true }, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text(category.label, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, fill = false))
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Choose category")
                }
                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                    PackCategory.entries.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label) },
                            onClick = {
                                category = option
                                menuOpen = false
                            },
                        )
                    }
                }
            }
            Spacer(Modifier.width(12.dp))
            FilledTonalButton(onClick = submit, enabled = text.isNotBlank(), modifier = Modifier.heightIn(min = 48.dp)) {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Add")
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}
