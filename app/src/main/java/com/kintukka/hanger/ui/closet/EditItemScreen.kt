package com.kintukka.hanger.ui.closet

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.kintukka.hanger.AppContainer
import com.kintukka.hanger.data.db.ItemEntity
import com.kintukka.hanger.demo.DemoArt
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.navigation.ItemRoute
import com.kintukka.hanger.ui.shortLabel
import com.kintukka.hanger.ui.theme.HangerTokens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.time.Instant
import java.time.ZoneOffset

data class EditItemState(
    val loaded: Boolean = false,
    val entity: ItemEntity? = null,
    val form: ItemForm = ItemForm(),
    val busy: Boolean = false,
    val message: String? = null,
    val savedId: Long? = null,
) {
    /** True for items whose picture is a generated illustration (no real photo). */
    val illustrated: Boolean get() = entity?.originalImageFile == null
}

class EditItemViewModel(private val container: AppContainer, private val id: Long) : ViewModel() {
    private val _state = MutableStateFlow(EditItemState())
    val state: StateFlow<EditItemState> = _state
    /** Files created during this edit that should be removed if the user backs out. */
    private val scratch = mutableListOf<String>()
    private val replaced = mutableListOf<String>()

    init {
        viewModelScope.launch {
            val entity = if (id > 0) container.repo.item(id) else null
            _state.value = EditItemState(loaded = true, entity = entity, form = entity?.let(ItemForm::from) ?: ItemForm())
        }
    }

    fun edit(transform: (ItemForm) -> ItemForm) = _state.update { it.copy(form = transform(it.form)) }

    fun useOriginal(original: Boolean) = _state.update { s -> s.copy(entity = s.entity?.copy(useOriginalImage = original)) }

    fun imageFile(s: EditItemState): File? {
        val e = s.entity ?: return null
        return container.images.file(if (e.useOriginalImage) e.originalImageFile else e.imageFile ?: e.originalImageFile)
    }

    fun redoCutout() {
        val original = _state.value.entity?.originalImageFile ?: return
        _state.update { it.copy(busy = true, message = null) }
        viewModelScope.launch {
            val result = runCatching { container.processor.reprocess(original) }.getOrNull()
            _state.update { s ->
                val cut = result?.cutoutFile
                if (cut == null) s.copy(busy = false, message = "Couldn't find the garment in this photo. Try a plainer background.")
                else {
                    scratch += cut
                    s.entity?.imageFile?.let { replaced += it }
                    s.copy(busy = false, entity = s.entity?.copy(imageFile = cut, useOriginalImage = false), message = "Cut-out updated")
                }
            }
        }
    }

    fun replacePhoto(uri: Uri) {
        _state.update { it.copy(busy = true, message = null) }
        viewModelScope.launch {
            val result = runCatching { container.processor.process(uri) }.getOrNull()
            _state.update { s ->
                if (result == null) return@update s.copy(busy = false, message = "Couldn't read that photo")
                scratch += listOfNotNull(result.originalFile, result.cutoutFile)
                replaced += listOfNotNull(s.entity?.imageFile, s.entity?.originalImageFile)
                val base = s.entity ?: ItemEntity(name = "", category = s.form.category)
                s.copy(
                    busy = false,
                    entity = base.copy(
                        imageFile = result.cutoutFile,
                        originalImageFile = result.originalFile,
                        useOriginalImage = result.cutoutFile == null,
                    ),
                    form = if (s.form.colors.isEmpty()) s.form.copy(colors = result.colors) else s.form,
                )
            }
        }
    }

    fun save() {
        val s = _state.value
        _state.update { it.copy(busy = true) }
        viewModelScope.launch {
            var entity = s.form.applyTo(s.entity ?: ItemEntity(name = "", category = s.form.category))
            if (s.illustrated) {
                // Keep illustrations in sync with the description.
                val art = DemoArt.render(entity.category, entity.subcategory, entity.colors, entity.pattern)
                val file = container.images.saveCutout(art)
                entity.imageFile?.let { if (it !in scratch) replaced += it }
                entity = entity.copy(imageFile = file, useOriginalImage = false)
            }
            val savedId = if (entity.id > 0) {
                container.repo.updateItem(entity); entity.id
            } else {
                container.repo.addItem(entity.copy(createdAt = System.currentTimeMillis()))
            }
            replaced.distinct().forEach(container.images::delete)
            scratch.clear()
            replaced.clear()
            _state.update { it.copy(busy = false, savedId = savedId) }
        }
    }

