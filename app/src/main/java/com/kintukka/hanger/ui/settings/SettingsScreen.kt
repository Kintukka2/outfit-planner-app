package com.kintukka.hanger.ui.settings

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.format.DateFormat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.LocalLaundryService
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.SaveAlt
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SettingsBackupRestore
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kintukka.hanger.BuildConfig
import com.kintukka.hanger.R
import com.kintukka.hanger.data.repo.TempUnit
import com.kintukka.hanger.engine.Occasion
import com.kintukka.hanger.notify.Reminders
import com.kintukka.hanger.ui.appContainer
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.plural
import com.kintukka.hanger.weather.Place
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val PagePadding = 20.dp

private val Currencies = listOf("£" to "Pound", "$" to "Dollar", "€" to "Euro", "¥" to "Yen", "₹" to "Rupee")

private const val PRIVACY_NOTE =
    "Everything stays on this phone. Cut-outs and tagging run on-device. Weather comes from Open-Meteo using " +
        "only your location's coordinates. Style advice comes from a guide built into the app, so nothing about " +
        "your closet ever leaves the phone. No account, no ads, no tracking."

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    val container = appContainer()
    val vm = hangerViewModel { SettingsViewModel(it) }
    val state by vm.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val is24Hour = remember(context) { DateFormat.is24HourFormat(context) }

    // Re-check notification access whenever the screen resumes (the user may have changed it in system settings).
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()
    val canNotify = remember(lifecycleState) { Reminders.canNotify(context) }

    var query by rememberSaveable { mutableStateOf("") }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    var pendingRestore by rememberSaveable { mutableStateOf<Uri?>(null) }
    var confirmRemoveDemo by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(vm) {
        if (query.isNotEmpty()) vm.onQueryChange(query)
        vm.messages.collect { snackbar.showSnackbar(it, withDismissAction = true) }
    }

    val locationPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) vm.useDeviceLocation()
        else vm.say("Without location access Hanger can't find you, but you can search for your town instead.")
    }
    val notificationPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            vm.setReminderEnabled(true)
        } else {
            scope.launch {
                val result = snackbar.showSnackbar(
                    "The reminder needs permission to send notifications, so it's staying off.",
                    actionLabel = "Settings",
                    duration = SnackbarDuration.Long,
                )
                if (result == SnackbarResult.ActionPerformed) openNotificationSettings(context)
            }
        }
    }
    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/zip")) { uri ->
        if (uri != null) vm.export(uri)
    }
    val restoreLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        pendingRestore = uri
    }

    val actions = SettingsActions(
        onQueryChange = { query = it; vm.onQueryChange(it) },
        onSelectPlace = { place ->
            query = ""
            focusManager.clearFocus()
            vm.selectPlace(place)
        },
        onUseMyLocation = {
            if (container.location.hasPermission()) vm.useDeviceLocation()
            else locationPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        },
        onClearLocation = vm::clearLocation,
        onTempUnit = vm::setTempUnit,
        onReminderToggle = { enabled ->
            when {
                !enabled -> vm.setReminderEnabled(false)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !Reminders.canNotify(context) ->
                    notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                else -> vm.setReminderEnabled(true)
            }
        },
        onPickTime = { showTimePicker = true },
        onOpenNotificationSettings = { openNotificationSettings(context) },
        onAutoLaundry = vm::setAutoLaundry,
        onOccasion = vm::setDefaultOccasion,
        onCurrency = vm::setCurrency,
        onExport = { exportLauncher.launch("hanger-backup-${LocalDate.now()}.zip") },
        onRestore = { restoreLauncher.launch(arrayOf("application/zip", "application/octet-stream")) },
        onLoadDemo = vm::loadDemo,
        onRemoveDemo = { confirmRemoveDemo = true },
    )

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            LargeTopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
            )
        },
    ) { padding ->
        val direction = LocalLayoutDirection.current
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = padding.calculateStartPadding(direction) + PagePadding,
                end = padding.calculateEndPadding(direction) + PagePadding,
                top = padding.calculateTopPadding() + 4.dp,
                bottom = padding.calculateBottomPadding() + 32.dp,
            ),
        ) {
            settingsContent(state, query, is24Hour, canNotify, actions)
        }
    }

    if (showTimePicker) {
        ReminderTimeDialog(
            initialMinutes = state.settings.reminderMinutes,
            is24Hour = is24Hour,
            onDismiss = { showTimePicker = false },
            onConfirm = { minutes ->
                showTimePicker = false
                vm.setReminderTime(minutes)
            },
        )
    }

    pendingRestore?.let { uri ->
        ConfirmDialog(
            title = "Restore this backup?",
            body = "This replaces everything currently on this phone (items, outfits, plans, trips and photos) " +
                "with the contents of the backup. It can't be undone.",
            confirmLabel = "Replace everything",
            onConfirm = {
                pendingRestore = null
                vm.restore(uri)
            },
            onDismiss = { pendingRestore = null },
        )
    }

    if (confirmRemoveDemo) {
        ConfirmDialog(
            title = "Remove the demo closet?",
            body = "This deletes the ${plural(state.demoItemCount, "sample piece")} and the outfits and wear history " +
                "that came with them. Anything you added yourself stays.",
            confirmLabel = "Remove",
            onConfirm = {
                confirmRemoveDemo = false
                vm.removeDemo()
            },
            onDismiss = { confirmRemoveDemo = false },
        )
    }
}

