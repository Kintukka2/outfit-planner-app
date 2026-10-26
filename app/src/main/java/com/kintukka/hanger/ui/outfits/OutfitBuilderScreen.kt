package com.kintukka.hanger.ui.outfits

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AutoFixHigh
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FlipToBack
import androidx.compose.material.icons.outlined.FlipToFront
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material.icons.outlined.ZoomIn
import androidx.compose.material.icons.outlined.ZoomOut
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.kintukka.hanger.AppContainer
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.data.OutfitLayout
import com.kintukka.hanger.data.Placement
import com.kintukka.hanger.engine.Category
import com.kintukka.hanger.engine.GarmentStatus
import com.kintukka.hanger.engine.Occasion
import com.kintukka.hanger.engine.OutfitEvaluation
import com.kintukka.hanger.ui.closet.pickerCategories
import com.kintukka.hanger.ui.components.ItemImage
import com.kintukka.hanger.ui.components.ItemTile
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.shortLabel
import com.kintukka.hanger.ui.theme.HangerTokens
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.time.LocalDate

data class BuilderState(
    val placements: List<Placement> = emptyList(),
    val selected: Long? = null,
    val name: String = "",
    val occasion: Occasion? = null,
    val evaluation: OutfitEvaluation? = null,
    val done: Boolean = false,
    val loaded: Boolean = false,
)

