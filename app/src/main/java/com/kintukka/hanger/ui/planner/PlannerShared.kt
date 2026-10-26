package com.kintukka.hanger.ui.planner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.data.db.TripEntity
import com.kintukka.hanger.data.repo.SettingsRepository
import com.kintukka.hanger.data.repo.Stylist
import com.kintukka.hanger.data.repo.WardrobeRepository
import com.kintukka.hanger.engine.Hemisphere
import com.kintukka.hanger.engine.Occasion
import com.kintukka.hanger.engine.TripSpec
import com.kintukka.hanger.ui.components.ItemImage
import com.kintukka.hanger.ui.theme.HangerTokens
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

/** Key used to hand a one-off message (e.g. "packing list failed") to the next planner destination. */
internal const val PLANNER_MESSAGE_KEY = "planner.message"

/** Like [runCatching], but never swallows coroutine cancellation. */
internal inline fun <T> catching(block: () -> T): Result<T> = try {
    Result.success(block())
} catch (e: CancellationException) {
    throw e
} catch (t: Throwable) {
    Result.failure(t)
}

/** Base for planner view models: one-off snackbar messages plus crash-safe write launching. */
internal abstract class MessagingViewModel : ViewModel() {
    private val _messages = Channel<String>(Channel.BUFFERED)
    val messages: Flow<String> = _messages.receiveAsFlow()

    protected fun say(message: String) {
        _messages.trySend(message)
    }

    /** Runs a repository write; a failure becomes a snackbar instead of a crash. */
    protected fun write(errorMessage: String, block: suspend () -> Unit): Job = viewModelScope.launch {
        catching { block() }.onFailure { say(errorMessage) }
    }
}

/** Shows every message from [messages] as a snackbar while the screen is on screen. */
@Composable
internal fun CollectMessages(messages: Flow<String>, host: SnackbarHostState) {
    LaunchedEffect(messages, host) {
        messages.collect { host.showSnackbar(it) }
    }
}

/** Shows a message handed over through this destination's saved state (see [PLANNER_MESSAGE_KEY]). */
@Composable
internal fun CollectHandedMessages(host: SnackbarHostState) {
    val entry = LocalViewModelStoreOwner.current as? NavBackStackEntry ?: return
    LaunchedEffect(entry, host) {
        val handle = entry.savedStateHandle
        handle.getStateFlow<String?>(PLANNER_MESSAGE_KEY, null).collect { message ->
            if (message != null) {
                handle[PLANNER_MESSAGE_KEY] = null
                host.showSnackbar(message)
            }
        }
    }
}

/**
 * Pops this destination at most once, and only while it is still the top of the back stack,
 * so a deleted/missing record can't pop an unrelated screen.
 */
@Composable
internal fun rememberPopSelf(navController: NavHostController): () -> Unit {
    val entry = LocalViewModelStoreOwner.current as? NavBackStackEntry
    var popped by remember { mutableStateOf(false) }
    val currentEntry by rememberUpdatedState(entry)
    return remember(navController) {
        {
            val self = currentEntry
            if (!popped && (self == null || navController.currentBackStackEntry?.id == self.id)) {
                popped = true
                navController.popBackStack()
            }
        }
    }
}

/**
 * Builds a packing plan for [trip] from the closet and (when the destination has coordinates) the forecast,
 * and stores it. Failure is returned, never thrown, so the trip itself is always kept.
 */
internal suspend fun generatePackingPlan(
    trip: TripEntity,
    repo: WardrobeRepository,
    stylist: Stylist,
    settings: SettingsRepository,
): Result<Unit> {
    val weatherByDay = if (overlapsForecast(trip.startDate, trip.endDate)) {
        catching { stylist.weatherRange(trip.latitude, trip.longitude, trip.startDate, trip.endDate) }.getOrDefault(emptyList())
    } else {
        emptyList()
    }
    val hemisphere = trip.latitude?.let { if (it < 0) Hemisphere.SOUTH else Hemisphere.NORTH }
        ?: catching { settings.current().hemisphere }.getOrDefault(Hemisphere.NORTH)
    val spec = TripSpec(
        startDate = trip.startDate,
        endDate = trip.endDate,
        weatherByDay = weatherByDay,
        occasions = trip.occasions.ifEmpty { setOf(Occasion.CASUAL) },
        laundryAvailable = trip.laundry,
        hemisphere = hemisphere,
    )
    return catching {
        val plan = stylist.packingPlan(spec)
        repo.applyPackingPlan(trip.id, plan)
    }
}

