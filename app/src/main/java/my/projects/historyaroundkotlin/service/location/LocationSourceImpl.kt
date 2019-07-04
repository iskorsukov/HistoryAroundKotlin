package my.projects.historyaroundkotlin.service.location

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationSourceImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
): LocationSource {
    private val locationRequest: LocationRequest = LocationRequest.create()
    private val locationUpdatesSubject: BehaviorSubject<Location> = BehaviorSubject.create()

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
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    override fun getLastKnownLocation(): Maybe<Location> {
        return Maybe.create { emitter ->
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                it?.apply {
                    emitter.onSuccess(it)
                } ?: emitter.onComplete()
            }.addOnFailureListener {
                emitter.onError(it)
            }.addOnCanceledListener {
                emitter.onComplete()
            }
        }
    }

    override fun getLocationUpdatesObservable(): Observable<Location> {
        return locationUpdatesSubject
    }
}