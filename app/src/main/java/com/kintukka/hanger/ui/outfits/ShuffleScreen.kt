package com.kintukka.hanger.ui.outfits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
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
import com.kintukka.hanger.data.OutfitLayout
import com.kintukka.hanger.data.db.OutfitEntity
import com.kintukka.hanger.engine.Category
import com.kintukka.hanger.engine.GarmentStatus
import com.kintukka.hanger.engine.OutfitEvaluation
import com.kintukka.hanger.engine.Seasons
import com.kintukka.hanger.engine.ShuffleRequest
import com.kintukka.hanger.engine.Shuffler
import com.kintukka.hanger.ui.components.ItemImage
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.navigation.BuilderRoute
import com.kintukka.hanger.ui.navigation.toEpochOrNone
import com.kintukka.hanger.ui.shortLabel
import com.kintukka.hanger.ui.theme.HangerTokens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import kotlin.random.Random

data class ShuffleOptions(
    val dressMode: Boolean = false,
    val layer: Boolean = false,
    val outerwear: Boolean = false,
    val bag: Boolean = false,
    val accessory: Boolean = false,
    val smart: Boolean = true,
    val leastWorn: Boolean = false,
    val inSeason: Boolean = true,
    val cleanOnly: Boolean = true,
) {
    val rows: List<Category>
        get() = buildList {
            if (outerwear) add(Category.OUTERWEAR)
            if (layer) add(Category.LAYER)
            if (dressMode) add(Category.ONE_PIECE) else { add(Category.TOP); add(Category.BOTTOM) }
            add(Category.SHOES)
            if (bag) add(Category.BAG)
            if (accessory) add(Category.ACCESSORY)
        }
}

data class ShuffleUi(
    val options: ShuffleOptions = ShuffleOptions(),
    val rows: Map<Category, List<Item>> = emptyMap(),
    /** The id currently showing in each row. */
    val current: Map<Category, Long> = emptyMap(),
    val locked: Set<Category> = emptySet(),
    val evaluation: OutfitEvaluation? = null,
    /** Bumped on every shuffle so pagers animate to the new picks. */
    val shuffleCount: Int = 0,
)

