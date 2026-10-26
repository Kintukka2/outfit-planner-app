package com.kintukka.hanger.ui.outfits

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.kintukka.hanger.AppContainer
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.data.OutfitLayout
import com.kintukka.hanger.data.db.OutfitEntity
import com.kintukka.hanger.engine.FeedbackType
import com.kintukka.hanger.engine.Occasion
import com.kintukka.hanger.engine.SuggestedOutfit
import com.kintukka.hanger.ui.components.EmptyState
import com.kintukka.hanger.ui.components.ItemImage
import com.kintukka.hanger.ui.components.weatherIcon
import com.kintukka.hanger.ui.formatTemp
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.longLabel
import com.kintukka.hanger.ui.navigation.BuilderRoute
import com.kintukka.hanger.ui.navigation.toEpochOrNone
import com.kintukka.hanger.weather.DayWeather
import com.kintukka.hanger.data.repo.TempUnit
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class SuggestState(
    val date: LocalDate = LocalDate.now(),
    val occasion: Occasion = Occasion.CASUAL,
    val anchor: Item? = null,
    val weather: DayWeather? = null,
    val unit: TempUnit = TempUnit.CELSIUS,
    val loading: Boolean = true,
    val suggestions: List<SuggestedOutfit> = emptyList(),
    val items: Map<Long, Item> = emptyMap(),
    val reactions: Map<String, Reaction> = emptyMap(),
    val saved: Set<String> = emptySet(),
    val closetSize: Int = 0,
)