class BuilderViewModel(
    private val container: AppContainer,
    private val outfitId: Long,
    private val date: LocalDate?,
    initialItemIds: List<Long>,
) : ViewModel() {
    private val _state = MutableStateFlow(BuilderState())
    val state: StateFlow<BuilderState> = _state
    private var evalJob: Job? = null

    val closet: StateFlow<List<Item>> = container.repo.closet.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        viewModelScope.launch {
            val items = container.repo.snapshotItems().associateBy { it.id }
            if (outfitId > 0) {
                val outfit = withTimeoutOrNull(5_000) {
                    container.repo.outfits.first { list -> list.any { it.id == outfitId } }.first { it.id == outfitId }
                }
                _state.value = if (outfit != null) {
                    BuilderState(placements = outfit.placements, name = outfit.name, occasion = outfit.entity.occasion, loaded = true)
                } else {
                    BuilderState(loaded = true)
                }
            } else {
                val initial = initialItemIds.mapNotNull { id -> items[id]?.let { id to it.category } }
                _state.value = BuilderState(placements = OutfitLayout.arrange(initial), loaded = true)
            }
            evaluate()
        }
    }

    private fun categoryOf(id: Long): Category? = closet.value.firstOrNull { it.id == id }?.category

    fun toggle(item: Item) {
        _state.update { s ->
            if (s.placements.any { it.itemId == item.id }) {
                s.copy(placements = s.placements.filterNot { it.itemId == item.id }, selected = null)
            } else {
                // One per slot, except accessories which stack.
                val keep = if (item.category == Category.ACCESSORY) s.placements
                else s.placements.filterNot { p ->
                    val c = categoryOf(p.itemId)
                    c == item.category ||
                        (item.category == Category.ONE_PIECE && (c == Category.TOP || c == Category.BOTTOM)) ||
                        (c == Category.ONE_PIECE && (item.category == Category.TOP || item.category == Category.BOTTOM))
                }
                val slot = OutfitLayout.arrange((keep.mapNotNull { p -> categoryOf(p.itemId)?.let { p.itemId to it } }) + (item.id to item.category))
                    .first { it.itemId == item.id }
                s.copy(placements = keep + slot.copy(z = (keep.maxOfOrNull { it.z } ?: 0) + 1), selected = item.id)
            }
        }
        evaluate()
    }

    fun select(id: Long?) = _state.update { it.copy(selected = id) }

    fun transform(id: Long, dx: Float, dy: Float, zoom: Float, rotation: Float) = _state.update { s ->
        s.copy(placements = s.placements.map { p ->
            if (p.itemId != id) p
            else p.copy(
                x = (p.x + dx).coerceIn(0f, 1f),
                y = (p.y + dy).coerceIn(0f, 1f),
                scale = (p.scale * zoom).coerceIn(0.25f, 3f),
                rotation = (p.rotation + rotation) % 360f,
            )
        })
    }

    fun bringForward(id: Long) = _state.update { s ->
        val top = (s.placements.maxOfOrNull { it.z } ?: 0) + 1
        s.copy(placements = s.placements.map { if (it.itemId == id) it.copy(z = top) else it })
    }

    fun sendBack(id: Long) = _state.update { s ->
        val bottom = (s.placements.minOfOrNull { it.z } ?: 0) - 1
        s.copy(placements = s.placements.map { if (it.itemId == id) it.copy(z = bottom) else it })
    }

    fun remove(id: Long) {
        _state.update { s -> s.copy(placements = s.placements.filterNot { it.itemId == id }, selected = null) }
        evaluate()
    }

    fun autoArrange() = _state.update { s ->
        s.copy(placements = OutfitLayout.arrange(s.placements.mapNotNull { p -> categoryOf(p.itemId)?.let { p.itemId to it } }))
    }

    fun setOccasion(o: Occasion?) {
        _state.update { it.copy(occasion = o) }
        evaluate()
    }

    fun setName(name: String) = _state.update { it.copy(name = name) }

    private fun evaluate() {
        evalJob?.cancel()
        evalJob = viewModelScope.launch {
            delay(250)
            val ids = _state.value.placements.map { it.itemId }
            val items = container.repo.snapshotItems().filter { it.id in ids }
            val evaluation = container.stylist.evaluate(items, date ?: LocalDate.now(), _state.value.occasion)
            _state.update { it.copy(evaluation = evaluation) }
        }
    }

    fun defaultName(): String {
        val ids = _state.value.placements.map { it.itemId }
        return autoOutfitName(closet.value.filter { it.id in ids })
    }

    /** Save the outfit; optionally also plan it on [date] or log it as worn. */
    fun save(name: String, plan: Boolean, wear: Boolean) = viewModelScope.launch {
        val s = _state.value
        if (s.placements.isEmpty()) return@launch
        val ordered = s.placements.sortedBy { it.z }
        val id = container.repo.saveOutfit(outfitId.takeIf { it > 0 }, name.ifBlank { defaultName() }, s.occasion, ordered)
        val ids = ordered.map { it.itemId }
        val day = date ?: LocalDate.now()
        when {
            wear -> container.repo.logWorn(day, ids, id, s.occasion)
            plan -> container.repo.plan(day, ids, id, s.occasion)
        }
        _state.update { it.copy(done = true) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutfitBuilderScreen(navController: NavHostController, outfitId: Long, date: LocalDate?, initialItemIds: List<Long>) {
    val vm = hangerViewModel(key = "builder-$outfitId-$date-${initialItemIds.joinToString()}") {
        BuilderViewModel(it, outfitId, date, initialItemIds)
    }
    val state by vm.state.collectAsStateWithLifecycle()
    val closet by vm.closet.collectAsStateWithLifecycle()
    var category by remember { mutableStateOf<Category?>(Category.TOP) }
    var saving by remember { mutableStateOf(false) }

    LaunchedEffect(state.done) { if (state.done) navController.popBackStack() }

    val itemsById = remember(closet) { closet.associateBy { it.id } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (outfitId > 0) "Edit outfit" else "Build an outfit") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back") }
                },
                actions = {
                    IconButton(onClick = vm::autoArrange, enabled = state.placements.isNotEmpty()) {
                        Icon(Icons.Outlined.AutoFixHigh, contentDescription = "Tidy layout")
                    }
                    TextButton(onClick = { saving = true }, enabled = state.placements.isNotEmpty()) { Text("Save") }
                },
            )
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            BoxWithConstraints(Modifier.weight(1f).fillMaxWidth().padding(horizontal = 20.dp), contentAlignment = Alignment.TopCenter) {
                // Largest 4:5 canvas that fits, leaving room for the edit toolbar and stylist feedback below it.
                val reserved = if (state.selected != null) 150.dp else 100.dp
                val canvasWidth = minOf(maxWidth, (maxHeight - reserved).coerceAtLeast(120.dp) * OutfitLayout.ASPECT)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.Center) {
                        OutfitCanvas(
                            placements = state.placements,
                            items = itemsById,
                            selected = state.selected,
                            onSelect = vm::select,
                            onTransform = vm::transform,
                            modifier = Modifier.width(canvasWidth),
                        )
                        if (state.placements.isEmpty()) {
                            Text(
                                "Tap pieces below to drop them on the canvas.\nDrag to move, pinch to resize and rotate.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(24.dp),
                            )
                        }
                    }
                    state.selected?.let { id ->
                        Row(Modifier.padding(top = 4.dp), horizontalArrangement = Arrangement.Center) {
                            IconButton(onClick = { vm.transform(id, 0f, 0f, 0.9f, 0f) }) { Icon(Icons.Outlined.ZoomOut, contentDescription = "Smaller") }
                            IconButton(onClick = { vm.transform(id, 0f, 0f, 1.1f, 0f) }) { Icon(Icons.Outlined.ZoomIn, contentDescription = "Bigger") }
                            IconButton(onClick = { vm.sendBack(id) }) { Icon(Icons.Outlined.FlipToBack, contentDescription = "Send backward") }
                            IconButton(onClick = { vm.bringForward(id) }) { Icon(Icons.Outlined.FlipToFront, contentDescription = "Bring forward") }
                            IconButton(onClick = { vm.remove(id) }) { Icon(Icons.Outlined.Delete, contentDescription = "Remove from outfit") }
                        }
                    }
                    EvaluationStrip(state.evaluation, Modifier.width(canvasWidth).padding(vertical = 8.dp))
                }
            }

            Surface(color = MaterialTheme.colorScheme.surfaceContainerLow, tonalElevation = 1.dp) {
                Column(Modifier.navigationBarsPadding().padding(vertical = 10.dp)) {
                    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(pickerCategories, key = { it.name }) { c ->
                            val count = closet.count { it.category == c }
                            if (count > 0) FilterChip(selected = category == c, onClick = { category = c }, label = { Text(c.label) })
                        }
                    }
                    val tray = closet.filter { it.category == category }
                        .sortedWith(compareBy({ it.status != GarmentStatus.CLEAN }, { -it.entity.createdAt }))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(tray, key = { it.id }) { item ->
                            val inOutfit = state.placements.any { it.itemId == item.id }
                            ItemTile(
                                item,
                                modifier = Modifier.width(84.dp).alpha(if (item.status == GarmentStatus.CLEAN) 1f else 0.55f),
                                selected = inOutfit,
                                onClick = { vm.toggle(item) },
                            )
                        }
                    }
                }
            }
        }
    }

    if (saving) {
        var name by remember { mutableStateOf(state.name.ifBlank { vm.defaultName() }) }
        ModalBottomSheet(onDismissRequest = { saving = false }) {
            Column(Modifier.padding(horizontal = 24.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Save outfit", style = MaterialTheme.typography.headlineSmall)
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Text("Occasion", style = MaterialTheme.typography.labelLarge)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(Occasion.entries, key = { it.name }) { o ->
                        FilterChip(selected = state.occasion == o, onClick = { vm.setOccasion(if (state.occasion == o) null else o) }, label = { Text(o.label) })
                    }
                }
                val day = date ?: LocalDate.now()
                val future = day.isAfter(LocalDate.now())
                Button(onClick = { saving = false; vm.save(name, plan = false, wear = false) }, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                    Text("Save to outfits")
                }
                if (future) {
                    OutlinedButton(onClick = { saving = false; vm.save(name, plan = true, wear = false) }, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                        Text("Save & plan for ${day.shortLabel()}")
                    }
                } else {
                    OutlinedButton(onClick = { saving = false; vm.save(name, plan = false, wear = true) }, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                        Text(if (day == LocalDate.now()) "Save & wear today" else "Save & log for ${day.shortLabel()}")
                    }
                    if (date != null) {
                        OutlinedButton(onClick = { saving = false; vm.save(name, plan = true, wear = false) }, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                            Text("Save & plan for ${day.shortLabel()}")
                        }
                    }
                }
            }
        }
    }
}

