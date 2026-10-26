package com.kintukka.hanger.ui.today

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.LocalLaundryService
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.kintukka.hanger.AppContainer
import com.kintukka.hanger.data.DayEntry
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.data.Outfit
import com.kintukka.hanger.data.repo.Settings
import com.kintukka.hanger.demo.DemoCloset
import com.kintukka.hanger.engine.GarmentStatus
import com.kintukka.hanger.engine.WeatherAdvisor
import com.kintukka.hanger.ui.components.ItemImage
import com.kintukka.hanger.ui.components.ItemsCollage
import com.kintukka.hanger.ui.components.OutfitCollage
import com.kintukka.hanger.ui.components.SectionHeader
import com.kintukka.hanger.ui.components.weatherIcon
import com.kintukka.hanger.ui.formatTemp
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.longLabel
import com.kintukka.hanger.ui.navigation.BuilderRoute
import com.kintukka.hanger.ui.navigation.ClosetRoute
import com.kintukka.hanger.ui.navigation.DayRoute
import com.kintukka.hanger.ui.navigation.PlannerRoute
import com.kintukka.hanger.ui.navigation.SettingsRoute
import com.kintukka.hanger.ui.navigation.ShuffleRoute
import com.kintukka.hanger.ui.navigation.SuggestRoute
import com.kintukka.hanger.ui.outfits.Reaction
import com.kintukka.hanger.ui.outfits.SuggestHeader
import com.kintukka.hanger.ui.outfits.SuggestViewModel
import com.kintukka.hanger.ui.outfits.SuggestionCard
import com.kintukka.hanger.ui.plural
import com.kintukka.hanger.ui.relativeLabel
import com.kintukka.hanger.ui.theme.HangerTokens
import com.kintukka.hanger.weather.DayWeather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

data class TodayUi(
    val settings: Settings = Settings(),
    val weather: DayWeather? = null,
    val weatherLoading: Boolean = false,
    val entries: List<DayEntry> = emptyList(),
    val outfits: Map<Long, Outfit> = emptyMap(),
    val items: Map<Long, Item> = emptyMap(),
    val closetSize: Int = 0,
    val dirty: Int = 0,
    val forgotten: Item? = null,
    val loaded: Boolean = false,
    val loadingDemo: Boolean = false,
)

class TodayViewModel(private val container: AppContainer, private val date: LocalDate) : ViewModel() {
    private val weather = MutableStateFlow<DayWeather?>(null)
    private val weatherLoading = MutableStateFlow(false)
    private val loadingDemo = MutableStateFlow(false)

