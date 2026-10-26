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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.DesignServices
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.kintukka.hanger.AppContainer
import com.kintukka.hanger.data.DayEntry
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.data.Outfit
import com.kintukka.hanger.data.repo.TempUnit
import com.kintukka.hanger.ui.components.EmptyState
import com.kintukka.hanger.ui.components.ItemsCollage
import com.kintukka.hanger.ui.components.OutfitCollage
import com.kintukka.hanger.ui.components.SectionHeader
import com.kintukka.hanger.ui.components.weatherIcon
import com.kintukka.hanger.ui.formatTemp
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.longLabel
import com.kintukka.hanger.ui.navigation.BuilderRoute
import com.kintukka.hanger.ui.navigation.DayRoute
import com.kintukka.hanger.ui.navigation.ShuffleRoute
import com.kintukka.hanger.ui.navigation.SuggestRoute
import com.kintukka.hanger.ui.navigation.TripRoute
import com.kintukka.hanger.ui.plural
import com.kintukka.hanger.ui.relativeLabel
import com.kintukka.hanger.ui.shortLabel
import com.kintukka.hanger.weather.DayWeather
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

// ---------------------------------------------------------------------------------------------
// State
// ---------------------------------------------------------------------------------------------

@Immutable
internal data class DayLook(
    val entry: DayEntry,
    /** The saved outfit this look came from, when it still exists. */
    val outfit: Outfit?,
    val items: List<Item>,
    val tripName: String?,
)

@Immutable
internal data class DayUiState(
    val loading: Boolean = true,
    val looks: List<DayLook> = emptyList(),
    val itemsById: Map<Long, Item> = emptyMap(),
    val outfits: List<Outfit> = emptyList(),
    val tempUnit: TempUnit = TempUnit.CELSIUS,
)

internal class DayViewModel(private val date: LocalDate, private val container: AppContainer) : MessagingViewModel() {
    private val repo = container.repo