/** Callbacks the list rows need, gathered so the content builder stays readable. */
internal class SettingsActions(
    val onQueryChange: (String) -> Unit,
    val onSelectPlace: (Place) -> Unit,
    val onUseMyLocation: () -> Unit,
    val onClearLocation: () -> Unit,
    val onTempUnit: (TempUnit) -> Unit,
    val onReminderToggle: (Boolean) -> Unit,
    val onPickTime: () -> Unit,
    val onOpenNotificationSettings: () -> Unit,
    val onAutoLaundry: (Boolean) -> Unit,
    val onOccasion: (Occasion) -> Unit,
    val onCurrency: (String) -> Unit,
    val onExport: () -> Unit,
    val onRestore: () -> Unit,
    val onLoadDemo: () -> Unit,
    val onRemoveDemo: () -> Unit,
)

internal fun LazyListScope.settingsContent(
    state: SettingsUiState,
    query: String,
    is24Hour: Boolean,
    canNotify: Boolean,
    actions: SettingsActions,
) {
    val s = state.settings

    section("location", "Weather location") {
        LocationCard(state, query, actions)
    }

    section("units", "Units") {
        SettingsCard {
            ListItem(
                leadingContent = { Icon(Icons.Outlined.Thermostat, contentDescription = null) },
                headlineContent = { Text("Temperature") },
                supportingContent = { Text("Used in forecasts") },
                trailingContent = { TempUnitToggle(s.tempUnit, actions.onTempUnit) },
                colors = rowColors(),
            )
        }
    }

    section("reminder", "Daily outfit reminder") {
        SettingsCard {
            SwitchRow(
                icon = Icons.Outlined.NotificationsActive,
                title = "Remind me each morning",
                supporting = "A nudge with today's planned or suggested outfit",
                checked = s.reminderEnabled,
                onCheckedChange = actions.onReminderToggle,
            )
            RowDivider()
            ListItem(
                leadingContent = { Icon(Icons.Outlined.Schedule, contentDescription = null) },
                headlineContent = { Text("Time") },
                trailingContent = {
                    Text(
                        formatMinutes(s.reminderMinutes, is24Hour),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (s.reminderEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                colors = rowColors(),
                modifier = Modifier.clickable(onClickLabel = "Change reminder time", role = Role.Button, onClick = actions.onPickTime),
            )
            if (s.reminderEnabled && !canNotify) {
                RowDivider()
                ListItem(
                    leadingContent = {
                        Icon(Icons.Outlined.NotificationsOff, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    },
                    headlineContent = { Text("Notifications are off for Hanger") },
                    supportingContent = { Text("The reminder can't appear until you allow them.") },
                    trailingContent = { TextButton(onClick = actions.onOpenNotificationSettings) { Text("Allow") } },
                    colors = rowColors(),
                )
            }
        }
    }

    section("laundry", "Laundry") {
        SettingsCard {
            SwitchRow(
                icon = Icons.Outlined.LocalLaundryService,
                title = "Move clothes to the laundry automatically",
                supporting = "After an item reaches its wears-before-wash count it's marked as in the laundry and skipped in suggestions.",
                checked = s.autoLaundry,
                onCheckedChange = actions.onAutoLaundry,
            )
        }
    }

    section("style", "Style defaults") {
        SettingsCard {
            OccasionRow(s.defaultOccasion, actions.onOccasion)
            RowDivider()
            CurrencyRow(s.currency, actions.onCurrency)
        }
    }

    section("backup", "Backup") {
        val running = state.busy == BusyTask.EXPORT || state.busy == BusyTask.RESTORE
        SettingsCard {
            ActionRow(
                icon = Icons.Outlined.SaveAlt,
                title = "Export backup",
                supporting = "Your closet, outfits, plans and photos in one .zip. Save it to Drive, Downloads or anywhere you like.",
                enabled = state.busy == null,
                onClick = actions.onExport,
            )
            RowDivider()
            ActionRow(
                icon = Icons.Outlined.SettingsBackupRestore,
                title = "Restore from backup",
                supporting = "Replace what's on this phone with a Hanger backup file.",
                enabled = state.busy == null,
                onClick = actions.onRestore,
            )
            if (running) BusyFooter(state.busy!!)
        }
    }

    section("demo", "Demo closet") {
        val running = state.busy == BusyTask.LOAD_DEMO || state.busy == BusyTask.REMOVE_DEMO
        SettingsCard {
            if (state.demoItemCount > 0) {
                ActionRow(
                    icon = Icons.Outlined.DeleteOutline,
                    title = "Remove demo closet",
                    supporting = "Delete the ${plural(state.demoItemCount, "sample piece")} and their outfits. Your own items stay.",
                    enabled = state.busy == null,
                    onClick = actions.onRemoveDemo,
                )
            } else {
                ActionRow(
                    icon = Icons.Outlined.Checkroom,
                    title = "Load demo closet",
                    supporting = "Try Hanger with a ready-made wardrobe, outfits and wear history. Remove it any time.",
                    enabled = state.busy == null,
                    onClick = actions.onLoadDemo,
                )
            }
            if (running) BusyFooter(state.busy!!)
        }
    }

    section("about", "About") {
        SettingsCard {
            ListItem(
                leadingContent = { Icon(Icons.Outlined.Checkroom, contentDescription = null) },
                headlineContent = { Text(stringResource(R.string.app_name)) },
                supportingContent = { Text("Version ${BuildConfig.VERSION_NAME}") },
                colors = rowColors(),
            )
            RowDivider()
            ListItem(
                leadingContent = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                headlineContent = { Text("Private by design") },
                supportingContent = { Text(PRIVACY_NOTE) },
                colors = rowColors(),
            )
            RowDivider()
            ListItem(
                leadingContent = { Icon(Icons.Outlined.Cloud, contentDescription = null) },
                headlineContent = { Text("Weather data") },
                supportingContent = { Text("Forecasts by Open-Meteo.com, licensed under CC BY 4.0.") },
                colors = rowColors(),
            )
        }
    }
}

/** A titled group: the header and its card are separate list items with stable keys. */
private fun LazyListScope.section(key: String, title: String, content: @Composable () -> Unit) {
    item(key = "$key-header") {
        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = 4.dp, top = 20.dp, bottom = 8.dp)
                .semantics { heading() },
        )
    }
    item(key = key) { content() }
}

// ---- Location ----

@Composable
private fun LocationCard(state: SettingsUiState, query: String, actions: SettingsActions) {
    val s = state.settings
    val trimmed = query.trim()
    val search = state.search
    val focusManager = LocalFocusManager.current
    SettingsCard {
        ListItem(
            leadingContent = { Icon(Icons.Outlined.LocationOn, contentDescription = null) },
            headlineContent = { Text(s.locationName ?: "Not set") },
            supportingContent = {
                Text(
                    if (s.locationName != null) "Forecasts and weather-aware outfits use this place"
                    else "Set a place to get forecasts and outfits that suit the weather",
                )
            },
            trailingContent = if (s.locationName != null || s.hasLocation) {
                { TextButton(onClick = actions.onClearLocation) { Text("Clear") } }
            } else null,
            colors = rowColors(),
        )
        Column(
            Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = actions.onUseMyLocation,
                enabled = !state.locating,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
            ) {
                if (state.locating) {
                    CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Outlined.MyLocation, contentDescription = null, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(8.dp))
                Text(if (state.locating) "Finding you…" else "Use my location")
            }
            OutlinedTextField(
                value = query,
                onValueChange = actions.onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Or search for a town or city") },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                trailingIcon = when {
                    search.searching -> {
                        { CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp) }
                    }
                    query.isNotEmpty() -> {
                        {
                            IconButton(onClick = { actions.onQueryChange("") }) {
                                Icon(Icons.Outlined.Close, contentDescription = "Clear search")
                            }
                        }
                    }
                    else -> null
                },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
            )
        }
        if (trimmed.isNotEmpty()) {
            if (search.results.isNotEmpty()) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                search.results.forEach { place ->
                    val detail = listOf(place.region, place.country).filter { it.isNotBlank() && it != place.name }.joinToString(", ")
                    ListItem(
                        leadingContent = { Icon(Icons.Outlined.Place, contentDescription = null) },
                        headlineContent = { Text(place.name) },
                        supportingContent = if (detail.isNotEmpty()) {
                            { Text(detail) }
                        } else null,
                        colors = rowColors(),
                        modifier = Modifier.clickable(onClickLabel = "Use ${place.label} for weather", role = Role.Button) {
                            actions.onSelectPlace(place)
                        },
                    )
                }
            } else if (!search.searching && search.query == trimmed && trimmed.length >= 2) {
                Text(
                    "No places found for “$trimmed”. Try a nearby town or check the spelling.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                )
            }
        }
    }
}