/** Open-Meteo forecasts cover today plus ~15 days. */
internal const val FORECAST_DAYS = 15L

/**
 * Whether any day of [start]..[end] can have a forecast. Checked before fetching because the weather cache
 * holds a single location, so a pointless trip fetch would evict the home forecast.
 */
internal fun overlapsForecast(start: LocalDate, end: LocalDate, today: LocalDate = LocalDate.now()): Boolean =
    !end.isBefore(today) && !start.isAfter(today.plusDays(FORECAST_DAYS))

private val dayMonth = DateTimeFormatter.ofPattern("d MMM", Locale.UK)
private val dayMonthYear = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.UK)
private val monthOnly = DateTimeFormatter.ofPattern("MMM", Locale.UK)
private val monthYear = DateTimeFormatter.ofPattern("MMM yyyy", Locale.UK)

/** "12–15 Nov", "28 Oct – 2 Nov", "28 Dec 2026 – 2 Jan 2027" (year only when it isn't this year). */
internal fun dateRangeLabel(start: LocalDate, end: LocalDate, today: LocalDate = LocalDate.now()): String {
    val thisYear = start.year == today.year && end.year == today.year
    return when {
        start == end -> start.format(if (thisYear) dayMonth else dayMonthYear)
        start.year != end.year -> "${start.format(dayMonthYear)} – ${end.format(dayMonthYear)}"
        start.month == end.month -> "${start.dayOfMonth}–${end.dayOfMonth} ${end.format(if (thisYear) monthOnly else monthYear)}"
        else -> "${start.format(dayMonth)} – ${end.format(if (thisYear) dayMonth else dayMonthYear)}"
    }
}

/** Where today sits relative to a trip: "Starts in 5 days", "Day 2 of 4", "Finished". */
internal fun tripTiming(start: LocalDate, end: LocalDate, today: LocalDate = LocalDate.now()): String = when {
    today.isBefore(start) -> when (val days = ChronoUnit.DAYS.between(today, start)) {
        1L -> "Starts tomorrow"
        else -> "Starts in $days days"
    }
    today.isAfter(end) -> "Finished"
    else -> "Day ${ChronoUnit.DAYS.between(start, today) + 1} of ${ChronoUnit.DAYS.between(start, end) + 1}"
}

/** Small "Worn" / "Planned" status pill. */
@Composable
internal fun WornBadge(worn: Boolean, modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = if (worn) scheme.primaryContainer else Color.Transparent,
        contentColor = if (worn) scheme.onPrimaryContainer else scheme.onSurfaceVariant,
        border = if (worn) null else BorderStroke(1.dp, scheme.outline),
    ) {
        Row(
            Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                if (worn) Icons.Filled.Check else Icons.Outlined.Schedule,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
            )
            Text(if (worn) "Worn" else "Planned", style = MaterialTheme.typography.labelMedium)
        }
    }
}

/** Neutral pill used for occasion / timing labels. */
@Composable
internal fun InfoPill(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        Text(text, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
    }
}

/** A garment on the warm tile backdrop, used for tiny decorative thumbnails next to text. */
@Composable
internal fun ItemThumb(item: Item?, size: Dp, modifier: Modifier = Modifier, padding: Dp = 3.dp) {
    Box(
        modifier
            .size(size)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(HangerTokens.extras.tile)
            .clearAndSetSemantics { },
    ) {
        ItemImage(item, Modifier.fillMaxSize().padding(padding))
    }
}
