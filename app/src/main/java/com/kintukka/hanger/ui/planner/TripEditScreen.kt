@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.kintukka.hanger.ui.planner

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocalLaundryService
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.kintukka.hanger.AppContainer
import com.kintukka.hanger.data.db.TripEntity
import com.kintukka.hanger.engine.Occasion
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.navigation.TripEditRoute
import com.kintukka.hanger.ui.navigation.TripRoute
import com.kintukka.hanger.ui.plural
import com.kintukka.hanger.weather.Place
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

// ---------------------------------------------------------------------------------------------
// State
// ---------------------------------------------------------------------------------------------

@Immutable
internal data class TripForm(
    val name: String = "",
    /** Destination text as typed (or the chosen place's label). */
    val destination: String = "",
    /** Label of the place picked from search; coordinates are only valid while the text still matches it. */
    val placeLabel: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val start: LocalDate = LocalDate.now().plusDays(7),
    val end: LocalDate = LocalDate.now().plusDays(10),
    val occasions: Set<Occasion> = setOf(Occasion.CASUAL),
    val laundry: Boolean = false,
) {
    val hasPlace: Boolean get() = placeLabel != null && latitude != null && longitude != null
    val days: Int get() = (end.toEpochDay() - start.toEpochDay() + 1).toInt().coerceAtLeast(1)
    val isValid: Boolean get() = name.isNotBlank() && !end.isBefore(start)
}

@Immutable
internal data class PlaceSearch(
    val searching: Boolean = false,
    val results: List<Place> = emptyList(),
    /** True once a search finished for the current text, so "no results" can be shown. */
    val searched: Boolean = false,
)

@Immutable
internal data class TripEditUiState(
    val loading: Boolean = true,
    val isNew: Boolean = true,
    val form: TripForm = TripForm(),
    val search: PlaceSearch = PlaceSearch(),
    val saving: Boolean = false,
    val generating: Boolean = false,
) {
    val busy: Boolean get() = saving || generating
}

internal sealed interface TripEditEvent {
    data class Saved(val tripId: Long, val message: String?) : TripEditEvent
    data object Missing : TripEditEvent
}

internal class TripEditViewModel(private val id: Long, private val container: AppContainer) : MessagingViewModel() {
    private val repo = container.repo
    private val isNew = id <= 0

    private val form = MutableStateFlow(TripForm())
    /** What the user typed into the destination field; only this drives place search. */
    private val query = MutableStateFlow("")
    private val search = MutableStateFlow(PlaceSearch())
    private val progress = MutableStateFlow(Progress(loading = !isNew))
    private var original: TripEntity? = null

    private data class Progress(val loading: Boolean = false, val saving: Boolean = false, val generating: Boolean = false)

