package com.iskorsukov.historyaround.service.location

import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationSourceImpl @Inject constructor(
    private val locationClient: LocationClient
): LocationSource {
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 5000
    }

    private val cancellationTokenSource = CancellationTokenSource()

    override fun checkLocationServicesAvailability(): Task<LocationSettingsResponse> {
        return locationClient.checkLocationServicesAvailability(locationRequest)
    }

    override fun getCurrentLocation(): Maybe<Location> {
        val cancellationToken = cancellationTokenSource.token
        return Maybe.create { emitter ->
            locationClient.currentLocation(cancellationToken).addOnCompleteListener {
                if (!it.isSuccessful) {
                    it.exception?.apply {
                        emitter.onError(this)
                    } ?: emitter.onError(IllegalStateException("Last location error"))
                } else if (it.isCanceled || it.result == null) {
                    emitter.onComplete()
                } else {
                    emitter.onSuccess(it.result!!)
                }
            }
        }
    }
}