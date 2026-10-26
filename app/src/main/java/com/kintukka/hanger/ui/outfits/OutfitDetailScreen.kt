package com.kintukka.hanger.ui.outfits

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.kintukka.hanger.AppContainer
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.data.Outfit
import com.kintukka.hanger.engine.OutfitEvaluation
import com.kintukka.hanger.ui.components.ItemTile
import com.kintukka.hanger.ui.components.OutfitCollage
import com.kintukka.hanger.ui.components.SectionHeader
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.navigation.BuilderRoute
import com.kintukka.hanger.ui.navigation.ItemRoute
import com.kintukka.hanger.ui.relativeLabel
import com.kintukka.hanger.ui.shortLabel
import com.kintukka.hanger.ui.theme.HangerTokens
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

data class OutfitDetailUi(
    val outfit: Outfit? = null,
    val items: Map<Long, Item> = emptyMap(),
    val evaluation: OutfitEvaluation? = null,
    val missing: Boolean = false,
)

class OutfitDetailViewModel(private val container: AppContainer, private val id: Long) : ViewModel() {
    private val evaluation = MutableStateFlow<OutfitEvaluation?>(null)
    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 2)
    val messages: SharedFlow<String> = _messages

    val state: StateFlow<OutfitDetailUi> = combine(container.repo.outfits, container.repo.allItems, evaluation) { outfits, items, e ->
        val outfit = outfits.firstOrNull { it.id == id }
        OutfitDetailUi(outfit, items.associateBy { it.id }, e, missing = outfit == null && outfits.isNotEmpty())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OutfitDetailUi())

    init {
        viewModelScope.launch {
            val outfit = container.repo.outfits.value.firstOrNull { it.id == id } ?: return@launch
            evaluation.value = container.stylist.evaluate(outfit.items, LocalDate.now(), outfit.entity.occasion)
        }
    }

    fun toggleFavourite() = viewModelScope.launch { container.repo.toggleOutfitFavorite(id) }
    fun delete(onDone: () -> Unit) = viewModelScope.launch { container.repo.deleteOutfit(id); onDone() }

    fun wearToday() = viewModelScope.launch {
        val o = state.value.outfit ?: return@launch
        container.repo.logWorn(LocalDate.now(), o.itemIds, o.id, o.entity.occasion)
        _messages.tryEmit("Logged as today's outfit")
    }

    fun planOn(date: LocalDate) = viewModelScope.launch {
        val o = state.value.outfit ?: return@launch
        container.repo.plan(date, o.itemIds, o.id, o.entity.occasion)
        _messages.tryEmit("Planned for ${date.shortLabel()}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutfitDetailScreen(navController: NavHostController, id: Long) {
    val vm = hangerViewModel(key = "outfit-$id") { OutfitDetailViewModel(it, id) }
    val state by vm.state.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    var menu by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }
    var pickDate by remember { mutableStateOf(false) }
    var leaving by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { vm.messages.collect { snackbar.showSnackbar(it) } }
    LaunchedEffect(state.missing) { if (state.missing && !leaving) { leaving = true; navController.popBackStack() } }

    val outfit = state.outfit
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back") }
                },
                actions = {
                    if (outfit != null) {
                        IconButton(onClick = vm::toggleFavourite) {
                            Icon(
                                if (outfit.entity.favorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = if (outfit.entity.favorite) "Remove from favourites" else "Add to favourites",
                                tint = if (outfit.entity.favorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        IconButton(onClick = { navController.navigate(BuilderRoute(outfitId = id)) }) { Icon(Icons.Outlined.Edit, contentDescription = "Edit outfit") }
                        Box {
                            IconButton(onClick = { menu = true }) { Icon(Icons.Outlined.MoreVert, contentDescription = "More options") }
                            DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                                DropdownMenuItem(text = { Text("Delete outfit", color = MaterialTheme.colorScheme.error) }, onClick = { menu = false; confirmDelete = true })
                            }
                        }
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        if (outfit == null) return@Scaffold
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            item(key = "collage") { OutfitCollage(outfit.placements, state.items, Modifier.fillMaxWidth()) }
            item(key = "title") {
                Column {
                    Text(outfit.name, style = MaterialTheme.typography.headlineLarge)
                    Text(
                        listOfNotNull(
                            outfit.entity.occasion?.label,
                            if (outfit.timesWorn > 0) "Worn ${outfit.timesWorn}×" else "Not worn yet",
                            outfit.lastWorn?.let { "last ${it.relativeLabel().lowercase()}" },
                        ).joinToString(" · "),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            item(key = "actions") {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = vm::wearToday, modifier = Modifier.weight(1f).height(48.dp)) { Text("Wear today") }
                    OutlinedButton(onClick = { pickDate = true }, modifier = Modifier.weight(1f).height(48.dp)) { Text("Plan a day") }
                }
            }
            state.evaluation?.let { e ->
                item(key = "notes") {
                    Column {
                        SectionHeader("Stylist notes")
                        (e.notes.map { it to false } + e.warnings.map { it to true }).forEach { (text, warning) ->
                            Row(Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.Top) {
                                Icon(
                                    if (warning) Icons.Outlined.WarningAmber else Icons.Outlined.CheckCircle,
                                    contentDescription = if (warning) "Warning" else null,
                                    tint = if (warning) HangerTokens.extras.warning else HangerTokens.extras.success,
                                    modifier = Modifier.size(18.dp),
                                )
                                Text(text, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                }
            }
            item(key = "pieces") {
                Column {
                    SectionHeader("Pieces")
                    outfit.items.chunked(3).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 10.dp)) {
                            row.forEach { item ->
                                ItemTile(item, Modifier.weight(1f), showName = true, onClick = { navController.navigate(ItemRoute(item.id)) })
                            }
                            repeat(3 - row.size) { Box(Modifier.weight(1f)) }
                        }
                    }
                }
            }
        }
    }

    if (pickDate) {
        val dateState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis() + 86_400_000L)
        DatePickerDialog(
            onDismissRequest = { pickDate = false },
            confirmButton = {
                TextButton(onClick = {
                    dateState.selectedDateMillis?.let { vm.planOn(Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()) }
                    pickDate = false
                }) { Text("Plan") }
            },
            dismissButton = { TextButton(onClick = { pickDate = false }) { Text("Cancel") } },
        ) { DatePicker(dateState) }
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete this outfit?") },
            text = { Text("The pieces stay in your closet and past wear logs are kept.") },
            confirmButton = {
                TextButton(onClick = { confirmDelete = false; leaving = true; vm.delete { navController.popBackStack() } }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Cancel") } },
        )
    }
}