    val state: StateFlow<TodayUi> = combine(
        combine(container.settings.settings, weather, weatherLoading, loadingDemo) { s, w, l, d -> listOf(s, w, l, d) },
        container.repo.allItems,
        container.repo.dayEntries,
        container.repo.outfits,
    ) { (s, w, l, d), items, days, outfits ->
        val closet = items.filter { !it.entity.archived && !it.entity.wishlist }
        val forgotten = closet
            .filter { it.status == GarmentStatus.CLEAN && it.lastWorn != null && it.lastWorn.isBefore(date.minusDays(45)) }
            .minByOrNull { it.lastWorn!! }
        TodayUi(
            settings = s as Settings,
            weather = w as DayWeather?,
            weatherLoading = l as Boolean,
            entries = days.filter { it.date == date },
            outfits = outfits.associateBy { it.id },
            items = items.associateBy { it.id },
            closetSize = closet.size,
            dirty = closet.count { it.status == GarmentStatus.DIRTY },
            forgotten = forgotten,
            loaded = true,
            loadingDemo = d as Boolean,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodayUi())

    init { refreshWeather(false) }

    fun refreshWeather(force: Boolean) = viewModelScope.launch {
        weatherLoading.value = true
        weather.value = runCatching { container.stylist.weatherFor(date, force) }.getOrNull()
        weatherLoading.value = false
    }

    fun markWorn(entry: DayEntry) = viewModelScope.launch { container.repo.setWorn(entry.id, true) }
    fun washAll() = viewModelScope.launch { container.repo.washAll() }

    fun loadDemo(onDone: () -> Unit) = viewModelScope.launch {
        loadingDemo.value = true
        runCatching { DemoCloset.load(container.db, container.images) }
        loadingDemo.value = false
        onDone()
    }
}

@Composable
fun TodayScreen(navController: NavHostController) {
    val now = LocalTime.now()
    val settingsHour = appSettingsHour()
    val eveningMode = now.hour >= settingsHour
    var showTomorrow by rememberSaveable { mutableStateOf(eveningMode) }
    val date = if (showTomorrow) LocalDate.now().plusDays(1) else LocalDate.now()

    val vm = hangerViewModel(key = "today-$date") { TodayViewModel(it, date) }
    val suggestVm = hangerViewModel(key = "today-suggest-$date") { SuggestViewModel(it, date, null) }
    val state by vm.state.collectAsStateWithLifecycle()
    val ideas by suggestVm.state.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(suggestVm) { suggestVm.messages.collect { snackbar.showSnackbar(it) } }

    // Re-run suggestions when the closet or laundry changes (e.g. after logging an outfit).
    LaunchedEffect(state.closetSize, state.dirty, state.entries.size) { suggestVm.refresh() }

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
        ) {
            item(key = "header") {
                Column(Modifier.padding(horizontal = 20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 12.dp)) {
                        Text(
                            greeting(now).uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f),
                        )
                        IconButton(onClick = { navController.navigate(SettingsRoute) }) {
                            Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                        }
                    }
                    Text(if (showTomorrow) "Tomorrow" else "Today", style = MaterialTheme.typography.displayMedium)
                    Text(
                        date.longLabel(),
                        style = MaterialTheme.typography.headlineSmall.copy(fontStyle = FontStyle.Italic),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (eveningMode || showTomorrow) {
                        SingleChoiceSegmentedButtonRow(Modifier.padding(top = 12.dp)) {
                            SegmentedButton(selected = !showTomorrow, onClick = { showTomorrow = false }, shape = SegmentedButtonDefaults.itemShape(0, 2)) { Text("Today") }
                            SegmentedButton(selected = showTomorrow, onClick = { showTomorrow = true }, shape = SegmentedButtonDefaults.itemShape(1, 2)) { Text("Tomorrow") }
                        }
                    }
                }
            }

