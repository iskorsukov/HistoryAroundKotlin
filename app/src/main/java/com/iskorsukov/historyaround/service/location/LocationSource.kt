package com.iskorsukov.historyaround.service.location

import android.location.Location
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.Task
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

interface LocationSource {

    suspend fun checkLocationServicesAvailability(): LocationSettingsResponse

    suspend fun getCurrentLocation(): Location
}