// ---- Units, style defaults ----

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TempUnitToggle(selected: TempUnit, onSelect: (TempUnit) -> Unit) {
    SingleChoiceSegmentedButtonRow(Modifier.width(148.dp)) {
        TempUnit.entries.forEachIndexed { index, unit ->
            val (symbol, name) = when (unit) {
                TempUnit.CELSIUS -> "°C" to "Celsius"
                TempUnit.FAHRENHEIT -> "°F" to "Fahrenheit"
            }
            SegmentedButton(
                selected = selected == unit,
                onClick = { onSelect(unit) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = TempUnit.entries.size),
                label = { Text(symbol, Modifier.semantics { contentDescription = name }) },
            )
        }
    }
}

@Composable
private fun OccasionRow(selected: Occasion, onSelect: (Occasion) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ListItem(
        leadingContent = { Icon(Icons.Outlined.Style, contentDescription = null) },
        headlineContent = { Text("Default occasion") },
        supportingContent = { Text("What suggestions dress you for") },
        trailingContent = {
            Box {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(selected.label, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    Occasion.entries.forEach { occasion ->
                        DropdownMenuItem(
                            text = { Text(occasion.label) },
                            onClick = {
                                expanded = false
                                onSelect(occasion)
                            },
                            trailingIcon = if (occasion == selected) {
                                { Icon(Icons.Filled.Check, contentDescription = "Selected") }
                            } else null,
                        )
                    }
                }
            }
        },
        colors = rowColors(),
        modifier = Modifier.clickable(onClickLabel = "Choose default occasion", role = Role.DropdownList) { expanded = true },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CurrencyRow(selected: String, onSelect: (String) -> Unit) {
    Column {
        ListItem(
            leadingContent = { Icon(Icons.Outlined.Payments, contentDescription = null) },
            headlineContent = { Text("Currency") },
            supportingContent = { Text("For prices, closet value and cost per wear") },
            colors = rowColors(),
        )
        FlowRow(
            Modifier.padding(start = 56.dp, end = 16.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Currencies.forEach { (symbol, name) ->
                FilterChip(
                    selected = selected == symbol,
                    onClick = { onSelect(symbol) },
                    label = {
                        Text(
                            symbol,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.semantics { contentDescription = "$name ($symbol)" },
                        )
                    },
                )
            }
        }
    }
}

// ---- Dialogs ----

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderTimeDialog(initialMinutes: Int, is24Hour: Boolean, onDismiss: () -> Unit, onConfirm: (Int) -> Unit) {
    val pickerState = rememberTimePickerState(
        initialHour = initialMinutes / 60,
        initialMinute = initialMinutes % 60,
        is24Hour = is24Hour,
    )
    var typing by rememberSaveable { mutableStateOf(false) }
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 6.dp,
        ) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Reminder time",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                )
                if (typing) TimeInput(state = pickerState) else TimePicker(state = pickerState)
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { typing = !typing }) {
                        Icon(
                            if (typing) Icons.Outlined.Schedule else Icons.Outlined.Keyboard,
                            contentDescription = if (typing) "Switch to clock" else "Switch to typing the time",
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    TextButton(onClick = { onConfirm(pickerState.hour * 60 + pickerState.minute) }) { Text("Set") }
                }
            }
        }
    }
}

