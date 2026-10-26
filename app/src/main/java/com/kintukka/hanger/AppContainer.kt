package com.kintukka.hanger

import android.content.Context
import android.net.Uri
import com.kintukka.hanger.backup.BackupManager
import com.kintukka.hanger.data.db.HangerDatabase
import com.kintukka.hanger.data.repo.SettingsRepository
import com.kintukka.hanger.data.repo.Stylist
import com.kintukka.hanger.data.repo.WardrobeRepository
import com.kintukka.hanger.images.GarmentProcessor
import com.kintukka.hanger.images.ImageStore
import com.kintukka.hanger.weather.DeviceLocation
import com.kintukka.hanger.weather.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow

/** Manual dependency graph; one instance per process. */
class AppContainer(
    val context: Context,
    val db: HangerDatabase = HangerDatabase.build(context),
) {
    val appScope = CoroutineScope(SupervisorJob())
    val images = ImageStore(context)
    val settings = SettingsRepository(context)
    val repo = WardrobeRepository(db, images, settings, appScope)
    val weather = WeatherRepository(settings)
    val stylist = Stylist(repo, weather, settings)
    val processor = GarmentProcessor(context, images)
    val backup = BackupManager(context, db, images)
    val location = DeviceLocation(context)

    /** Photos picked/captured and waiting to be reviewed on the "Add items" screen. */
    val pendingCaptures = MutableStateFlow<List<Uri>>(emptyList())
}
