package my.projects.historyaroundkotlin.service.location

import android.location.Location
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.Task
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface LocationSource {
    fun checkLocationServicesAvailability(): Completable

    fun getLocationUpdatesObservable(): Observable<Location>
    fun getLastKnownLocation(): Maybe<Location>

    fun startLocationUpdates()
    fun stopLocationUpdates()
}