/** Editable flat-lay canvas: tap to select, drag/pinch/rotate the selected piece. */
@Composable
fun OutfitCanvas(
    placements: List<Placement>,
    items: Map<Long, Item>,
    selected: Long?,
    onSelect: (Long?) -> Unit,
    onTransform: (id: Long, dx: Float, dy: Float, zoom: Float, rotation: Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    BoxWithConstraints(
        modifier
            .aspectRatio(OutfitLayout.ASPECT)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(HangerTokens.extras.tile)
            .pointerInput(Unit) { detectTapGestures { onSelect(null) } }
            .semantics { contentDescription = "Outfit canvas with ${placements.size} pieces" },
    ) {
        val w = maxWidth
        val h = maxHeight
        val wPx = with(density) { w.toPx() }
        val hPx = with(density) { h.toPx() }
        placements.sortedBy { it.z }.forEach { p ->
            val item = items[p.itemId] ?: return@forEach
            val side = w * OutfitLayout.BASE_SIZE * p.scale
            val isSelected = p.itemId == selected
            Box(
                Modifier
                    .offset(x = w * p.x - side / 2, y = h * p.y - side / 2)
                    .size(side)
                    .graphicsLayer { rotationZ = p.rotation }
                    .then(
                        if (isSelected) Modifier.border(BorderStroke(2.dp, MaterialTheme.colorScheme.primary), RoundedCornerShape(12.dp))
                        else Modifier,
                    )
                    .pointerInput(p.itemId) {
                        detectTapGestures { onSelect(p.itemId) }
                    }
                    .pointerInput(p.itemId, wPx, hPx) {
                        detectTransformGestures { _, pan, zoom, rotation ->
                            onSelect(p.itemId)
                            onTransform(p.itemId, pan.x / wPx, pan.y / hPx, zoom, rotation)
                        }
                    }
                    .semantics { contentDescription = item.name + if (isSelected) ", selected" else "" },
            ) {
                ItemImage(item, Modifier.fillMaxSize().padding(4.dp))
            }
        }
    }
}

@Composable
fun EvaluationStrip(evaluation: OutfitEvaluation?, modifier: Modifier = Modifier) {
    if (evaluation == null) {
        Spacer(modifier.height(4.dp))
        return
    }
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                when {
                    evaluation.score >= 0.8 -> "Looks great"
                    evaluation.score >= 0.6 -> "Works well"
                    evaluation.score >= 0.45 -> "Could work"
                    else -> "A bit of a clash"
                },
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(110.dp),
            )
            LinearProgressIndicator(
                progress = { evaluation.score.toFloat().coerceIn(0f, 1f) },
                modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
            )
        }
        (evaluation.warnings.take(2).map { it to true } + evaluation.notes.take(2).map { it to false }).take(3).forEach { (text, warning) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (warning) Icons.Outlined.WarningAmber else Icons.Outlined.CheckCircle,
                    contentDescription = if (warning) "Warning" else null,
                    tint = if (warning) HangerTokens.extras.warning else HangerTokens.extras.success,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 6.dp),
                )
            }
        }
    }
}