    val state: StateFlow<TripEditUiState> = combine(form, search, progress) { f, s, p ->
        TripEditUiState(
            loading = p.loading,
            isNew = isNew,
            form = f,
            search = s,
            saving = p.saving,
            generating = p.generating,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TripEditUiState(loading = !isNew, isNew = isNew))

    private val _events = Channel<TripEditEvent>(Channel.BUFFERED)
    val events: Flow<TripEditEvent> = _events.receiveAsFlow()

    init {
        if (!isNew) {
            viewModelScope.launch {
                val trip = catching { repo.tripEntity(id) }.getOrNull()
                if (trip == null) {
                    _events.send(TripEditEvent.Missing)
                    return@launch
                }
                original = trip
                form.value = TripForm(
                    name = trip.name,
                    destination = trip.destination,
                    placeLabel = trip.destination.takeIf { trip.latitude != null && trip.longitude != null },
                    latitude = trip.latitude,
                    longitude = trip.longitude,
                    start = trip.startDate,
                    end = trip.endDate,
                    occasions = trip.occasions.ifEmpty { setOf(Occasion.CASUAL) },
                    laundry = trip.laundry,
                )
                progress.update { it.copy(loading = false) }
            }
        }
        // Debounced place search as the user types a destination.
        viewModelScope.launch {
            query.collectLatest { raw ->
                val text = raw.trim()
                if (text.length < 2) {
                    search.value = PlaceSearch()
                    return@collectLatest
                }
                delay(400)
                search.update { it.copy(searching = true, searched = false) }
                val results = catching { container.weather.searchPlaces(text) }.getOrDefault(emptyList())
                search.value = PlaceSearch(searching = false, results = results, searched = true)
            }
        }
    }

    fun setName(value: String) = form.update { it.copy(name = value) }

    fun setDestination(value: String) {
        form.update {
            // Editing the text after picking a place unlinks its coordinates.
            if (value == it.placeLabel) it.copy(destination = value)
            else it.copy(destination = value, placeLabel = null, latitude = null, longitude = null)
        }
        query.value = if (value == form.value.placeLabel) "" else value
    }

    fun pickPlace(place: Place) {
        form.update {
            it.copy(destination = place.label, placeLabel = place.label, latitude = place.latitude, longitude = place.longitude)
        }
        query.value = ""
    }

    fun clearDestination() {
        form.update { it.copy(destination = "", placeLabel = null, latitude = null, longitude = null) }
        query.value = ""
    }

    fun setDates(start: LocalDate, end: LocalDate) = form.update {
        if (end.isBefore(start)) it.copy(start = end, end = start) else it.copy(start = start, end = end)
    }

    fun toggleOccasion(occasion: Occasion) = form.update {
        val next = if (occasion in it.occasions) it.occasions - occasion else it.occasions + occasion
        // Always keep at least one occasion.
        if (next.isEmpty()) it else it.copy(occasions = next)
    }

    fun setLaundry(value: Boolean) = form.update { it.copy(laundry = value) }

    fun save() {
        val f = form.value
        if (!f.isValid || progress.value.saving || progress.value.generating) return
        viewModelScope.launch {
            progress.update { it.copy(saving = true) }
            val base = original
            val entity = (base ?: TripEntity(name = f.name.trim(), startDate = f.start, endDate = f.end)).copy(
                name = f.name.trim(),
                destination = f.destination.trim(),
                latitude = if (f.hasPlace) f.latitude else null,
                longitude = if (f.hasPlace) f.longitude else null,
                startDate = f.start,
                endDate = f.end,
                occasions = f.occasions,
                laundry = f.laundry,
            )
            val tripId = catching { repo.saveTrip(entity) }.getOrElse {
                progress.update { it.copy(saving = false) }
                say("Couldn't save the trip. Please try again.")
                return@launch
            }
            val saved = entity.copy(id = tripId)
            original = saved

            // Editing only the name shouldn't throw away a packing list someone has been ticking off.
            val planInputsChanged = base == null ||
                base.startDate != saved.startDate || base.endDate != saved.endDate ||
                base.latitude != saved.latitude || base.longitude != saved.longitude ||
                base.occasions != saved.occasions || base.laundry != saved.laundry
            val hasPacking = !isNew && catching { repo.trip(tripId).first()?.packing?.isNotEmpty() == true }.getOrDefault(false)

            var message: String? = null
            if (planInputsChanged || !hasPacking) {
                progress.update { it.copy(saving = false, generating = true) }
                val result = generatePackingPlan(saved, repo, container.stylist, container.settings)
                if (result.isFailure) {
                    message = "Trip saved, but the packing list couldn't be built. Try Regenerate from the menu."
                }
            }
            progress.update { it.copy(saving = false, generating = false) }
            _events.send(TripEditEvent.Saved(tripId, message))
        }
    }
}

// ---------------------------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------------------------

@Composable
fun TripEditScreen(navController: NavHostController, id: Long) {
    val vm = hangerViewModel(key = "trip-edit-$id") { TripEditViewModel(id, it) }
    val state by vm.state.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    CollectMessages(vm.messages, snackbar)
    val popSelf = rememberPopSelf(navController)

    LaunchedEffect(vm) {
        vm.events.collect { event ->
            when (event) {
                TripEditEvent.Missing -> popSelf()
                is TripEditEvent.Saved -> {
                    navController.navigate(TripRoute(event.tripId)) {
                        popUpTo<TripEditRoute> { inclusive = true }
                        launchSingleTop = true
                    }
                    if (event.message != null) {
                        // Hand the message to the trip screen; it shows it as a snackbar.
                        catching { navController.currentBackStackEntry?.let { it.savedStateHandle[PLANNER_MESSAGE_KEY] = event.message } }
                    }
                }
            }
        }
    }

    // Don't let a half-generated trip be abandoned mid-way.
    BackHandler(enabled = state.busy) { }

    var showDates by rememberSaveable { mutableStateOf(false) }
    val form = state.form

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            Column {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }, enabled = !state.busy) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    title = { Text(if (state.isNew) "New trip" else "Edit trip") },
                )
                if (state.busy || state.loading) {
                    LinearProgressIndicator(Modifier.fillMaxWidth())
                } else {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surface) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                ) {
                    Button(
                        onClick = vm::save,
                        enabled = form.isValid && !state.busy && !state.loading,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                    ) {
                        if (state.busy) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = LocalContentColor.current,
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(if (state.generating) "Building your packing list…" else "Saving…")
                        } else {
                            Text(if (state.isNew) "Save and build packing list" else "Save changes")
                        }
                    }
                }
            }
        },
    ) { padding ->
        if (state.loading) {
            Box(Modifier.fillMaxSize().padding(padding))
            return@Scaffold
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            OutlinedTextField(
                value = form.name,
                onValueChange = vm::setName,
                label = { Text("Trip name") },
                placeholder = { Text("e.g. Lisbon long weekend") },
                singleLine = true,
                enabled = !state.busy,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
            )

            DestinationField(
                form = form,
                search = state.search,
                enabled = !state.busy,
                onChange = vm::setDestination,
                onClear = vm::clearDestination,
                onPick = vm::pickPlace,
            )

            DatesField(form, enabled = !state.busy, onClick = { showDates = true })

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Dressing for", style = MaterialTheme.typography.titleSmall, modifier = Modifier.semantics { heading() })
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Occasion.entries.forEach { occasion ->
                        val selected = occasion in form.occasions
                        FilterChip(
                            selected = selected,
                            onClick = { vm.toggleOccasion(occasion) },
                            enabled = !state.busy,
                            label = { Text(occasion.label) },
                            leadingIcon = if (selected) {
                                { Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
                            } else {
                                null
                            },
                        )
                    }
                }
            }

            LaundryRow(form.laundry, enabled = !state.busy, onChange = vm::setLaundry)

            Text(
                "Hanger builds a packing list and an outfit for each day from your clean clothes" +
                    if (form.hasPlace) ", using the forecast for ${form.placeLabel}." else ".",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
        }
    }

    if (showDates) {
        TripDatesDialog(
            start = form.start,
            end = form.end,
            onDismiss = { showDates = false },
            onConfirm = { start, end ->
                vm.setDates(start, end)
                showDates = false
            },
        )
    }
}

