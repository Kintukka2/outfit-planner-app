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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items as rowItems
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.kintukka.hanger.AppContainer
import com.kintukka.hanger.data.Capsule
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.engine.CapsuleAnalyzer
import com.kintukka.hanger.ui.components.ItemTile
import com.kintukka.hanger.ui.components.SectionHeader
import com.kintukka.hanger.ui.components.StatPill
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.navigation.ItemRoute
import com.kintukka.hanger.ui.plural
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CapsuleUi(
    val capsule: Capsule? = null,
    val members: List<Item> = emptyList(),
    val closet: List<Item> = emptyList(),
    val outfitCount: Int? = null,
    /** Closet pieces not yet in the capsule, ranked by how many outfits they'd add. */
    val boosts: List<Pair<Item, Int>> = emptyList(),
    val editing: Boolean = false,
    val draft: Set<Long> = emptySet(),
    val missing: Boolean = false,
)

class CapsuleViewModel(private val container: AppContainer, private val id: Long) : ViewModel() {
    private val editing = MutableStateFlow(false)
    private val draft = MutableStateFlow<Set<Long>>(emptySet())

    val state: StateFlow<CapsuleUi> = combine(container.repo.capsules, container.repo.closet, editing, draft) { caps, closet, ed, d ->
        val capsule = caps.firstOrNull { it.entity.id == id }
        val members = closet.filter { capsule != null && it.id in capsule.itemIds }
        val garments = members.map { it.toGarment() }
        val count = runCatching { CapsuleAnalyzer.outfitCount(garments) }.getOrNull()
        val boosts = if (count == null) emptyList() else closet.filter { it !in members }
            .mapNotNull { candidate -> runCatching { candidate to CapsuleAnalyzer.wishlistImpact(garments, candidate.toGarment()) }.getOrNull() }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
            .take(8)
        CapsuleUi(capsule, members, closet, count, boosts, ed, d, missing = capsule == null && caps.isNotEmpty())
    }.flowOn(Dispatchers.Default).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CapsuleUi())

    fun startEditing() {
        draft.value = state.value.capsule?.itemIds?.toSet().orEmpty()
        editing.value = true
    }

    fun toggle(itemId: Long) { draft.value = if (itemId in draft.value) draft.value - itemId else draft.value + itemId }

    fun saveEdits() = viewModelScope.launch {
        val current = state.value.capsule?.itemIds?.toSet().orEmpty()
        container.repo.setCapsuleItems(id, draft.value, current)
        editing.value = false
    }

    fun add(itemId: Long) = viewModelScope.launch {
        val current = state.value.capsule?.itemIds?.toSet().orEmpty()
        container.repo.setCapsuleItems(id, current + itemId, current)
    }

    fun rename(name: String) = viewModelScope.launch {
        state.value.capsule?.entity?.let { container.repo.renameCapsule(it.copy(name = name)) }
    }

    fun delete(onDone: () -> Unit) = viewModelScope.launch { container.repo.deleteCapsule(id); onDone() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CapsuleScreen(navController: NavHostController, id: Long) {
    val vm = hangerViewModel(key = "capsule-$id") { CapsuleViewModel(it, id) }
    val state by vm.state.collectAsStateWithLifecycle()
    var renaming by remember { mutableStateOf(false) }
    var leaving by remember { mutableStateOf(false) }
    LaunchedEffect(state.missing) { if (state.missing && !leaving) { leaving = true; navController.popBackStack() } }
    val capsule = state.capsule

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(capsule?.entity?.name ?: "Capsule") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back") }
                },
                actions = {
                    IconButton(onClick = { renaming = true }) { Icon(Icons.Outlined.DriveFileRenameOutline, contentDescription = "Rename capsule") }
                    IconButton(onClick = { leaving = true; vm.delete { navController.popBackStack() } }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Delete capsule")
                    }
                },
            )
        },
        bottomBar = {
            if (state.editing) {
                Button(onClick = vm::saveEdits, modifier = Modifier.fillMaxWidth().padding(16.dp).height(52.dp)) {
                    Text("Done · ${plural(state.draft.size, "piece")}")
                }
            }
        },
    ) { padding ->
        if (capsule == null) return@Scaffold
        LazyVerticalGrid(
            columns = GridCells.Adaptive(100.dp),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            if (state.editing) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text("Tap pieces to add or remove them.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                items(state.closet, key = { it.id }) { item ->
                    ItemTile(item, selected = item.id in state.draft, onClick = { vm.toggle(item.id) })
                }
                return@LazyVerticalGrid
            }
            item(span = { GridItemSpan(maxLineSpan) }, key = "stats") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatPill("Pieces", state.members.size.toString(), Modifier.weight(1f))
                        StatPill("Outfits it makes", state.outfitCount?.toString() ?: "—", Modifier.weight(1f))
                    }
                    Button(onClick = vm::startEditing) { Text(if (state.members.isEmpty()) "Choose pieces" else "Edit pieces") }
                }
            }
            if (state.boosts.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }, key = "boosts") {
                    Column {
                        SectionHeader("Biggest boosts from your closet")
                        Text(
                            "Adding one of these unlocks the most new combinations.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(top = 10.dp)) {
                            rowItems(state.boosts, key = { it.first.id }) { (item, gain) ->
                                ItemTile(item, Modifier.width(96.dp), badge = "+$gain", showName = true, onClick = { vm.add(item.id) })
                            }
                        }
                    }
                }
            }
            if (state.members.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }, key = "members-h") { SectionHeader("In this capsule") }
            }
            items(state.members, key = { it.id }) { item ->
                ItemTile(item, showName = true, onClick = { navController.navigate(ItemRoute(item.id)) })
            }
        }
    }

    if (renaming && capsule != null) {
        var name by remember { mutableStateOf(capsule.entity.name) }
        AlertDialog(
            onDismissRequest = { renaming = false },
            title = { Text("Rename capsule") },
            text = { OutlinedTextField(name, { name = it }, singleLine = true) },
            confirmButton = { TextButton(onClick = { renaming = false; vm.rename(name) }) { Text("Save") } },
            dismissButton = { TextButton(onClick = { renaming = false }) { Text("Cancel") } },
        )
    }
}