@Composable
private fun ConfirmDialog(title: String, body: String, confirmLabel: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(body) },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
            ) { Text(confirmLabel) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

// ---- Building blocks ----

@Composable
private fun SettingsCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(Modifier.padding(vertical = 4.dp), content = content)
    }
}

@Composable
private fun SwitchRow(
    icon: ImageVector,
    title: String,
    supporting: String?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    ListItem(
        leadingContent = { Icon(icon, contentDescription = null) },
        headlineContent = { Text(title) },
        supportingContent = supporting?.let { { Text(it) } },
        trailingContent = { Switch(checked = checked, onCheckedChange = null) },
        colors = rowColors(),
        modifier = Modifier.toggleable(value = checked, role = Role.Switch, onValueChange = onCheckedChange),
    )
}

@Composable
private fun ActionRow(icon: ImageVector, title: String, supporting: String, enabled: Boolean, onClick: () -> Unit) {
    val alpha = if (enabled) 1f else 0.5f
    ListItem(
        leadingContent = { Icon(icon, contentDescription = null) },
        headlineContent = { Text(title) },
        supportingContent = { Text(supporting) },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
            headlineColor = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
            supportingColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
            leadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
        ),
        modifier = Modifier.clickable(enabled = enabled, role = Role.Button, onClick = onClick),
    )
}

@Composable
private fun BusyFooter(task: BusyTask) {
    Column(Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp)) {
        Text(task.label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(Modifier.fillMaxWidth())
    }
}

@Composable
private fun RowDivider() {
    HorizontalDivider(Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant)
}

@Composable
private fun rowColors() = ListItemDefaults.colors(containerColor = Color.Transparent)

private fun formatMinutes(minutes: Int, is24Hour: Boolean): String {
    val time = LocalTime.of((minutes / 60).coerceIn(0, 23), (minutes % 60).coerceIn(0, 59))
    return time.format(DateTimeFormatter.ofPattern(if (is24Hour) "HH:mm" else "h:mm a", Locale.getDefault()))
}

private fun openNotificationSettings(context: Context) {
    val intent = Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        .putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, context.packageName)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    runCatching { context.startActivity(intent) }
}