/** Shared logic for generated suggestions (used by Today and the Suggest screen). */
class SuggestViewModel(
    private val container: AppContainer,
    date: LocalDate,
    anchorItemId: Long?,
) : ViewModel() {
    private val _state = MutableStateFlow(SuggestState(date = date))
    val state: StateFlow<SuggestState> = _state
    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 4)
    val messages: SharedFlow<String> = _messages
    private var seed = date.toEpochDay()
    private var anchorId = anchorItemId

    init {
        viewModelScope.launch {
            val settings = container.settings.current()
            _state.update { it.copy(occasion = settings.defaultOccasion, unit = settings.tempUnit) }
            refresh()
        }
    }

    fun setOccasion(occasion: Occasion) {
        _state.update { it.copy(occasion = occasion) }
        viewModelScope.launch { refresh() }
    }

    fun clearAnchor() {
        anchorId = null
        viewModelScope.launch { refresh() }
    }

    fun more() {
        seed += 7919
        viewModelScope.launch { refresh() }
    }

    suspend fun refresh() {
        _state.update { it.copy(loading = true) }
        val date = _state.value.date
        val items = container.repo.snapshotItems().associateBy { it.id }
        val weather = runCatching { container.stylist.weatherFor(date) }.getOrNull()
        val suggestions = container.stylist.suggest(
            date = date,
            occasion = _state.value.occasion,
            seed = seed,
            anchors = setOfNotNull(anchorId),
            count = 4,
            weatherContext = weather?.wearing,
        )
        _state.update {
            it.copy(
                loading = false,
                suggestions = suggestions,
                items = items,
                weather = weather,
                anchor = anchorId?.let(items::get),
                closetSize = items.values.count { i -> !i.entity.archived && !i.entity.wishlist },
            )
        }
    }

    fun react(s: SuggestedOutfit, reaction: Reaction) {
        _state.update { it.copy(reactions = it.reactions + (s.signature to reaction)) }
        val type = when (reaction) {
            Reaction.LIKED -> FeedbackType.LIKED
            Reaction.DISLIKED -> FeedbackType.REJECTED
            Reaction.NONE -> return
        }
        viewModelScope.launch { container.repo.recordFeedback(type, s.itemIds) }
    }

    fun save(s: SuggestedOutfit) = viewModelScope.launch {
        val items = s.itemIds.mapNotNull { _state.value.items[it] }
        container.repo.saveOutfit(
            id = null,
            name = autoOutfitName(items),
            occasion = _state.value.occasion,
            placements = OutfitLayout.arrange(items.map { it.id to it.category }),
            source = OutfitEntity.SOURCE_SUGGESTED,
        )
        container.repo.recordFeedback(FeedbackType.SAVED, s.itemIds)
        _state.update { it.copy(saved = it.saved + s.signature) }
        _messages.tryEmit("Saved to your outfits")
    }

    /** Wear on today/past dates, plan on future ones. */
    fun commit(s: SuggestedOutfit, onDone: () -> Unit = {}) = viewModelScope.launch {
        val date = _state.value.date
        if (date.isAfter(LocalDate.now())) {
            container.repo.plan(date, s.itemIds, occasion = _state.value.occasion)
            _messages.tryEmit("Planned for ${date.longLabel()}")
        } else {
            container.repo.logWorn(date, s.itemIds, occasion = _state.value.occasion)
            _messages.tryEmit(if (date == LocalDate.now()) "Logged as today's outfit" else "Logged for ${date.longLabel()}")
        }
        onDone()
    }

    fun primaryLabel(): String = if (_state.value.date.isAfter(LocalDate.now())) "Plan it" else "Wear it"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestScreen(navController: NavHostController, date: LocalDate?, anchorItemId: Long?) {
    val day = date ?: LocalDate.now()
    val vm = hangerViewModel(key = "suggest-${day}-$anchorItemId") { SuggestViewModel(it, day, anchorItemId) }
    val state by vm.state.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(Unit) { vm.messages.collect { snackbar.showSnackbar(it) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.anchor != null) "Style it" else "Ideas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back") }
                },
                actions = {
                    IconButton(onClick = vm::more) { Icon(Icons.Outlined.Refresh, contentDescription = "More ideas") }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            item(key = "header") {
                SuggestHeader(state, onOccasion = vm::setOccasion, onClearAnchor = vm::clearAnchor)
            }
            when {
                state.loading -> item(key = "loading") {
                    Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                }
                state.suggestions.isEmpty() -> item(key = "empty") {
                    EmptyState(
                        icon = Icons.Outlined.AutoAwesome,
                        title = "Not enough to work with",
                        body = if (state.closetSize < 3) "Add at least a top, a bottom and some shoes and ideas will appear here."
                        else "Everything suitable is in the laundry or out of season. Try another occasion or mark a few things washed.",
                    )
                }
                else -> {
                    items(state.suggestions, key = { it.signature }) { s ->
                        SuggestionCard(
                            suggestion = s,
                            items = state.items,
                            primaryLabel = vm.primaryLabel(),
                            onPrimary = { vm.commit(s) { navController.popBackStack() } },
                            onSave = { vm.save(s) },
                            onEdit = { navController.navigate(BuilderRoute(dateEpoch = date.toEpochOrNone(), itemIds = s.itemIds.joinToString(","))) },
                            reaction = state.reactions[s.signature] ?: Reaction.NONE,
                            onReact = { vm.react(s, it) },
                            saved = s.signature in state.saved,
                        )
                    }
                    item(key = "more") {
                        OutlinedButton(onClick = vm::more, modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Outlined.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("Show me different ideas", modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestHeader(state: SuggestState, onOccasion: (Occasion) -> Unit, onClearAnchor: (() -> Unit)?) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(state.date.longLabel(), style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            state.weather?.let { w ->
                Icon(weatherIcon(w.condition), contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                Text(
                    " ${formatTemp(w.tempMinC, state.unit)}–${formatTemp(w.tempMaxC, state.unit)}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        state.anchor?.let { anchor ->
            AssistChip(
                onClick = { onClearAnchor?.invoke() },
                label = { Text("Built around ${anchor.name}") },
                leadingIcon = { ItemImage(anchor, Modifier.size(24.dp)) },
                trailingIcon = if (onClearAnchor != null) ({ Icon(Icons.Outlined.Close, contentDescription = "Stop building around this item", modifier = Modifier.size(16.dp)) }) else null,
            )
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Occasion.entries, key = { it.name }) { o ->
                FilterChip(selected = state.occasion == o, onClick = { onOccasion(o) }, label = { Text(o.label) })
            }
        }
    }
}
