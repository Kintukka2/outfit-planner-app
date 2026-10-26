package com.kintukka.hanger.ui.stylist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.kintukka.hanger.AppContainer
import com.kintukka.hanger.ai.StylistException
import com.kintukka.hanger.ai.StylistOutfit
import com.kintukka.hanger.ai.StylistPrompt
import com.kintukka.hanger.ai.StylistSession
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.data.OutfitLayout
import com.kintukka.hanger.data.db.OutfitEntity
import com.kintukka.hanger.engine.GarmentStatus
import com.kintukka.hanger.ui.components.EmptyState
import com.kintukka.hanger.ui.components.ItemsCollage
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.navigation.SettingsRoute
import com.kintukka.hanger.ui.outfits.autoOutfitName
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class ChatMessage(
    val fromUser: Boolean,
    val text: String,
    val outfits: List<StylistOutfit> = emptyList(),
    val isError: Boolean = false,
)

data class StylistUi(
    val hasKey: Boolean? = null,
    val messages: List<ChatMessage> = emptyList(),
    val sending: Boolean = false,
    val items: Map<Long, Item> = emptyMap(),
)

class StylistViewModel(private val container: AppContainer) : ViewModel() {
    private val _state = MutableStateFlow(StylistUi())
    val state: StateFlow<StylistUi> = _state
    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 2)
    val messages: SharedFlow<String> = _messages
    private var session: StylistSession? = null
    private var sessionKey: String? = null

    init {
        viewModelScope.launch {
            container.settings.settings.collect { s ->
                _state.update { it.copy(hasKey = s.stylistApiKey != null) }
                if (s.stylistApiKey != sessionKey) session = null
            }
        }
    }

    private suspend fun ensureSession(): StylistSession? {
        session?.let { return it }
        val settings = container.settings.current()
        val key = settings.stylistApiKey ?: return null
        val items = container.repo.snapshotCloset()
        _state.update { it.copy(items = container.repo.snapshotItems().associateBy { i -> i.id }) }
        val weather = runCatching { container.stylist.weatherFor(LocalDate.now()) }.getOrNull()
        return StylistSession(key, StylistPrompt.build(items, settings, LocalDate.now(), weather)).also {
            session = it
            sessionKey = key
        }
    }

    fun send(text: String) {
        val question = text.trim()
        if (question.isEmpty() || _state.value.sending) return
        _state.update { it.copy(sending = true, messages = it.messages + ChatMessage(true, question)) }
        viewModelScope.launch {
            val s = ensureSession()
            if (s == null) {
                _state.update { it.copy(sending = false) }
                return@launch
            }
            try {
                val reply = s.send(question)
                val valid = reply.outfits.map { o -> o.copy(itemIds = o.itemIds.filter { it in _state.value.items }) }
                    .filter { it.itemIds.size >= 2 }
                _state.update { it.copy(sending = false, messages = it.messages + ChatMessage(false, reply.reply, valid)) }
            } catch (e: StylistException) {
                _state.update { it.copy(sending = false, messages = it.messages + ChatMessage(false, e.message.orEmpty(), isError = true)) }
            } catch (e: Exception) {
                _state.update { it.copy(sending = false, messages = it.messages + ChatMessage(false, "Something unexpected went wrong.", isError = true)) }
            }
        }
    }

    /** Fresh conversation with an up-to-date wardrobe snapshot. */
    fun reset() {
        session = null
        _state.update { it.copy(messages = emptyList()) }
    }

    fun wear(outfit: StylistOutfit) = viewModelScope.launch {
        container.repo.logWorn(LocalDate.now(), outfit.itemIds)
        _messages.tryEmit("Logged as today's outfit")
    }

    fun save(outfit: StylistOutfit) = viewModelScope.launch {
        val items = outfit.itemIds.mapNotNull { _state.value.items[it] }
        container.repo.saveOutfit(
            null,
            outfit.title.ifBlank { autoOutfitName(items) },
            null,
            OutfitLayout.arrange(items.map { it.id to it.category }),
            OutfitEntity.SOURCE_SUGGESTED,
        )
        _messages.tryEmit("Saved to your outfits")
    }
}

