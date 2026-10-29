package com.kintukka.hanger.data.repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kintukka.hanger.engine.AffinityModel
import com.kintukka.hanger.engine.Hemisphere
import com.kintukka.hanger.engine.Occasion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hanger_settings")

enum class TempUnit { CELSIUS, FAHRENHEIT }

data class Settings(
    val locationName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val tempUnit: TempUnit = TempUnit.CELSIUS,
    val reminderEnabled: Boolean = false,
    /** Minutes after midnight. */
    val reminderMinutes: Int = 7 * 60 + 30,
    val autoLaundry: Boolean = true,
    val currency: String = "£",
    val defaultOccasion: Occasion = Occasion.CASUAL,
    /** After this hour the Today screen looks ahead to tomorrow. */
    val tomorrowFromHour: Int = 18,
    val onboarded: Boolean = false,
) {
    val hasLocation: Boolean get() = latitude != null && longitude != null
    val hemisphere: Hemisphere get() = if ((latitude ?: 1.0) < 0) Hemisphere.SOUTH else Hemisphere.NORTH
}

@Serializable
private data class AffinityJson(val pairs: Map<String, Double> = emptyMap(), val items: Map<Long, Double> = emptyMap())

class SettingsRepository(private val context: Context) {
    private val store get() = context.dataStore
    private val json = Json { ignoreUnknownKeys = true }

    private object Keys {
        val locationName = stringPreferencesKey("location_name")
        val latitude = doublePreferencesKey("latitude")
        val longitude = doublePreferencesKey("longitude")
        val tempUnit = stringPreferencesKey("temp_unit")
        val reminderEnabled = booleanPreferencesKey("reminder_enabled")
        val reminderMinutes = intPreferencesKey("reminder_minutes")
        val autoLaundry = booleanPreferencesKey("auto_laundry")
        val currency = stringPreferencesKey("currency")
        val defaultOccasion = stringPreferencesKey("default_occasion")
        val tomorrowFromHour = intPreferencesKey("tomorrow_from_hour")
        val onboarded = booleanPreferencesKey("onboarded")
        val affinity = stringPreferencesKey("affinity_model")
        val weatherCache = stringPreferencesKey("weather_cache")
        /** Left over from the removed AI stylist; wiped on start-up so no key lingers on the phone. */
        val legacyStylistApiKey = stringPreferencesKey("stylist_api_key")
    }

    val settings: Flow<Settings> = store.data.map { p ->
        Settings(
            locationName = p[Keys.locationName],
            latitude = p[Keys.latitude],
            longitude = p[Keys.longitude],
            tempUnit = p[Keys.tempUnit]?.let { runCatching { TempUnit.valueOf(it) }.getOrNull() } ?: TempUnit.CELSIUS,
            reminderEnabled = p[Keys.reminderEnabled] ?: false,
            reminderMinutes = p[Keys.reminderMinutes] ?: (7 * 60 + 30),
            autoLaundry = p[Keys.autoLaundry] ?: true,
            currency = p[Keys.currency] ?: "£",
            defaultOccasion = p[Keys.defaultOccasion]?.let { runCatching { Occasion.valueOf(it) }.getOrNull() } ?: Occasion.CASUAL,
            tomorrowFromHour = p[Keys.tomorrowFromHour] ?: 18,
            onboarded = p[Keys.onboarded] ?: false,
        )
    }

    suspend fun current(): Settings = settings.first()

    suspend fun setLocation(name: String, latitude: Double, longitude: Double) = store.edit {
        it[Keys.locationName] = name
        it[Keys.latitude] = latitude
        it[Keys.longitude] = longitude
        it.remove(Keys.weatherCache)
    }

    suspend fun clearLocation() = store.edit {
        it.remove(Keys.locationName); it.remove(Keys.latitude); it.remove(Keys.longitude); it.remove(Keys.weatherCache)
    }

    suspend fun setTempUnit(unit: TempUnit) = store.edit { it[Keys.tempUnit] = unit.name }
    suspend fun setReminder(enabled: Boolean, minutes: Int) = store.edit {
        it[Keys.reminderEnabled] = enabled
        it[Keys.reminderMinutes] = minutes
    }
    suspend fun setAutoLaundry(enabled: Boolean) = store.edit { it[Keys.autoLaundry] = enabled }
    suspend fun setCurrency(symbol: String) = store.edit { it[Keys.currency] = symbol.take(3) }
    suspend fun setDefaultOccasion(occasion: Occasion) = store.edit { it[Keys.defaultOccasion] = occasion.name }
    suspend fun setOnboarded() = store.edit { it[Keys.onboarded] = true }
    suspend fun removeLegacyApiKey() {
        if (store.data.first()[Keys.legacyStylistApiKey] != null) store.edit { it.remove(Keys.legacyStylistApiKey) }
    }

    val affinity: Flow<AffinityModel> = store.data.map { p -> decodeAffinity(p[Keys.affinity]) }

    suspend fun updateAffinity(transform: (AffinityModel) -> AffinityModel) = store.edit { p ->
        val next = transform(decodeAffinity(p[Keys.affinity]))
        p[Keys.affinity] = json.encodeToString(AffinityJson(next.pairScores, next.itemScores))
    }

    suspend fun weatherCache(): String? = store.data.first()[Keys.weatherCache]
    suspend fun setWeatherCache(value: String) = store.edit { it[Keys.weatherCache] = value }

    private fun decodeAffinity(raw: String?): AffinityModel {
        if (raw.isNullOrEmpty()) return AffinityModel()
        return runCatching {
            val a = json.decodeFromString<AffinityJson>(raw)
            AffinityModel(a.pairs, a.items)
        }.getOrDefault(AffinityModel())
    }
}
