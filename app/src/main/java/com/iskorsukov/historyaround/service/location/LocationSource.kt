package com.iskorsukov.historyaround.service.location

import android.location.Location
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

interface LocationSource {
    fun checkLocationServicesAvailability(): Completable

    fun getLocationUpdatesObservable(): Observable<Location>
    fun getLastKnownLocation(): Maybe<Location>

    fun startLocationUpdates()
    fun stopLocationUpdates()
}