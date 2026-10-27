package com.kintukka.hanger.ui.closet

import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.kintukka.hanger.AppContainer
import com.kintukka.hanger.data.db.ItemEntity
import com.kintukka.hanger.engine.Category
import com.kintukka.hanger.images.ProcessedGarment
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.plural
import com.kintukka.hanger.ui.theme.HangerTokens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

data class Draft(
    val key: Int,
    val processed: ProcessedGarment? = null,
    val error: String? = null,
    val form: ItemForm = ItemForm(),
    val useCutout: Boolean = true,
) {
    val ready: Boolean get() = processed != null
}

data class AddItemsState(
    val drafts: List<Draft> = emptyList(),
    val total: Int = 0,
    val processedCount: Int = 0,
    val saving: Boolean = false,
    val saved: Boolean = false,
) {
    val processing: Boolean get() = processedCount < total
}

class AddItemsViewModel(private val container: AppContainer) : ViewModel() {
    private val _state = MutableStateFlow(AddItemsState())
    val state: StateFlow<AddItemsState> = _state

    init {
        val uris = container.pendingCaptures.value
        container.pendingCaptures.value = emptyList()
        _state.value = AddItemsState(drafts = uris.indices.map { Draft(it) }, total = uris.size)
        viewModelScope.launch { uris.forEachIndexed { index, uri -> process(index, uri) } }
    }

    private suspend fun process(key: Int, uri: Uri) {
        val result = runCatching { container.processor.process(uri) }
        val stillWanted = !_state.value.saved && _state.value.drafts.any { it.key == key }
        if (!stillWanted) {
            // Removed (or the batch was saved) while this photo was still processing.
            result.getOrNull()?.let(::deleteFiles)
            _state.update { it.copy(processedCount = it.processedCount + 1) }
            return
        }
        _state.update { s ->
            s.copy(
                processedCount = s.processedCount + 1,
                drafts = s.drafts.map { d ->
                    if (d.key != key) d
                    else result.fold(
                        onSuccess = { p ->
                            var form = ItemForm(colors = p.colors)
                            p.preset?.let { form = form.withPreset(it) }
                            d.copy(processed = p, form = form, useCutout = p.cutoutFile != null)
                        },
                        onFailure = { d.copy(error = "Couldn't read this photo") },
                    )
                },
            )
        }
    }

    fun imageFile(draft: Draft): File? {
        val p = draft.processed ?: return null
        return container.images.file(if (draft.useCutout && p.cutoutFile != null) p.cutoutFile else p.originalFile)
    }

    fun edit(key: Int, transform: (Draft) -> Draft) = _state.update { s -> s.copy(drafts = s.drafts.map { if (it.key == key) transform(it) else it }) }

    fun remove(key: Int) {
        val draft = _state.value.drafts.firstOrNull { it.key == key }
        draft?.processed?.let { deleteFiles(it) }
        _state.update { s -> s.copy(drafts = s.drafts.filterNot { it.key == key }) }
    }

    fun save() {
        val ready = _state.value.drafts.filter { it.ready }
        if (ready.isEmpty()) return
        _state.update { it.copy(saving = true) }
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val entities = ready.mapIndexed { i, d ->
                val p = d.processed!!
                d.form.applyTo(
                    ItemEntity(
                        name = "",
                        category = d.form.category,
                        imageFile = p.cutoutFile,
                        originalImageFile = p.originalFile,
                        useOriginalImage = !d.useCutout || p.cutoutFile == null,
                        createdAt = now + i,
                        updatedAt = now + i,
                    ),
                )
            }
            container.repo.addItems(entities)
            savedKeys += ready.map { it.key }
            _state.update { it.copy(saving = false, saved = true) }
        }
    }

    private fun deleteFiles(p: ProcessedGarment) {
        container.images.delete(p.originalFile)
        container.images.delete(p.cutoutFile)
    }

    private val savedKeys = mutableSetOf<Int>()

    override fun onCleared() {
        // Photos processed but never saved shouldn't linger in app storage.
        _state.value.drafts.filter { it.key !in savedKeys }.mapNotNull { it.processed }.forEach(::deleteFiles)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemsScreen(navController: NavHostController) {
    val vm = hangerViewModel { AddItemsViewModel(it) }
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.saved) {
        if (state.saved) navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.processing) "Processing photos…" else "Review ${plural(state.drafts.count { it.ready }, "item")}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back") }
                },
            )
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surfaceContainerLow, tonalElevation = 2.dp) {
                Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(16.dp)) {
                    if (state.processing) {
                        LinearProgressIndicator(
                            progress = { if (state.total == 0) 0f else state.processedCount / state.total.toFloat() },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            "Removing backgrounds and reading colours · ${state.processedCount} of ${state.total}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                    Button(
                        onClick = vm::save,
                        enabled = state.drafts.any { it.ready } && !state.saving,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp).height(52.dp),
                    ) {
                        val n = state.drafts.count { it.ready }
                        Text(if (state.saving) "Saving…" else "Add ${plural(n, "piece")} to closet")
                    }
                }
            }
        },
    ) { padding ->
        if (state.total == 0) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No photos selected", style = MaterialTheme.typography.bodyLarge)
            }
            return@Scaffold
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            items(state.drafts, key = { it.key }) { draft ->
                DraftCard(
                    draft = draft,
                    image = vm.imageFile(draft),
                    onEdit = { transform -> vm.edit(draft.key, transform) },
                    onRemove = { vm.remove(draft.key) },
                )
            }
        }
    }
}