            item(key = "weather") {
                WeatherCard(
                    state = state,
                    onRefresh = { vm.refreshWeather(true) },
                    onSetLocation = { navController.navigate(SettingsRoute) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            if (state.loaded && state.closetSize == 0) {
                item(key = "welcome") {
                    WelcomeCard(
                        loading = state.loadingDemo,
                        onAdd = { navController.navigate(ClosetRoute) },
                        onDemo = { vm.loadDemo { suggestVm.more() } },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }

            state.entries.forEach { entry ->
                item(key = "entry-${entry.id}") {
                    PlannedCard(
                        entry = entry,
                        state = state,
                        tomorrow = showTomorrow,
                        onWear = { vm.markWorn(entry) },
                        onOpen = { navController.navigate(DayRoute(date.toEpochDay())) },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }

            if (state.dirty > 0) {
                item(key = "laundry") {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    ) {
                        Row(Modifier.padding(start = 14.dp, end = 4.dp, top = 4.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.LocalLaundryService, contentDescription = null, tint = HangerTokens.extras.laundry, modifier = Modifier.size(20.dp))
                            Text(
                                "${plural(state.dirty, "piece")} in the laundry — they're left out of ideas",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f).padding(start = 10.dp),
                            )
                            TextButton(onClick = vm::washAll) { Text("Washed") }
                        }
                    }
                }
            }

            if (state.closetSize > 0) {
                item(key = "ideas-header") {
                    Column(Modifier.padding(horizontal = 20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                if (state.entries.isEmpty()) "What to wear" else "Or try something else",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.weight(1f),
                            )
                            IconButton(onClick = suggestVm::more) { Icon(Icons.Outlined.Refresh, contentDescription = "New ideas") }
                        }
                        SuggestHeader(ideas.copy(weather = null), onOccasion = suggestVm::setOccasion, onClearAnchor = null)
                    }
                }
                item(key = "ideas") {
                    when {
                        ideas.loading && ideas.suggestions.isEmpty() -> Box(Modifier.fillMaxWidth().height(240.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                        ideas.suggestions.isEmpty() -> Text(
                            "No complete outfits available — add a few more pieces or mark some laundry as washed.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 20.dp),
                        )
                        else -> {
                            val pager = rememberPagerState { ideas.suggestions.size }
                            HorizontalPager(
                                state = pager,
                                contentPadding = PaddingValues(horizontal = 32.dp),
                                pageSpacing = 12.dp,
                                key = { ideas.suggestions[it].signature },
                            ) { page ->
                                val s = ideas.suggestions[page]
                                SuggestionCard(
                                    suggestion = s,
                                    items = ideas.items,
                                    primaryLabel = suggestVm.primaryLabel(),
                                    onPrimary = { suggestVm.commit(s) },
                                    onSave = { suggestVm.save(s) },
                                    onEdit = { navController.navigate(BuilderRoute(dateEpoch = date.toEpochDay(), itemIds = s.itemIds.joinToString(","))) },
                                    reaction = ideas.reactions[s.signature] ?: Reaction.NONE,
                                    onReact = { suggestVm.react(s, it) },
                                    saved = s.signature in ideas.saved,
                                )
                            }
                        }
                    }
                }
                item(key = "quick") {
                    Row(Modifier.padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        QuickTile(Icons.Outlined.Shuffle, "Dress me", Modifier.weight(1f)) { navController.navigate(ShuffleRoute(date.toEpochDay())) }
                        QuickTile(Icons.Outlined.Dashboard, "Build", Modifier.weight(1f)) { navController.navigate(BuilderRoute(dateEpoch = date.toEpochDay())) }
                        QuickTile(Icons.Outlined.CalendarMonth, "Plan week", Modifier.weight(1f)) {
                            navController.navigate(PlannerRoute) { launchSingleTop = true }
                        }
                    }
                }
                state.forgotten?.let { gem ->
                    item(key = "forgotten") {
                        ForgottenCard(gem, modifier = Modifier.padding(horizontal = 20.dp)) {
                            navController.navigate(SuggestRoute(anchorItemId = gem.id))
                        }
                    }
                }
            }
        }
        SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun appSettingsHour(): Int {
    val container = com.kintukka.hanger.ui.appContainer()
    val settings by container.settings.settings.collectAsStateWithLifecycle(initialValue = Settings())
    return settings.tomorrowFromHour
}

private fun greeting(now: LocalTime): String = when (now.hour) {
    in 5..11 -> "Good morning"
    in 12..17 -> "Good afternoon"
    else -> "Good evening"
}

@Composable
private fun WeatherCard(state: TodayUi, onRefresh: () -> Unit, onSetLocation: () -> Unit, modifier: Modifier = Modifier) {
    val s = state.settings
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer),
    ) {
        if (!s.hasLocation) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.LocationOn, contentDescription = null)
                Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                    Text("Dress for the weather", style = MaterialTheme.typography.titleMedium)
                    Text("Set your location and ideas adapt to the forecast.", style = MaterialTheme.typography.bodyMedium)
                }
                TextButton(onClick = onSetLocation) { Text("Set") }
            }
            return@Card
        }
        val w = state.weather
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(weatherIcon(w?.condition), contentDescription = null, modifier = Modifier.size(44.dp))
            Column(Modifier.weight(1f).padding(start = 14.dp)) {
                if (w == null) {
                    Text(if (state.weatherLoading) "Checking the forecast…" else "Forecast unavailable", style = MaterialTheme.typography.titleMedium)
                    Text(s.locationName.orEmpty(), style = MaterialTheme.typography.bodyMedium)
                } else {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("${formatTemp(w.tempMaxC, s.tempUnit)}", style = MaterialTheme.typography.displaySmall)
                        Text(
                            "  / ${formatTemp(w.tempMinC, s.tempUnit)} · ${w.condition.label}",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(bottom = 6.dp),
                        )
                    }
                    val summary = runCatching { WeatherAdvisor.needs(w.wearing).summary }.getOrNull()
                    Text(summary ?: "${w.precipitationProbability}% chance of rain", style = MaterialTheme.typography.bodyMedium)
                    Text(s.locationName.orEmpty(), style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(top = 4.dp))
                }
            }
            IconButton(onClick = onRefresh) { Icon(Icons.Outlined.Refresh, contentDescription = "Refresh forecast") }
        }
    }
}