    override fun onCleared() {
        scratch.forEach(container.images::delete)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(navController: NavHostController, id: Long) {
    val vm = hangerViewModel(key = "edit-$id") { EditItemViewModel(it, id) }
    val state by vm.state.collectAsStateWithLifecycle()
    var pickDate by remember { mutableStateOf(false) }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri -> uri?.let(vm::replacePhoto) }

    LaunchedEffect(state.savedId) {
        val saved = state.savedId ?: return@LaunchedEffect
        if (id > 0) navController.popBackStack()
        else navController.navigate(ItemRoute(saved)) { popUpTo<com.kintukka.hanger.ui.navigation.EditItemRoute> { inclusive = true } }
    }

    val form = state.form
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (id > 0) "Edit item" else "New item") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back") }
                },
                actions = {
                    TextButton(onClick = vm::save, enabled = state.loaded && !state.busy) { Text("Save") }
                },
            )
        },
    ) { padding ->
        if (!state.loaded) return@Scaffold
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 40.dp),
        ) {
            Box(
                Modifier.fillMaxWidth().height(240.dp).clip(MaterialTheme.shapes.extraLarge).background(HangerTokens.extras.tile),
                contentAlignment = Alignment.Center,
            ) {
                val file = vm.imageFile(state)
                if (state.illustrated) {
                    val art = remember(form.category, form.subcategory, form.colors, form.pattern) {
                        DemoArt.render(form.category, form.subcategory, form.colors, form.pattern, size = 360).asImageBitmap()
                    }
                    Image(art, contentDescription = "Illustration preview", modifier = Modifier.fillMaxSize().padding(24.dp))
                } else if (file != null) {
                    AsyncImage(model = file, contentDescription = form.displayName(), modifier = Modifier.fillMaxSize().padding(20.dp))
                }
                if (state.busy) CircularProgressIndicator()
            }
            Row(Modifier.padding(top = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                val e = state.entity
                if (e?.originalImageFile != null && e.imageFile != null) {
                    FilterChip(selected = !e.useOriginalImage, onClick = { vm.useOriginal(false) }, label = { Text("Cut-out") })
                    FilterChip(selected = e.useOriginalImage, onClick = { vm.useOriginal(true) }, label = { Text("Original") })
                }
                Spacer(Modifier.weight(1f))
                if (e?.originalImageFile != null) {
                    IconButton(onClick = vm::redoCutout, enabled = !state.busy) { Icon(Icons.Outlined.ContentCut, contentDescription = "Redo cut-out") }
                }
                IconButton(onClick = { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, enabled = !state.busy) {
                    Icon(Icons.Outlined.PhotoLibrary, contentDescription = if (state.illustrated) "Add a photo" else "Replace photo")
                }
            }
            state.message?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            OutlinedTextField(
                value = form.displayName(),
                onValueChange = { v -> vm.edit { it.copy(name = v, nameEdited = true) } },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            )
            FieldLabel("Category")
            CategoryPicker(form.category, onSelect = { c -> vm.edit { it.withCategory(c) } })
            FieldLabel("Type")
            SubcategoryPicker(form.category, form.subcategory, onSelect = { p -> vm.edit { it.withPreset(p) } })
            OutlinedTextField(
                value = form.subcategory,
                onValueChange = { v -> vm.edit { it.copy(subcategory = v) } },
                label = { Text("Or type your own") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            )
            FieldLabel(if (form.colors.isEmpty()) "Colours (tap up to 3, main first)" else "Colours · ${form.colors.joinToString { it.label }}")
            ColorPicker(form.colors, onChange = { c -> vm.edit { it.copy(colors = c) } })
            FieldLabel("Pattern")
            PatternPicker(form.pattern, onSelect = { p -> vm.edit { it.copy(pattern = p) } })
            Spacer(Modifier.height(12.dp))
            FormalitySlider(form.formality) { v -> vm.edit { it.copy(formality = v) } }
            WarmthSlider(form.warmth) { v -> vm.edit { it.copy(warmth = v) } }
            FieldLabel("Seasons")
            SeasonPicker(form.seasons, onChange = { v -> vm.edit { it.copy(seasons = v) } })

            SwitchRow("Sporty / activewear", form.sporty) { v -> vm.edit { it.copy(sporty = v) } }
            SwitchRow("Waterproof", form.waterproof) { v -> vm.edit { it.copy(waterproof = v) } }
            SwitchRow("On my wishlist (don't own it yet)", form.wishlist) { v -> vm.edit { it.copy(wishlist = v) } }

            FieldLabel("Wears before washing")
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { vm.edit { it.copy(wearsBeforeWash = (it.wearsBeforeWash - 1).coerceAtLeast(0)) } }) {
                    Icon(Icons.Outlined.Remove, contentDescription = "Fewer wears")
                }
                Text(
                    if (form.wearsBeforeWash == 0) "Never needs a wash" else "${form.wearsBeforeWash}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
                IconButton(onClick = { vm.edit { it.copy(wearsBeforeWash = (it.wearsBeforeWash + 1).coerceAtMost(30)) } }) {
                    Icon(Icons.Outlined.Add, contentDescription = "More wears")
                }
            }

            FieldLabel("Details")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(form.brand, { v -> vm.edit { it.copy(brand = v) } }, label = { Text("Brand") }, singleLine = true, modifier = Modifier.weight(1f))
                OutlinedTextField(form.size, { v -> vm.edit { it.copy(size = v) } }, label = { Text("Size") }, singleLine = true, modifier = Modifier.weight(0.6f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 8.dp)) {
                OutlinedTextField(form.material, { v -> vm.edit { it.copy(material = v) } }, label = { Text("Material") }, singleLine = true, modifier = Modifier.weight(1f))
                OutlinedTextField(
                    form.price, { v -> vm.edit { it.copy(price = v.filter { ch -> ch.isDigit() || ch == '.' || ch == ',' }) } },
                    label = { Text("Price") }, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(0.6f),
                )
            }
            OutlinedButton(onClick = { pickDate = true }, modifier = Modifier.padding(top = 10.dp)) {
                Text(form.purchaseDate?.let { "Bought ${it.shortLabel()}" } ?: "Add purchase date")
            }
            OutlinedTextField(form.tags, { v -> vm.edit { it.copy(tags = v) } }, label = { Text("Tags (comma separated)") }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(top = 10.dp))
            OutlinedTextField(form.notes, { v -> vm.edit { it.copy(notes = v) } }, label = { Text("Notes") }, minLines = 2, modifier = Modifier.fillMaxWidth().padding(top = 10.dp))

            Button(onClick = vm::save, enabled = !state.busy, modifier = Modifier.fillMaxWidth().padding(top = 20.dp).height(52.dp)) {
                Text(if (id > 0) "Save changes" else "Add to closet")
            }
        }
    }

    if (pickDate) {
        val initial = form.purchaseDate?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
        val dateState = rememberDatePickerState(initialSelectedDateMillis = initial ?: System.currentTimeMillis())
        DatePickerDialog(
            onDismissRequest = { pickDate = false },
            confirmButton = {
                TextButton(onClick = {
                    dateState.selectedDateMillis?.let { ms ->
                        val date = Instant.ofEpochMilli(ms).atZone(ZoneOffset.UTC).toLocalDate()
                        vm.edit { it.copy(purchaseDate = date) }
                    }
                    pickDate = false
                }) { Text("Set") }
            },
            dismissButton = {
                TextButton(onClick = { vm.edit { it.copy(purchaseDate = null) }; pickDate = false }) { Text("Clear") }
            },
        ) { DatePicker(dateState) }
    }
}

@Composable
private fun SwitchRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
