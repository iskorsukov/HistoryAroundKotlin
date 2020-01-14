package com.iskorsukov.historyaround.service.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationSourceImpl @Inject constructor(
    private val locationClient: LocationClient
): LocationSource {
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 500
    }

    private val locationUpdatesSubject: PublishSubject<Location> = PublishSubject.create()

    override fun checkLocationServicesAvailability(): Completable {
        return Completable.create { emitter ->
            locationClient.checkLocationServicesAvailability(locationRequest).addOnCompleteListener {
                if (!it.isSuccessful || it.isCanceled) {
                    emitter.onError(IllegalStateException("Location services unavailable"))
                }
                emitter.onComplete()
            }
        }
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.run {
                this.locations.maxBy { location -> location.accuracy }?.run {
                    locationUpdatesSubject.onNext(this)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates() {
        locationClient.startLocationUpdates(locationRequest, locationCallback)
    }

    override fun stopLocationUpdates() {
        locationClient.stopLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    override fun getLastKnownLocation(): Maybe<Location> {
        return Maybe.create { emitter ->
            locationClient.lastLocation().addOnCompleteListener {
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

    override fun getLocationUpdatesObservable(): Observable<Location> {
        return locationUpdatesSubject
    }
}