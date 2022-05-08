package com.iskorsukov.historyaround.service.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.Task
import javax.inject.Inject

// Delegate class for FusedLocationProviderClient
class LocationClient @Inject constructor(
    private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient) {

    @SuppressLint("MissingPermission")
    fun currentLocation(cancellationToken: CancellationToken): Task<Location?> {
        return fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY, cancellationToken
        )
    }

    fun checkLocationServicesAvailability(locationRequest: LocationRequest): Task<LocationSettingsResponse> {
        val locationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
        val settingsClient = LocationServices.getSettingsClient(context)
        return settingsClient.checkLocationSettings(locationSettingsRequest)
    }
}