@Composable
private fun DestinationField(
    form: TripForm,
    search: PlaceSearch,
    enabled: Boolean,
    onChange: (String) -> Unit,
    onClear: () -> Unit,
    onPick: (Place) -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = form.destination,
            onValueChange = onChange,
            label = { Text("Destination") },
            placeholder = { Text("Search for a city") },
            singleLine = true,
            enabled = enabled,
            leadingIcon = { Icon(Icons.Outlined.Place, contentDescription = null) },
            trailingIcon = when {
                search.searching -> {
                    { CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp) }
                }
                form.destination.isNotEmpty() -> {
                    {
                        IconButton(onClick = onClear, enabled = enabled) {
                            Icon(Icons.Outlined.Close, contentDescription = "Clear destination")
                        }
                    }
                }
                else -> null
            },
            supportingText = {
                Text(
                    when {
                        form.hasPlace -> "Forecast linked for packing"
                        form.destination.isNotBlank() -> "Pick a place from the list to plan around its forecast"
                        else -> "Optional. With a place, packing follows the forecast."
                    },
                )
            },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Search),
            modifier = Modifier.fillMaxWidth(),
        )
        when {
            search.results.isNotEmpty() -> Surface(
                shape = MaterialTheme.shapes.medium,
                color = scheme.surfaceContainerLow,
                border = BorderStroke(1.dp, scheme.outlineVariant),
            ) {
                Column {
                    search.results.forEachIndexed { index, place ->
                        if (index > 0) HorizontalDivider(color = scheme.outlineVariant)
                        PlaceRow(place, onClick = { onPick(place) })
                    }
                }
            }
            search.searched && !form.hasPlace -> Text(
                "No places found. You can keep the name as typed.",
                style = MaterialTheme.typography.bodySmall,
                color = scheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
private fun PlaceRow(place: Place, onClick: () -> Unit) {
    Surface(onClick = onClick, color = MaterialTheme.colorScheme.surfaceContainerLow) {
        Row(
            Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Outlined.Place, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(place.name, style = MaterialTheme.typography.bodyLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
                val detail = listOf(place.region, place.country).filter { it.isNotBlank() && it != place.name }.joinToString(", ")
                if (detail.isNotEmpty()) {
                    Text(
                        detail,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
private fun DatesField(form: TripForm, enabled: Boolean, onClick: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.extraSmall,
        color = scheme.surface,
        border = BorderStroke(1.dp, scheme.outline),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.heightIn(min = 64.dp).padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Outlined.DateRange, contentDescription = null, tint = scheme.onSurfaceVariant)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text("Dates", style = MaterialTheme.typography.labelMedium, color = scheme.onSurfaceVariant)
                Text(
                    "${dateRangeLabel(form.start, form.end)} · ${plural(form.days, "day")}",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Text("Change", style = MaterialTheme.typography.labelLarge, color = scheme.primary)
        }
    }
}

@Composable
private fun LaundryRow(checked: Boolean, enabled: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .toggleable(value = checked, enabled = enabled, role = Role.Switch, onValueChange = onChange),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Outlined.LocalLaundryService, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text("Laundry available", style = MaterialTheme.typography.bodyLarge)
            Text(
                "Pack fewer clothes and re-wear them",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.width(16.dp))
        Switch(checked = checked, onCheckedChange = null, enabled = enabled)
    }
}

@Composable
private fun TripDatesDialog(
    start: LocalDate,
    end: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate, LocalDate) -> Unit,
) {
    val pickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = start.toUtcMillis(),
        initialSelectedEndDateMillis = end.toUtcMillis(),
        initialDisplayedMonthMillis = start.toUtcMillis(),
    )
    val pickedStart = pickerState.selectedStartDateMillis
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = pickedStart != null,
                onClick = {
                    val s = pickedStart?.utcMillisToDate() ?: return@TextButton
                    val e = pickerState.selectedEndDateMillis?.utcMillisToDate() ?: s
                    onConfirm(s, e)
                },
            ) { Text("OK") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    ) {
        DateRangePicker(
            state = pickerState,
            modifier = Modifier.weight(1f),
            title = {
                Text("Trip dates", modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp))
            },
            showModeToggle = true,
        )
    }
}

/** Material date pickers work in UTC midnight millis. */
private fun LocalDate.toUtcMillis(): Long = atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

private fun Long.utcMillisToDate(): LocalDate = Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