class ShuffleViewModel(private val container: AppContainer, private val date: LocalDate?) : ViewModel() {
    private val options = MutableStateFlow(ShuffleOptions())
    private val current = MutableStateFlow<Map<Category, Long>>(emptyMap())
    private val locked = MutableStateFlow<Set<Category>>(emptySet())
    private val evaluation = MutableStateFlow<OutfitEvaluation?>(null)
    private val shuffles = MutableStateFlow(0)
    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 2)
    val messages: SharedFlow<String> = _messages
    private var evalJob: Job? = null
    private val day get() = date ?: LocalDate.now()

    private val rows = combine(container.repo.closet, options) { closet, o ->
        val season = Seasons.forDate(day, container.settings.current().hemisphere)
        o.rows.associateWith { c ->
            closet.filter { it.category == c }
                .filter { !o.cleanOnly || it.status == GarmentStatus.CLEAN }
                .filter { !o.inSeason || it.entity.seasons.isEmpty() || season in it.entity.seasons }
                .sortedByDescending { it.entity.favorite }
        }
    }

    val state: StateFlow<ShuffleUi> = combine(
        combine(options, rows, current) { o, r, c -> Triple(o, r, c) },
        locked, evaluation, shuffles,
    ) { (o, r, c), l, e, n ->
        // Make sure every row points at something it actually contains.
        val fixed = r.mapValues { (cat, list) -> c[cat]?.takeIf { id -> list.any { it.id == id } } ?: list.firstOrNull()?.id }
            .filterValues { it != null }.mapValues { it.value!! }
        ShuffleUi(o, r, fixed, l, e, n)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ShuffleUi())

    init {
        viewModelScope.launch { delay(300); shuffle() }
    }

    fun setOptions(transform: (ShuffleOptions) -> ShuffleOptions) {
        options.update(transform)
        evaluateSoon()
    }

    fun toggleLock(category: Category) = locked.update { if (category in it) it - category else it + category }

    fun showing(category: Category, id: Long) {
        if (current.value[category] == id) return
        current.update { it + (category to id) }
        evaluateSoon()
    }

    fun shuffle() = viewModelScope.launch {
        val s = state.value
        val closet = container.repo.snapshotCloset().map { it.toGarment() }
        val season = if (s.options.inSeason) Seasons.forDate(day, container.settings.current().hemisphere) else null
        val picks = withContext(Dispatchers.Default) {
            runCatching {
                Shuffler.shuffle(
                    closet,
                    ShuffleRequest(
                        rows = s.options.rows,
                        locked = s.locked.mapNotNull { c -> s.current[c]?.let { c to it } }.toMap(),
                        onlyClean = s.options.cleanOnly,
                        season = season,
                        smart = s.options.smart,
                        favourLeastWorn = s.options.leastWorn,
                        seed = Random.nextLong(),
                    ),
                )
            }.getOrElse {
                // Fallback: plain random picks so the screen never dead-ends.
                s.rows.mapNotNull { (c, list) -> (s.current[c].takeIf { c in s.locked } ?: list.randomOrNull()?.id)?.let { c to it } }.toMap()
            }
        }
        current.value = current.value + picks
        shuffles.update { it + 1 }
        evaluateSoon()
    }

    private fun evaluateSoon() {
        evalJob?.cancel()
        evalJob = viewModelScope.launch {
            delay(300)
            val ids = state.value.current.values.toSet()
            val items = container.repo.snapshotItems().filter { it.id in ids }
            evaluation.value = container.stylist.evaluate(items, day, null)
        }
    }

    private fun currentItems(): List<Item> {
        val s = state.value
        return s.options.rows.mapNotNull { c -> s.current[c]?.let { id -> s.rows[c]?.firstOrNull { it.id == id } } }
    }

    fun save() = viewModelScope.launch {
        val items = currentItems()
        if (items.isEmpty()) return@launch
        container.repo.saveOutfit(null, autoOutfitName(items), null, OutfitLayout.arrange(items.map { it.id to it.category }), OutfitEntity.SOURCE_SHUFFLE)
        _messages.tryEmit("Saved to your outfits")
    }

    fun commit(onDone: () -> Unit) = viewModelScope.launch {
        val ids = currentItems().map { it.id }
        if (ids.isEmpty()) return@launch
        if (day.isAfter(LocalDate.now())) container.repo.plan(day, ids) else container.repo.logWorn(day, ids)
        onDone()
    }

    fun currentIds(): List<Long> = currentItems().map { it.id }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShuffleScreen(navController: NavHostController, date: LocalDate?) {
    val vm = hangerViewModel(key = "shuffle-$date") { ShuffleViewModel(it, date) }
    val state by vm.state.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(Unit) { vm.messages.collect { snackbar.showSnackbar(it) } }
    val day = date ?: LocalDate.now()
    val future = day.isAfter(LocalDate.now())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dress me") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back") }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(BuilderRoute(dateEpoch = date.toEpochOrNone(), itemIds = vm.currentIds().joinToString(",")))
                    }) { Icon(Icons.Outlined.Edit, contentDescription = "Open on canvas") }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surfaceContainerLow) {
                Column(Modifier.navigationBarsPadding().padding(horizontal = 20.dp, vertical = 12.dp)) {
                    EvaluationStrip(state.evaluation, Modifier.padding(bottom = 10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(onClick = vm::save, modifier = Modifier.weight(1f).height(52.dp)) { Text("Save") }
                        OutlinedButton(
                            onClick = { vm.commit { navController.popBackStack() } },
                            modifier = Modifier.weight(1f).height(52.dp),
                        ) { Text(if (future) "Plan ${day.shortLabel()}" else "Wear it") }
                        Button(onClick = { vm.shuffle() }, modifier = Modifier.weight(1.2f).height(52.dp)) {
                            Icon(Icons.Outlined.Shuffle, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Shuffle")
                        }
                    }
                }
            }
        },
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
        ) {
            LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val o = state.options
                item { FilterChip(o.dressMode, { vm.setOptions { it.copy(dressMode = !it.dressMode) } }, { Text("Dress") }) }
                item { FilterChip(o.layer, { vm.setOptions { it.copy(layer = !it.layer) } }, { Text("+ Layer") }) }
                item { FilterChip(o.outerwear, { vm.setOptions { it.copy(outerwear = !it.outerwear) } }, { Text("+ Coat") }) }
                item { FilterChip(o.bag, { vm.setOptions { it.copy(bag = !it.bag) } }, { Text("+ Bag") }) }
                item { FilterChip(o.accessory, { vm.setOptions { it.copy(accessory = !it.accessory) } }, { Text("+ Accessory") }) }
                item { FilterChip(o.smart, { vm.setOptions { it.copy(smart = !it.smart) } }, { Text("Smart match") }) }
                item { FilterChip(o.leastWorn, { vm.setOptions { it.copy(leastWorn = !it.leastWorn) } }, { Text("Least worn") }) }
                item { FilterChip(o.inSeason, { vm.setOptions { it.copy(inSeason = !it.inSeason) } }, { Text("In season") }) }
                item { FilterChip(o.cleanOnly, { vm.setOptions { it.copy(cleanOnly = !it.cleanOnly) } }, { Text("Clean only") }) }
            }
            Spacer(Modifier.height(8.dp))
            state.options.rows.forEach { category ->
                key(category) {
                    ShuffleRow(
                        category = category,
                        items = state.rows[category].orEmpty(),
                        currentId = state.current[category],
                        locked = category in state.locked,
                        shuffleCount = state.shuffleCount,
                        onShowing = { vm.showing(category, it) },
                        onToggleLock = { vm.toggleLock(category) },
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ShuffleRow(
    category: Category,
    items: List<Item>,
    currentId: Long?,
    locked: Boolean,
    shuffleCount: Int,
    onShowing: (Long) -> Unit,
    onToggleLock: () -> Unit,
) {
    val tall = category in setOf(Category.ONE_PIECE, Category.TOP, Category.BOTTOM, Category.OUTERWEAR, Category.LAYER)
    val height = if (category == Category.ONE_PIECE) 260.dp else if (tall) 150.dp else 104.dp
    Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(category.label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
            IconToggleButton(checked = locked, onCheckedChange = { onToggleLock() }) {
                Icon(
                    if (locked) Icons.Filled.Lock else Icons.Outlined.LockOpen,
                    contentDescription = if (locked) "Unlock ${category.label}" else "Keep this ${category.label.lowercase()}",
                    tint = if (locked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (items.isEmpty()) {
            Text(
                "Nothing available here right now.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            )
            return
        }
        val pager = rememberPagerState(initialPage = items.indexOfFirst { it.id == currentId }.coerceAtLeast(0)) { items.size }
        SyncPager(pager, items, currentId, shuffleCount, onShowing)
        HorizontalPager(
            state = pager,
            contentPadding = PaddingValues(horizontal = 96.dp),
            pageSpacing = 12.dp,
            userScrollEnabled = !locked,
            modifier = Modifier.fillMaxWidth().height(height),
        ) { page ->
            val item = items[page]
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.large)
                    .background(HangerTokens.extras.tile),
            ) {
                ItemImage(item, Modifier.fillMaxSize().padding(10.dp))
            }
        }
    }
}

@Composable
private fun SyncPager(pager: PagerState, items: List<Item>, currentId: Long?, shuffleCount: Int, onShowing: (Long) -> Unit) {
    val scope = rememberCoroutineScope()
    // Model -> pager: animate to the shuffled pick.
    LaunchedEffect(shuffleCount, currentId, items.size) {
        val target = items.indexOfFirst { it.id == currentId }
        if (target >= 0 && target != pager.currentPage) scope.launch { pager.animateScrollToPage(target) }
    }
    // Pager -> model: user swiped.
    LaunchedEffect(pager, items) {
        snapshotFlow { pager.settledPage }.collect { page -> items.getOrNull(page)?.let { onShowing(it.id) } }
    }
}
