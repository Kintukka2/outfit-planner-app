@file:OptIn(ExperimentalMaterial3Api::class)

package com.kintukka.hanger.ui.planner

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.kintukka.hanger.data.DayEntry
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.data.Trip
import com.kintukka.hanger.data.repo.WardrobeRepository
import com.kintukka.hanger.ui.components.EmptyState
import com.kintukka.hanger.ui.components.ItemsCollage
import com.kintukka.hanger.ui.components.PageTitle
import com.kintukka.hanger.ui.components.SectionHeader
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.longLabel
import com.kintukka.hanger.ui.monthLabel
import com.kintukka.hanger.ui.navigation.DayRoute
import com.kintukka.hanger.ui.navigation.TripEditRoute
import com.kintukka.hanger.ui.navigation.TripRoute
import com.kintukka.hanger.ui.plural
import com.kintukka.hanger.ui.relativeLabel
import com.kintukka.hanger.ui.shortLabel
import com.kintukka.hanger.ui.theme.HangerTokens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

// ---------------------------------------------------------------------------------------------
// State
// ---------------------------------------------------------------------------------------------

/** What a calendar cell needs to know about a day. */
@Immutable
internal data class DayMark(
    /** Garment shown as the cell thumbnail (the "main" piece of the first look). */
    val thumbnail: Item?,
    val worn: Boolean,
    val looks: Int,
)

@Immutable
internal data class PlannerUiState(
    val loading: Boolean = true,
    val today: LocalDate = LocalDate.now(),
    val itemsById: Map<Long, Item> = emptyMap(),
    val marks: Map<LocalDate, DayMark> = emptyMap(),
    val streak: Int = 0,
    val loggedToday: Boolean = false,
    val comingUp: List<DayEntry> = emptyList(),
    val upcomingTrips: List<Trip> = emptyList(),
    val pastTrips: List<Trip> = emptyList(),
    val tripNames: Map<Long, String> = emptyMap(),
) {
    val hasTrips: Boolean get() = upcomingTrips.isNotEmpty() || pastTrips.isNotEmpty()
}

internal class PlannerViewModel(repo: WardrobeRepository) : ViewModel() {
    private val _month = MutableStateFlow(YearMonth.now())
    val month: StateFlow<YearMonth> = _month.asStateFlow()

    fun previousMonth() = _month.update { it.minusMonths(1) }
    fun nextMonth() = _month.update { it.plusMonths(1) }
    fun thisMonth() {
        _month.value = YearMonth.now()
    }

    val state: StateFlow<PlannerUiState> = combine(repo.allItems, repo.dayEntries, repo.trips) { items, entries, trips ->
        buildState(LocalDate.now(), items, entries, trips)
    }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PlannerUiState())

    companion object {
        fun buildState(today: LocalDate, items: List<Item>, entries: List<DayEntry>, trips: List<Trip>): PlannerUiState {
            val itemsById = items.associateBy { it.id }

            val marks = entries.groupBy { it.date }.mapValues { (_, dayEntries) ->
                val lead = dayEntries.firstOrNull { it.worn } ?: dayEntries.first()
                val thumbnail = lead.itemIds.mapNotNull { itemsById[it] }.minByOrNull { it.category.order }
                DayMark(thumbnail = thumbnail, worn = dayEntries.any { it.worn }, looks = dayEntries.size)
            }

            val wornDates = entries.asSequence().filter { it.worn }.map { it.date }.toHashSet()
            var cursor = if (today in wornDates) today else today.minusDays(1)
            var streak = 0
            while (cursor in wornDates) {
                streak++
                cursor = cursor.minusDays(1)
            }

            val horizon = today.plusDays(13)
            val comingUp = entries
                .filter { !it.worn && !it.date.isBefore(today) && !it.date.isAfter(horizon) }
                .sortedWith(compareBy({ it.date }, { it.entity.createdAt }))

            val (upcoming, past) = trips.partition { !it.entity.endDate.isBefore(today) }

            return PlannerUiState(
                loading = false,
                today = today,
                itemsById = itemsById,
                marks = marks,
                streak = streak,
                loggedToday = today in wornDates,
                comingUp = comingUp,
                upcomingTrips = upcoming.sortedBy { it.entity.startDate },
                pastTrips = past.sortedByDescending { it.entity.startDate },
                tripNames = trips.associate { it.id to it.entity.name },
            )
        }
    }
}

