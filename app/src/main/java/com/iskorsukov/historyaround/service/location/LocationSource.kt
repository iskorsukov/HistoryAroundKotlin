package com.iskorsukov.historyaround.service.location

import android.location.Location
import com.google.android.gms.location.LocationSettingsResponse

interface LocationSource {

    suspend fun checkLocationServicesAvailability(): LocationSettingsResponse

    suspend fun getCurrentLocation(): Location

    fun cancel()
}