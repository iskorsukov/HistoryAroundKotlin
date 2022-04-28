package com.iskorsukov.historyaround.service.location

import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resumeWithException

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
        return suspendCancellableCoroutine { continuation ->
            locationClient.checkLocationServicesAvailability(locationRequest).addOnCompleteListener {
                if (!it.isSuccessful) {
                    it.exception?.apply {
                        continuation.resumeWithException(this)
                    } ?: continuation.resumeWithException(
                        IllegalStateException("Location settings availability error")
                    )
                } else if (it.isCanceled) {
                    continuation.cancel()
                } else {
                    continuation.resumeWith(Result.success(it.result))
                }
            }
        }
    }

    override suspend fun getCurrentLocation(): Location {
        val cancellationToken = cancellationTokenSource.token
        return suspendCancellableCoroutine { continuation ->
            locationClient.currentLocation(cancellationToken).addOnCompleteListener {
                if (!it.isSuccessful) {
                    it.exception?.apply {
                        continuation.resumeWithException(this)
                    } ?: continuation.resumeWithException(
                        IllegalStateException("Current location not received")
                    )
                } else if (it.isCanceled) {
                    continuation.cancel()
                } else {
                    continuation.resumeWith(Result.success(it.result))
                }
            }
        }
    }
}