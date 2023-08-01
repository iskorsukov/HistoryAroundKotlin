package com.iskorsukov.historyaround.service.location

import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.CancellationTokenSource
import com.iskorsukov.historyaround.injection.DispatcherIO
import com.iskorsukov.historyaround.presentation.viewmodel.map.throwable.LocationErrorThrowable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class LocationSourceImpl @Inject constructor(
    private val locationClient: LocationClient
): LocationSource {
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 5000
    }

    private val cancellationTokenSource = CancellationTokenSource()

    override suspend fun checkLocationServicesAvailability(): LocationSettingsResponse {
        return locationClient.checkLocationServicesAvailability(locationRequest).await()
    }

    override suspend fun getCurrentLocation(): Location {
        val cancellationToken = cancellationTokenSource.token
        val location = locationClient.currentLocation(cancellationToken).await(cancellationTokenSource)
        if (location == null) {
            throw IllegalStateException("Failed to get current location")
        } else {
            return location
        }
    }

    override fun cancel() {
        cancellationTokenSource.cancel()
    }
}