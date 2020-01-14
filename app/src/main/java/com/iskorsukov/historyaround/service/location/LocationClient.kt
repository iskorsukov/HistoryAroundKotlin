package com.iskorsukov.historyaround.service.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.iskorsukov.historyaround.mock.Mockable
import javax.inject.Inject

// Delegate class for FusedLocationProviderClient
@Mockable
class LocationClient @Inject constructor(private val context: Context, private val fusedLocationProviderClient: FusedLocationProviderClient) {

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(locationRequest: LocationRequest, locationCallback: LocationCallback) {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    fun stopLocationUpdates(locationCallback: LocationCallback) {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    fun lastLocation(): Task<Location> = fusedLocationProviderClient.lastLocation

    fun checkLocationServicesAvailability(locationRequest: LocationRequest): Task<LocationSettingsResponse> {
        val locationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
        val settingsClient = LocationServices.getSettingsClient(context)
        return settingsClient.checkLocationSettings(locationSettingsRequest)
    }
}