    val state: StateFlow<DayUiState> = combine(
        repo.allItems,
        repo.dayEntries,
        repo.outfits,
        repo.trips,
        container.settings.settings,
    ) { items, entries, outfits, trips, settings ->
        val itemsById = items.associateBy { it.id }
        val outfitsById = outfits.associateBy { it.id }
        val tripNames = trips.associate { it.id to it.entity.name }
        val looks = entries.filter { it.date == date }.map { entry ->
            DayLook(
                entry = entry,
                outfit = entry.entity.outfitId?.let { outfitsById[it] },
                items = entry.itemIds.mapNotNull { itemsById[it] },
                tripName = entry.entity.tripId?.let { tripNames[it] },
            )
        }
        DayUiState(
            loading = false,
            looks = looks,
            itemsById = itemsById,
            outfits = outfits.sortedWith(compareByDescending<Outfit> { it.entity.favorite }.thenByDescending { it.entity.createdAt }),
            tempUnit = settings.tempUnit,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DayUiState())

    private val _weather = MutableStateFlow<DayWeather?>(null)
    val weather: StateFlow<DayWeather?> = _weather.asStateFlow()

    init {
        // The forecast only covers roughly the next two weeks; don't hit the network for other days.
        if (overlapsForecast(date, date)) {
            viewModelScope.launch {
                _weather.value = catching { container.stylist.weatherFor(date) }.getOrNull()
            }
        }
    }

    fun setWorn(entryId: Long, worn: Boolean) = write("Couldn't update that look") {
        repo.setWorn(entryId, worn)
        say(if (worn) "Logged as worn" else "Marked as not worn")
    }

    fun remove(entryId: Long) = write("Couldn't remove that look") {
        repo.deleteEntry(entryId)
        say("Look removed")
    }

    /** Notes are saved on the app scope so a save started while leaving the screen still lands. */
    fun saveNote(entryId: Long, note: String) {
        container.appScope.launch { catching { repo.updateEntryNote(entryId, note) } }
    }

    fun addOutfit(outfit: Outfit, logAsWorn: Boolean) = write("Couldn't add ${outfit.name}") {
        if (logAsWorn) {
            repo.logWorn(date, outfit.itemIds, outfit.id, outfit.entity.occasion)
            say("Logged ${outfit.name} as worn")
        } else {
            repo.plan(date, outfit.itemIds, outfit.id, outfit.entity.occasion)
            say("Planned ${outfit.name}")
        }
    }
}

// ---------------------------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------------------------

@Composable
fun DayScreen(navController: NavHostController, date: LocalDate) {
    val vm = hangerViewModel(key = "day-$date") { DayViewModel(date, it) }
    val state by vm.state.collectAsStateWithLifecycle()
    val weather by vm.weather.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    CollectMessages(vm.messages, snackbar)

    val today = remember { LocalDate.now() }
    val canLogWorn = !date.isAfter(today)
    val dateEpoch = date.toEpochDay()

    var showOutfitSheet by rememberSaveable { mutableStateOf(false) }
    var pendingRemoveId by rememberSaveable { mutableLongStateOf(0L) }
    var pendingOutfitId by rememberSaveable { mutableLongStateOf(0L) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val goToDay: (LocalDate) -> Unit = { target ->
        navController.navigate(DayRoute(target.toEpochDay())) {
            popUpTo<DayRoute> { inclusive = true }
        }
    }

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
                    Column {
                        Text(
                            date.longLabel(),
                            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp, lineHeight = 26.sp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            date.relativeLabel(today),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { goToDay(date.minusDays(1)) }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous day")
                    }
                    IconButton(onClick = { goToDay(date.plusDays(1)) }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next day")
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            weather?.let { day ->
                item(key = "weather") { WeatherRow(day, state.tempUnit) }
            }

            if (!state.loading) {
                if (state.looks.isEmpty()) {
                    item(key = "empty") {
                        EmptyState(
                            icon = Icons.Outlined.EventAvailable,
                            title = if (date.isBefore(today)) "Nothing logged" else "Nothing planned yet",
                            body = if (date.isBefore(today)) {
                                "Add what you wore and it counts towards your wear stats."
                            } else {
                                "Pick a look below: a suggestion, a saved outfit, or something new."
                            },
                        )
                    }
                } else {
                    item(key = "looks-header") {
                        SectionHeader(
                            plural(state.looks.size, "look"),
                            Modifier.semantics { heading() },
                        )
                    }
                    items(state.looks, key = { "look-${it.entry.id}" }) { look ->
                        LookCard(
                            look = look,
                            itemsById = state.itemsById,
                            canLogWorn = canLogWorn,
                            onToggleWorn = { vm.setWorn(look.entry.id, !look.entry.worn) },
                            onEdit = {
                                navController.navigate(
                                    BuilderRoute(
                                        outfitId = look.entry.entity.outfitId ?: 0,
                                        dateEpoch = dateEpoch,
                                        itemIds = look.entry.itemIds.joinToString(","),
                                        entryId = look.entry.id,
                                    ),
                                )
                            },
                            onRemove = { pendingRemoveId = look.entry.id },
                            onSaveNote = { vm.saveNote(look.entry.id, it) },
                            onOpenTrip = look.entry.entity.tripId?.let { tripId -> { navController.navigate(TripRoute(tripId)) } },
                        )
                    }
                }
            }

            item(key = "add-header") {
                SectionHeader(
                    if (state.looks.isEmpty()) "Add a look" else "Add another look",
                    Modifier.semantics { heading() },
                )
            }
            item(key = "add-actions") {
                AddLookGrid(
                    onSuggest = { navController.navigate(SuggestRoute(dateEpoch = dateEpoch)) },
                    onPickSaved = { showOutfitSheet = true },
                    onBuild = { navController.navigate(BuilderRoute(dateEpoch = dateEpoch)) },
                    onShuffle = { navController.navigate(ShuffleRoute(dateEpoch = dateEpoch)) },
                )
            }
        }
    }

    if (showOutfitSheet) {
        SavedOutfitSheet(
            outfits = state.outfits,
            itemsById = state.itemsById,
            onDismiss = { showOutfitSheet = false },
            onBuild = {
                showOutfitSheet = false
                navController.navigate(BuilderRoute(dateEpoch = dateEpoch))
            },
            onPick = { outfit ->
                showOutfitSheet = false
                if (canLogWorn) pendingOutfitId = outfit.id else vm.addOutfit(outfit, logAsWorn = false)
            },
        )
    }

    state.outfits.firstOrNull { it.id == pendingOutfitId }?.let { outfit ->
        AlertDialog(
            onDismissRequest = { pendingOutfitId = 0L },
            icon = { Icon(Icons.Outlined.CheckCircle, contentDescription = null) },
            title = { Text(if (date == today) "Wearing it today?" else "Did you wear it?") },
            text = {
                Text(
                    "Log ${outfit.name} as worn on ${date.shortLabel()} to update wear counts and laundry, " +
                        "or just keep it on the calendar as a plan.",
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.addOutfit(outfit, logAsWorn = true)
                    pendingOutfitId = 0L
                }) { Text("Log as worn") }
            },
            dismissButton = {
                TextButton(onClick = {
                    vm.addOutfit(outfit, logAsWorn = false)
                    pendingOutfitId = 0L
                }) { Text("Just plan it") }
            },
        )
    }

    state.looks.firstOrNull { it.entry.id == pendingRemoveId }?.let { look ->
        AlertDialog(
            onDismissRequest = { pendingRemoveId = 0L },
            icon = { Icon(Icons.Outlined.DeleteOutline, contentDescription = null) },
            title = { Text("Remove this look?") },
            text = {
                Text(
                    if (look.entry.worn) {
                        "It comes off ${date.shortLabel()} and its wears are taken off your stats."
                    } else {
                        "It comes off ${date.shortLabel()}. Saved outfits aren't affected."
                    },
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.remove(look.entry.id)
                    pendingRemoveId = 0L
                }) { Text("Remove", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { pendingRemoveId = 0L }) { Text("Cancel") }
            },
        )
    }
}

// ---------------------------------------------------------------------------------------------
// Pieces
// ---------------------------------------------------------------------------------------------

@Composable
private fun WeatherRow(day: DayWeather, unit: TempUnit) {
    val scheme = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = scheme.surfaceContainerLow,
        border = BorderStroke(1.dp, scheme.outlineVariant),
    ) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(44.dp).background(scheme.secondaryContainer, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(weatherIcon(day.condition), contentDescription = null, tint = scheme.onSecondaryContainer)
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "${formatTemp(day.tempMinC, unit)} – ${formatTemp(day.tempMaxC, unit)}",
                    style = MaterialTheme.typography.titleMedium,
                )
                val rain = if (day.precipitationProbability >= 20) " · ${day.precipitationProbability}% chance of rain" else ""
                Text(
                    day.condition.label + rain,
                    style = MaterialTheme.typography.bodyMedium,
                    color = scheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun LookCard(
    look: DayLook,
    itemsById: Map<Long, Item>,
    canLogWorn: Boolean,
    onToggleWorn: () -> Unit,
    onEdit: () -> Unit,
    onRemove: () -> Unit,
    onSaveNote: (String) -> Unit,
    onOpenTrip: (() -> Unit)?,
) {
    val scheme = MaterialTheme.colorScheme
    val entry = look.entry
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = scheme.surfaceContainerLowest,
        border = BorderStroke(1.dp, scheme.outlineVariant),
    ) {
        Column(Modifier.padding(14.dp)) {
            Row {
                val collageModifier = Modifier.width(118.dp)
                if (look.outfit != null && look.outfit.placements.isNotEmpty()) {
                    OutfitCollage(look.outfit.placements, itemsById, collageModifier, shape = MaterialTheme.shapes.medium)
                } else {
                    ItemsCollage(entry.itemIds, itemsById, collageModifier, shape = MaterialTheme.shapes.medium)
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        WornBadge(entry.worn)
                        entry.entity.occasion?.let { InfoPill(it.label) }
                    }
                    Text(
                        look.outfit?.name ?: if (look.items.isEmpty()) "Look" else plural(look.items.size, "piece"),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (look.items.isEmpty()) {
                        Text(
                            "These pieces are no longer in your closet.",
                            style = MaterialTheme.typography.bodySmall,
                            color = scheme.onSurfaceVariant,
                        )
                    } else {
                        Text(
                            look.items.joinToString("\n") { it.name },
                            style = MaterialTheme.typography.bodySmall,
                            color = scheme.onSurfaceVariant,
                            maxLines = 6,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    if (look.tripName != null) {
                        Row(
                            Modifier
                                .then(if (onOpenTrip != null) Modifier.clickable(onClickLabel = "Open trip", onClick = onOpenTrip) else Modifier)
                                .heightIn(min = 32.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(Icons.Outlined.Luggage, contentDescription = null, tint = scheme.primary, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(
                                look.tripName,
                                style = MaterialTheme.typography.labelLarge,
                                color = scheme.primary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            NoteField(entryId = entry.id, savedNote = entry.entity.note, onSave = onSaveNote)
            Spacer(Modifier.height(8.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Logging a future day as worn would skew wear stats, so the toggle only shows from the day itself.
                if (canLogWorn) {
                    val toggleModifier = Modifier.weight(1f, fill = false)
                    if (entry.worn) {
                        OutlinedButton(onClick = onToggleWorn, modifier = toggleModifier) {
                            Icon(Icons.AutoMirrored.Outlined.Undo, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Not worn", maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    } else {
                        FilledTonalButton(onClick = onToggleWorn, modifier = toggleModifier) {
                            Icon(Icons.Outlined.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Mark as worn", maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                } else {
                    Spacer(Modifier.width(1.dp))
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Edit look", tint = scheme.onSurfaceVariant)
                    }
                    IconButton(onClick = onRemove) {
                        Icon(Icons.Outlined.DeleteOutline, contentDescription = "Remove look", tint = scheme.error)
                    }
                }
            }
        }
    }
}

/** Note field that saves after a short pause in typing, on IME "done" and when focus leaves. */
@Composable
private fun NoteField(entryId: Long, savedNote: String, onSave: (String) -> Unit) {
    var text by rememberSaveable(entryId) { mutableStateOf(savedNote) }
    var lastSaved by rememberSaveable(entryId) { mutableStateOf(savedNote) }
    val focusManager = LocalFocusManager.current

    fun commit() {
        if (text != lastSaved) {
            lastSaved = text
            onSave(text)
        }
    }

    LaunchedEffect(text) {
        if (text != lastSaved) {
            delay(800)
            commit()
        }
    }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { if (!it.isFocused) commit() },
        placeholder = { Text("Add a note: where you wore it, how it felt") },
        leadingIcon = { Icon(Icons.AutoMirrored.Outlined.Notes, contentDescription = null) },
        textStyle = MaterialTheme.typography.bodyMedium,
        maxLines = 4,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            commit()
            focusManager.clearFocus()
        }),
        shape = MaterialTheme.shapes.medium,
    )
}

private data class AddAction(
    val icon: ImageVector,
    val title: String,
    val body: String,
    val highlight: Boolean,
    val onClick: () -> Unit,
)

@Composable
private fun AddLookGrid(onSuggest: () -> Unit, onPickSaved: () -> Unit, onBuild: () -> Unit, onShuffle: () -> Unit) {
    val actions = listOf(
        AddAction(Icons.Outlined.AutoAwesome, "Get suggestions", "Weather-aware picks", highlight = true, onClick = onSuggest),
        AddAction(Icons.Outlined.Bookmarks, "Pick a saved outfit", "From your outfits", highlight = false, onClick = onPickSaved),
        AddAction(Icons.Outlined.DesignServices, "Build an outfit", "Arrange pieces yourself", highlight = false, onClick = onBuild),
        AddAction(Icons.Outlined.Shuffle, "Shuffle", "Let chance decide", highlight = false, onClick = onShuffle),
    )
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        actions.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { action -> AddLookCard(action, Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun AddLookCard(action: AddAction, modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    Surface(
        onClick = action.onClick,
        modifier = modifier.heightIn(min = 104.dp),
        shape = MaterialTheme.shapes.large,
        color = if (action.highlight) scheme.primaryContainer else scheme.surfaceContainerHigh,
        contentColor = if (action.highlight) scheme.onPrimaryContainer else scheme.onSurface,
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(
                action.icon,
                contentDescription = null,
                tint = if (action.highlight) scheme.onPrimaryContainer else scheme.primary,
            )
            Spacer(Modifier.height(6.dp))
            Text(action.title, style = MaterialTheme.typography.titleSmall)
            Text(
                action.body,
                style = MaterialTheme.typography.bodySmall,
                color = if (action.highlight) scheme.onPrimaryContainer.copy(alpha = 0.8f) else scheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SavedOutfitSheet(
    outfits: List<Outfit>,
    itemsById: Map<Long, Item>,
    onDismiss: () -> Unit,
    onBuild: () -> Unit,
    onPick: (Outfit) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    // Animate the sheet away before running the follow-up action.
    val closeThen: (() -> Unit) -> Unit = { then ->
        scope.launch { sheetState.hide() }.invokeOnCompletion { then() }
    }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(Modifier.fillMaxWidth()) {
            Text(
                "Pick a saved outfit",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 20.dp).semantics { heading() },
            )
            Spacer(Modifier.height(12.dp))
            if (outfits.isEmpty()) {
                EmptyState(
                    icon = Icons.Outlined.Style,
                    title = "No saved outfits yet",
                    body = "Outfits you build or save from suggestions show up here.",
                    action = {
                        Button(onClick = { closeThen(onBuild) }) { Text("Build an outfit") }
                    },
                )
                Spacer(Modifier.height(24.dp))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 140.dp),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(outfits, key = { it.id }) { outfit ->
                        OutfitPickCard(outfit, itemsById, onClick = { closeThen { onPick(outfit) } })
                    }
                }
            }
        }
    }
}

@Composable
private fun OutfitPickCard(outfit: Outfit, itemsById: Map<Long, Item>, onClick: () -> Unit) {
    Column(
        Modifier
            .clip(MaterialTheme.shapes.large)
            .clickable(onClickLabel = "Choose outfit", onClick = onClick),
    ) {
        if (outfit.placements.isNotEmpty()) {
            OutfitCollage(outfit.placements, itemsById, Modifier.fillMaxWidth().clearAndSetSemantics { })
        } else {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh, MaterialTheme.shapes.large),
            )
        }
        Text(
            outfit.name,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 8.dp),
        )
        Text(
            listOfNotNull(outfit.entity.occasion?.label, plural(outfit.items.size, "piece")).joinToString(" · "),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp),
        )
    }
}