private val starters = listOf(
    "What should I wear today?",
    "Something smart but comfy for dinner tonight",
    "Style my least-worn piece",
    "Pack 4 days in a carry-on",
    "What's missing from my wardrobe?",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StylistScreen(navController: NavHostController) {
    val vm = hangerViewModel { StylistViewModel(it) }
    val state by vm.state.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    var input by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()
    LaunchedEffect(Unit) { vm.messages.collect { snackbar.showSnackbar(it) } }
    LaunchedEffect(state.messages.size, state.sending) {
        val count = listState.layoutInfo.totalItemsCount
        if (count > 0) listState.animateScrollToItem(count - 1)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stylist") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back") }
                },
                actions = {
                    if (state.messages.isNotEmpty()) {
                        IconButton(onClick = vm::reset) { Icon(Icons.Outlined.RestartAlt, contentDescription = "New conversation") }
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            if (state.hasKey == true) {
                Surface(color = MaterialTheme.colorScheme.surfaceContainerLow) {
                    Row(
                        Modifier.fillMaxWidth().navigationBarsPadding().imePadding().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedTextField(
                            value = input,
                            onValueChange = { input = it },
                            placeholder = { Text("Ask about outfits, packing, gaps…") },
                            maxLines = 4,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(onSend = { vm.send(input); input = "" }),
                            modifier = Modifier.weight(1f),
                        )
                        Spacer(Modifier.width(8.dp))
                        FilledIconButton(onClick = { vm.send(input); input = "" }, enabled = input.isNotBlank() && !state.sending) {
                            Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = "Send")
                        }
                    }
                }
            }
        },
    ) { padding ->
        when (state.hasKey) {
            null -> Unit
            false -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                EmptyState(
                    icon = Icons.Outlined.AutoAwesome,
                    title = "A stylist who knows your closet",
                    body = "Chat about what to wear, packing or what's missing, and get looks built from your own pieces. " +
                        "It runs on Claude using your own Anthropic API key — add one in Settings. Your wardrobe list and today's weather are sent with each chat; photos never are.",
                    action = { Button(onClick = { navController.navigate(SettingsRoute) }) { Text("Add API key") } },
                )
            }
            true -> LazyColumn(
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize().padding(padding),
            ) {
                if (state.messages.isEmpty()) {
                    item(key = "intro") {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("What can I help you wear?", style = MaterialTheme.typography.headlineMedium)
                            Text(
                                "I can see every piece in your closet, what's in the laundry and today's forecast.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(starters) { s -> SuggestionChip(onClick = { vm.send(s) }, label = { Text(s) }) }
                            }
                        }
                    }
                }
                itemsIndexed(state.messages) { _, m ->
                    MessageBubble(m, state.items, onWear = vm::wear, onSave = vm::save)
                }
                if (state.sending) {
                    item(key = "typing") {
                        Column(Modifier.fillMaxWidth(0.6f)) {
                            Text("Thinking about your closet…", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            LinearProgressIndicator(Modifier.fillMaxWidth().padding(top = 6.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: ChatMessage,
    items: Map<Long, Item>,
    onWear: (StylistOutfit) -> Unit,
    onSave: (StylistOutfit) -> Unit,
) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = if (message.fromUser) Alignment.End else Alignment.Start) {
        Surface(
            color = when {
                message.fromUser -> MaterialTheme.colorScheme.primary
                message.isError -> MaterialTheme.colorScheme.errorContainer
                else -> MaterialTheme.colorScheme.surfaceContainerHigh
            },
            contentColor = when {
                message.fromUser -> MaterialTheme.colorScheme.onPrimary
                message.isError -> MaterialTheme.colorScheme.onErrorContainer
                else -> MaterialTheme.colorScheme.onSurface
            },
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.widthIn(max = 320.dp),
        ) {
            Text(message.text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp))
        }
        if (message.outfits.isNotEmpty()) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(top = 10.dp)) {
                items(message.outfits) { o ->
                    Card(
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                        modifier = Modifier.width(230.dp),
                    ) {
                        Column(Modifier.padding(10.dp)) {
                            ItemsCollage(o.itemIds, items, Modifier.fillMaxWidth())
                            Text(o.title, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
                            Text(o.why, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            val dirty = o.itemIds.mapNotNull { items[it] }.filter { it.status == GarmentStatus.DIRTY }
                            if (dirty.isNotEmpty()) {
                                Text(
                                    "In the laundry: ${dirty.joinToString { it.name }}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(top = 4.dp),
                                )
                            }
                            Row(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(onClick = { onWear(o) }, modifier = Modifier.weight(1f)) { Text("Wear") }
                                OutlinedButton(onClick = { onSave(o) }, modifier = Modifier.weight(1f)) { Text("Save") }
                            }
                        }
                    }
                }
            }
        }
    }
}

