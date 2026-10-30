package com.kintukka.hanger

import android.app.Application
import com.kintukka.hanger.engine.StyleGuide
import com.kintukka.hanger.notify.Reminders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HangerApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        Reminders.createChannel(this)
        container.appScope.launch { container.settings.removeLegacyApiKey() }
        // Parse the bundled style guide off the main thread before the first screen needs it.
        container.appScope.launch(Dispatchers.Default) { runCatching { StyleGuide.knowledge } }
        container.appScope.launch {
            container.settings.settings
                .map { it.reminderEnabled to it.reminderMinutes }
                .distinctUntilChanged()
                .collect { (enabled, minutes) -> Reminders.schedule(this@HangerApp, enabled, minutes) }
        }
        container.processor.prefetchModels()
    }
}
