package my.projects.historyaroundkotlin.service.location

import android.location.Location
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface LocationSource {
    fun getLocationUpdatesObservable(): Observable<Location>
    fun getLastKnownLocation(): Maybe<Location>

    fun startLocationUpdates()
    fun stopLocationUpdates()
}