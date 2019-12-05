package my.projects.historyaroundkotlin.service.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationSourceImpl @Inject constructor(
    private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
): LocationSource {

    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 500
    }
    private val locationUpdatesSubject: PublishSubject<Location> = PublishSubject.create()

    override fun checkLocationServicesAvailability(): Completable {
        return Completable.create { emitter ->
            val locationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
            val settingsClient = LocationServices.getSettingsClient(context)
            val task = settingsClient.checkLocationSettings(locationSettingsRequest)
            task.addOnCompleteListener {
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