package com.kintukka.hanger.ui.settings

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kintukka.hanger.AppContainer
import com.kintukka.hanger.backup.BackupSummary
import com.kintukka.hanger.data.repo.Settings
import com.kintukka.hanger.data.repo.TempUnit
import com.kintukka.hanger.demo.DemoCloset
import com.kintukka.hanger.engine.Occasion
import com.kintukka.hanger.ui.plural
import com.kintukka.hanger.weather.Place
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** A long-running job the screen shows progress for. Only one runs at a time. */
enum class BusyTask(val label: String) {
    EXPORT("Saving backup…"),
    RESTORE("Restoring backup…"),
    LOAD_DEMO("Setting up the demo closet…"),
    REMOVE_DEMO("Removing the demo closet…"),
}

@Immutable
data class PlaceSearch(
    val query: String = "",
    val results: List<Place> = emptyList(),
    val searching: Boolean = false,
)

@Immutable
data class SettingsUiState(
    val settings: Settings = Settings(),
    val loaded: Boolean = false,
    val demoItemCount: Int = 0,
    val locating: Boolean = false,
    val busy: BusyTask? = null,
    val search: PlaceSearch = PlaceSearch(),
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SettingsViewModel(private val container: AppContainer) : ViewModel() {
    private val settingsRepo = container.settings

    private val query = MutableStateFlow("")
    private val locating = MutableStateFlow(false)
    private val busy = MutableStateFlow<BusyTask?>(null)

    private val messageChannel = Channel<String>(Channel.BUFFERED)

    /** One-off messages for the snackbar. */
    val messages: Flow<String> = messageChannel.receiveAsFlow()

    private val search: Flow<PlaceSearch> = query
        .map { it.trim() }
        .distinctUntilChanged()
        // Clearing the field should clear results at once; typing waits for a pause.
        .debounce { if (it.length < MIN_QUERY) 0L else 400L }
        .transformLatest { q ->
            if (q.length < MIN_QUERY) {
                emit(PlaceSearch(q))
            } else {
                emit(PlaceSearch(q, searching = true))
                val results = try {
                    container.weather.searchPlaces(q)
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    emptyList()
                }
                emit(PlaceSearch(q, results))
            }
        }

    val state: StateFlow<SettingsUiState> = combine(
        settingsRepo.settings,
        container.repo.demoItemCount,
        locating,
        busy,
        search,
    ) { settings, demo, isLocating, task, places ->
        SettingsUiState(settings, loaded = true, demoItemCount = demo, locating = isLocating, busy = task, search = places)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    // ---- Location ----

    fun onQueryChange(text: String) {
        query.value = text
    }

    fun selectPlace(place: Place) {
        query.value = ""
        viewModelScope.launch {
            settingsRepo.setLocation(place.shortLabel(), place.latitude, place.longitude)
            say("Weather location set to ${place.name}")
        }
    }

    /** Call once ACCESS_COARSE_LOCATION is granted. */
    fun useDeviceLocation() {
        if (locating.value) return
        locating.value = true
        viewModelScope.launch {
            try {
                val place = try {
                    container.location.current()
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    null
                }
                if (place != null) {
                    settingsRepo.setLocation(place.shortLabel(), place.latitude, place.longitude)
                    say("Weather location set to ${place.name}")
                } else {
                    say("Couldn't find where you are. Check location is switched on, or search for your town.")
                }
            } finally {
                locating.value = false
            }
        }
    }

    fun clearLocation() {
        viewModelScope.launch { settingsRepo.clearLocation() }
    }

    // ---- Preferences ----

    fun setTempUnit(unit: TempUnit) {
        viewModelScope.launch { settingsRepo.setTempUnit(unit) }
    }

    fun setReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val minutes = settingsRepo.current().reminderMinutes
            settingsRepo.setReminder(enabled, minutes)
        }
    }

    fun setReminderTime(minutes: Int) {
        viewModelScope.launch {
            val enabled = settingsRepo.current().reminderEnabled
            settingsRepo.setReminder(enabled, minutes.coerceIn(0, 24 * 60 - 1))
        }
    }

    fun setAutoLaundry(enabled: Boolean) {
        viewModelScope.launch { settingsRepo.setAutoLaundry(enabled) }
    }

    fun setCurrency(symbol: String) {
        viewModelScope.launch { settingsRepo.setCurrency(symbol) }
    }

    fun setDefaultOccasion(occasion: Occasion) {
        viewModelScope.launch { settingsRepo.setDefaultOccasion(occasion) }
    }

    fun say(message: String) {
        messageChannel.trySend(message)
    }

    // ---- Backup & demo ----

    fun export(target: Uri) = runBusy(
        task = BusyTask.EXPORT,
        work = { container.backup.export(target) },
        done = { "Saved ${it.describe()}" },
        failed = { "Couldn't save the backup${it.reason()}" },
    )

    fun restore(source: Uri) = runBusy(
        task = BusyTask.RESTORE,
        work = { container.backup.restore(source) },
        done = { "Restored ${it.describe()}" },
        failed = { "Couldn't restore that file${it.reason()}" },
    )

    fun loadDemo() = runBusy(
        task = BusyTask.LOAD_DEMO,
        work = { DemoCloset.load(container.db, container.images) },
        done = { "Demo closet added. Have a look around." },
        failed = { "Couldn't load the demo closet${it.reason()}" },
    )

    fun removeDemo() = runBusy(
        task = BusyTask.REMOVE_DEMO,
        work = { container.repo.clearDemo() },
        done = { "Demo closet removed" },
        failed = { "Couldn't remove the demo closet${it.reason()}" },
    )

    /**
     * Runs [work] in the app scope so a backup or restore isn't cut off half-way if the user leaves
     * the screen; this ViewModel only waits for the result to report it.
     */
    private fun <T> runBusy(task: BusyTask, work: suspend () -> T, done: (T) -> String, failed: (Throwable) -> String) {
        if (busy.value != null) return
        busy.value = task
        viewModelScope.launch {
            try {
                val result = container.appScope.async { work() }.await()
                say(done(result))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                say(failed(e))
            } finally {
                busy.update { if (it == task) null else it }
            }
        }
    }

    private companion object {
        const val MIN_QUERY = 2

        fun Place.shortLabel(): String =
            listOf(name, country).filter { it.isNotBlank() }.distinct().joinToString(", ")

        fun BackupSummary.describe(): String =
            "${plural(items, "item")}, ${plural(outfits, "outfit")}, ${plural(images, "photo")}"

        fun Throwable.reason(): String = message?.takeIf { it.isNotBlank() }?.let { ": $it" } ?: "."
    }
}
