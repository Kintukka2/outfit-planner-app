package com.kintukka.hanger.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

/** One-shot coarse location lookup used to set the weather location. */
class DeviceLocation(private val context: Context) {

    fun hasPermission(): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    suspend fun current(): Place? {
        if (!hasPermission()) return null
        val client = LocationServices.getFusedLocationProviderClient(context)
        val location = withTimeoutOrNull(15_000) {
            runCatching { client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).await() }.getOrNull()
                ?: runCatching { client.lastLocation.await() }.getOrNull()
        } ?: return null
        val name = withContext(Dispatchers.IO) { placeName(location.latitude, location.longitude) } ?: "Current location"
        return Place(name, "", "", location.latitude, location.longitude)
    }

    @Suppress("DEPRECATION")
    private fun placeName(lat: Double, lon: Double): String? = runCatching {
        if (!Geocoder.isPresent()) return null
        val address = Geocoder(context).getFromLocation(lat, lon, 1)?.firstOrNull() ?: return null
        address.locality ?: address.subAdminArea ?: address.adminArea
    }.getOrNull()
}