// ---------------------------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------------------------

private const val TAB_CALENDAR = 0
private const val TAB_TRIPS = 1

@Composable
fun PlannerScreen(navController: NavHostController) {
    val vm = hangerViewModel { PlannerViewModel(it.repo) }
    val state by vm.state.collectAsStateWithLifecycle()
    val month by vm.month.collectAsStateWithLifecycle()
    var tab by rememberSaveable { mutableIntStateOf(TAB_CALENDAR) }

    val openDay: (LocalDate) -> Unit = { navController.navigate(DayRoute(it.toEpochDay())) }
    val newTrip: () -> Unit = { navController.navigate(TripEditRoute(0)) }

    Scaffold(
        contentWindowInsets = WindowInsets.statusBars,
        floatingActionButton = {
            if (tab == TAB_TRIPS && !state.loading) {
                ExtendedFloatingActionButton(
                    onClick = newTrip,
                    icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                    text = { Text("New trip") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = if (tab == TAB_TRIPS) 104.dp else 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(key = "title") {
                PageTitle("Plan", subtitle = state.today.longLabel())
            }
            item(key = "tabs") {
                PlannerTabs(tab, onSelect = { tab = it }, modifier = Modifier.padding(top = 4.dp, bottom = 4.dp))
            }
            if (tab == TAB_CALENDAR) {
                calendarSection(
                    state = state,
                    month = month,
                    onPrevious = vm::previousMonth,
                    onNext = vm::nextMonth,
                    onToday = vm::thisMonth,
                    onOpenDay = openDay,
                )
            } else {
                tripsSection(
                    state = state,
                    onOpenTrip = { navController.navigate(TripRoute(it)) },
                )
            }
        }
    }
}

@Composable
private fun PlannerTabs(selected: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    val options = listOf("Calendar" to Icons.Outlined.CalendarMonth, "Trips" to Icons.Outlined.Luggage)
    SingleChoiceSegmentedButtonRow(modifier.fillMaxWidth()) {
        options.forEachIndexed { index, (label, icon) ->
            SegmentedButton(
                selected = selected == index,
                onClick = { onSelect(index) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                icon = { Icon(icon, contentDescription = null, modifier = Modifier.size(SegmentedButtonDefaults.IconSize)) },
                label = { Text(label) },
            )
        }
    }
}

// ---------------------------------------------------------------------------------------------
// Calendar tab
// ---------------------------------------------------------------------------------------------

private fun LazyListScope.calendarSection(
    state: PlannerUiState,
    month: YearMonth,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onToday: () -> Unit,
    onOpenDay: (LocalDate) -> Unit,
) {
    item(key = "month-header") {
        MonthHeader(month, showToday = month != YearMonth.from(state.today), onPrevious, onNext, onToday)
    }
    item(key = "weekdays") { WeekdayHeader() }
    item(key = "grid") {
        AnimatedContent(
            targetState = month,
            transitionSpec = {
                val forward = targetState > initialState
                (slideInHorizontally { width -> if (forward) width / 5 else -width / 5 } + fadeIn()) togetherWith fadeOut()
            },
            label = "month",
        ) { shown ->
            MonthGrid(shown, state.today, state.marks, onOpenDay)
        }
    }
    item(key = "streak") {
        StreakCard(
            streak = state.streak,
            loggedToday = state.loggedToday,
            onClick = { onOpenDay(state.today) },
            modifier = Modifier.padding(top = 8.dp),
        )
    }
    item(key = "coming-up-header") {
        SectionHeader("Coming up", Modifier.semantics { heading() })
    }
    if (state.comingUp.isEmpty()) {
        item(key = "coming-up-empty") {
            ComingUpEmpty(
                loading = state.loading,
                onPlanTomorrow = { onOpenDay(state.today.plusDays(1)) },
            )
        }
    } else {
        items(state.comingUp, key = { "up-${it.id}" }) { entry ->
            ComingUpRow(
                entry = entry,
                itemsById = state.itemsById,
                tripName = entry.entity.tripId?.let { state.tripNames[it] },
                today = state.today,
                onClick = { onOpenDay(entry.date) },
            )
        }
    }
}

@Composable
private fun MonthHeader(
    month: YearMonth,
    showToday: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onToday: () -> Unit,
) {
    Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(
            month.atDay(1).monthLabel(),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.weight(1f).semantics { heading() },
        )
        if (showToday) {
            TextButton(onClick = onToday) { Text("Today") }
        }
        IconButton(onClick = onPrevious) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous month")
        }
        IconButton(onClick = onNext) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next month")
        }
    }
}

@Composable
private fun WeekdayHeader() {
    // Cells announce their full date, so the initials are hidden from accessibility services.
    Row(Modifier.fillMaxWidth().clearAndSetSemantics { }) {
        DayOfWeek.entries.forEach { day ->
            Text(
                day.getDisplayName(TextStyle.NARROW, Locale.UK),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun MonthGrid(month: YearMonth, today: LocalDate, marks: Map<LocalDate, DayMark>, onOpenDay: (LocalDate) -> Unit) {
    val first = month.atDay(1)
    val gridStart = remember(month) { first.minusDays((first.dayOfWeek.value - DayOfWeek.MONDAY.value).toLong()) }
    Column(Modifier.fillMaxWidth()) {
        repeat(6) { week ->
            Row(Modifier.fillMaxWidth()) {
                repeat(7) { dayIndex ->
                    val date = gridStart.plusDays((week * 7 + dayIndex).toLong())
                    DayCell(
                        date = date,
                        inMonth = YearMonth.from(date) == month,
                        isToday = date == today,
                        mark = marks[date],
                        onClick = { onOpenDay(date) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    inMonth: Boolean,
    isToday: Boolean,
    mark: DayMark?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scheme = MaterialTheme.colorScheme
    val description = buildString {
        append(date.longLabel())
        if (isToday) append(", today")
        if (mark != null) {
            append(", ")
            append(if (mark.worn) "outfit logged" else plural(mark.looks, "look") + " planned")
        }
    }
    Column(
        modifier
            .height(66.dp)
            .clip(MaterialTheme.shapes.small)
            .clickable(onClickLabel = "Open day", role = Role.Button, onClick = onClick)
            .semantics { contentDescription = description }
            .alpha(if (inMonth) 1f else 0.45f)
            .padding(top = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (isToday) scheme.primaryContainer else Color.Transparent)
                .clearAndSetSemantics { },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                date.dayOfMonth.toString(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
                color = if (isToday) scheme.onPrimaryContainer else scheme.onSurface,
            )
        }
        if (mark != null) {
            Spacer(Modifier.height(2.dp))
            ItemThumb(mark.thumbnail, size = 22.dp, padding = 2.dp)
            Spacer(Modifier.height(4.dp))
            Box(
                Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .then(
                        if (mark.worn) Modifier.background(scheme.primary)
                        else Modifier.border(1.dp, scheme.primary, CircleShape),
                    ),
            )
        }
    }
}

@Composable
private fun StreakCard(streak: Int, loggedToday: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    val title = if (streak > 0) "$streak-day streak" else "No streak yet"
    val body = when {
        loggedToday -> "Today's outfit is logged. See you tomorrow."
        streak > 0 -> "Log today to keep it going"
        else -> "Log what you wear today to start one"
    }
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = scheme.secondaryContainer,
        contentColor = scheme.onSecondaryContainer,
    ) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(44.dp).clip(CircleShape).background(scheme.surfaceContainerLowest.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Outlined.LocalFireDepartment, contentDescription = null, tint = scheme.secondary)
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(body, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun ComingUpEmpty(loading: Boolean, onPlanTomorrow: () -> Unit) {
    if (loading) return
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(Modifier.padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Nothing planned for the next two weeks.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = onPlanTomorrow) { Text("Plan tomorrow") }
        }
    }
}

@Composable
private fun ComingUpRow(
    entry: DayEntry,
    itemsById: Map<Long, Item>,
    tripName: String?,
    today: LocalDate,
    onClick: () -> Unit,
) {
    val names = entry.itemIds.mapNotNull { itemsById[it]?.name }
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            ItemsCollage(
                entry.itemIds,
                itemsById,
                modifier = Modifier.width(64.dp).clearAndSetSemantics { },
                shape = MaterialTheme.shapes.medium,
            )
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    "${entry.date.shortLabel()} · ${entry.date.relativeLabel(today)}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    names.joinToString(", ").ifEmpty { "Items no longer in your closet" },
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                val extra = listOfNotNull(entry.entity.occasion?.label, tripName?.let { "Trip · $it" })
                if (extra.isNotEmpty()) {
                    Text(
                        extra.joinToString(" · "),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// Trips tab
// ---------------------------------------------------------------------------------------------

private fun LazyListScope.tripsSection(
    state: PlannerUiState,
    onOpenTrip: (Long) -> Unit,
) {
    if (state.loading) return
    if (!state.hasTrips) {
        item(key = "trips-empty") {
            EmptyState(
                icon = Icons.Outlined.Luggage,
                title = "No trips yet",
                body = "Tap New trip and Hanger builds a packing list and an outfit for each day " +
                    "from your closet and the forecast.",
            )
        }
        return
    }
    if (state.upcomingTrips.isNotEmpty()) {
        item(key = "trips-upcoming-header") { SectionHeader("Upcoming", Modifier.semantics { heading() }) }
        items(state.upcomingTrips, key = { "trip-${it.id}" }) { trip ->
            TripCard(trip, state.today, past = false, onClick = { onOpenTrip(trip.id) })
        }
    }
    if (state.pastTrips.isNotEmpty()) {
        item(key = "trips-past-header") { SectionHeader("Past", Modifier.semantics { heading() }) }
        items(state.pastTrips, key = { "trip-${it.id}" }) { trip ->
            TripCard(trip, state.today, past = true, onClick = { onOpenTrip(trip.id) })
        }
    }
}

@Composable
private fun TripCard(trip: Trip, today: LocalDate, past: Boolean, onClick: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val e = trip.entity
    val total = trip.packing.size
    val packed = trip.packedCount
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = if (past) scheme.surfaceContainerLow else scheme.surfaceContainerLowest,
        border = BorderStroke(1.dp, scheme.outlineVariant),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(e.name, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    if (e.destination.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                            Icon(
                                Icons.Outlined.Place,
                                contentDescription = null,
                                tint = scheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                e.destination,
                                style = MaterialTheme.typography.bodyMedium,
                                color = scheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
                Spacer(Modifier.width(8.dp))
                InfoPill(tripTiming(e.startDate, e.endDate, today))
            }
            Text(
                "${dateRangeLabel(e.startDate, e.endDate, today)} · ${plural(trip.days, "day")}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(4.dp))
            if (total > 0) {
                LinearProgressIndicator(
                    progress = { packed.toFloat() / total },
                    modifier = Modifier.fillMaxWidth().clearAndSetSemantics { },
                    color = if (packed == total) HangerTokens.extras.success else scheme.primary,
                    trackColor = scheme.surfaceContainerHighest,
                )
                Text(
                    if (packed == total) "All $total packed" else "$packed of $total packed",
                    style = MaterialTheme.typography.labelMedium,
                    color = scheme.onSurfaceVariant,
                )
            } else {
                Text("No packing list yet", style = MaterialTheme.typography.labelMedium, color = scheme.onSurfaceVariant)
            }
        }
    }
}