@Composable
private fun WelcomeCard(loading: Boolean, onAdd: () -> Unit, onDemo: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(Icons.Outlined.Checkroom, contentDescription = null, modifier = Modifier.size(32.dp))
            Text("Your closet, in your pocket", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Photograph your clothes and Hanger cuts them out, tags their colours and starts suggesting outfits for the weather and your plans. Everything stays on this phone.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Button(onClick = onAdd, modifier = Modifier.fillMaxWidth().height(50.dp)) { Text("Add my first pieces") }
            OutlinedButton(onClick = onDemo, enabled = !loading, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                Text(if (loading) "Setting up the demo closet…" else "Explore with a demo closet")
            }
        }
    }
}

@Composable
private fun PlannedCard(entry: DayEntry, state: TodayUi, tomorrow: Boolean, onWear: () -> Unit, onOpen: () -> Unit, modifier: Modifier = Modifier) {
    val outfit = entry.entity.outfitId?.let { state.outfits[it] }
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onOpen,
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.width(110.dp)) {
                if (outfit != null) OutfitCollage(outfit.placements, state.items, Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large)
                else ItemsCollage(entry.itemIds, state.items, Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large)
            }
            Column(Modifier.weight(1f).padding(start = 14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    when {
                        entry.worn -> "Wearing today"
                        tomorrow -> "Planned for tomorrow"
                        else -> "Planned for today"
                    }.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(outfit?.name ?: entry.itemIds.mapNotNull { state.items[it]?.name }.take(3).joinToString(" · "), style = MaterialTheme.typography.titleMedium)
                if (!entry.worn && !tomorrow) {
                    Button(onClick = onWear, modifier = Modifier.padding(top = 6.dp)) {
                        Icon(Icons.Outlined.TaskAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("I'm wearing it")
                    }
                } else if (entry.worn) {
                    Text("Logged · laundry updated", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun QuickTile(icon: ImageVector, label: String, modifier: Modifier, onClick: () -> Unit) {
    Surface(onClick = onClick, color = MaterialTheme.colorScheme.surfaceContainer, shape = MaterialTheme.shapes.large, modifier = modifier) {
        Column(Modifier.padding(vertical = 16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(label, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 6.dp))
        }
    }
}

@Composable
private fun ForgottenCard(item: Item, modifier: Modifier = Modifier, onStyle: () -> Unit) {
    Column(modifier) {
        SectionHeader("Forgotten gem")
        Card(
            onClick = onStyle,
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        ) {
            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(color = HangerTokens.extras.tile, shape = MaterialTheme.shapes.medium, modifier = Modifier.size(72.dp)) {
                    ItemImage(item, Modifier.fillMaxSize().padding(6.dp))
                }
                Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
                    Text(item.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Last worn ${item.lastWorn?.relativeLabel()?.lowercase() ?: "a while ago"}. Give it an outing?",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Icon(Icons.Outlined.AutoAwesome, contentDescription = "Style it", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