@Composable
private fun DraftCard(draft: Draft, image: File?, onEdit: ((Draft) -> Draft) -> Unit, onRemove: () -> Unit) {
    var expanded by rememberSaveable(draft.key) { mutableStateOf(false) }
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    Modifier.size(112.dp).clip(MaterialTheme.shapes.medium).background(HangerTokens.extras.tile),
                    contentAlignment = Alignment.Center,
                ) {
                    when {
                        draft.error != null -> Text("!", style = MaterialTheme.typography.headlineMedium)
                        image == null -> CircularProgressIndicator(Modifier.size(28.dp), strokeWidth = 3.dp)
                        else -> AsyncImage(model = image, contentDescription = draft.form.displayName(), modifier = Modifier.fillMaxSize().padding(8.dp))
                    }
                }
                Column(Modifier.weight(1f).padding(start = 14.dp)) {
                    if (draft.error != null) {
                        Text(draft.error, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
                    } else if (!draft.ready) {
                        Text("Cutting out…", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        ItemNameField(draft.form, onEdit = { transform -> onEdit { it.copy(form = transform(it.form)) } })
                        if (draft.processed?.cutoutFile != null) {
                            Row(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilterChip(selected = draft.useCutout, onClick = { onEdit { it.copy(useCutout = true) } }, label = { Text("Cut-out") })
                                FilterChip(selected = !draft.useCutout, onClick = { onEdit { it.copy(useCutout = false) } }, label = { Text("Original") })
                            }
                        } else if (draft.processed != null) {
                            Text(
                                "Couldn't separate the background — keeping the photo.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 8.dp),
                            )
                        }
                    }
                }
                IconButton(onClick = onRemove) { Icon(Icons.Outlined.Delete, contentDescription = "Remove this photo") }
            }

            if (draft.ready) {
                CategoryPicker(
                    draft.form.category,
                    onSelect = { c -> onEdit { it.copy(form = it.form.withCategory(c)) } },
                    modifier = Modifier.padding(top = 16.dp),
                )
                SubcategoryPicker(
                    draft.form.category,
                    draft.form.subcategory,
                    onSelect = { p -> onEdit { it.copy(form = it.form.withPreset(p)) } },
                    modifier = Modifier.padding(top = 10.dp),
                )
                ColorPicker(
                    draft.form.colors,
                    onChange = { c -> onEdit { it.copy(form = it.form.copy(colors = c)) } },
                    modifier = Modifier.padding(top = 10.dp),
                )
                TextButton(onClick = { expanded = !expanded }, modifier = Modifier.padding(top = 2.dp)) {
                    Icon(if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, contentDescription = null)
                    Spacer(Modifier.size(6.dp))
                    Text(if (expanded) "Fewer details" else "Pattern, formality, warmth, seasons")
                }
                if (expanded) {
                    FieldLabel("Pattern")
                    PatternPicker(draft.form.pattern, onSelect = { p -> onEdit { it.copy(form = it.form.copy(pattern = p)) } })
                    Spacer(Modifier.height(10.dp))
                    FormalitySlider(draft.form.formality) { v -> onEdit { it.copy(form = it.form.copy(formality = v)) } }
                    WarmthSlider(draft.form.warmth) { v -> onEdit { it.copy(form = it.form.copy(warmth = v)) } }
                    FieldLabel("Seasons")
                    SeasonPicker(draft.form.seasons, onChange = { v -> onEdit { it.copy(form = it.form.copy(seasons = v)) } })
                }
                if (draft.form.category == Category.OTHER) Spacer(Modifier.height(4.dp))
            }
        }
    }
}
