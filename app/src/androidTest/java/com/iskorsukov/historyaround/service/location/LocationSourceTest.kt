package com.iskorsukov.historyaround.service.location

import android.location.Location
import android.os.Handler
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.iskorsukov.historyaround.utils.MockitoUtil
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class LocationSourceTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val locationClient = Mockito.mock(LocationClient::class.java)
    private val locationSource = LocationSourceImpl(locationClient)
    private val sampleLocation = Location("test").also {
        it.latitude = 1.0
        it.longitude = 1.0
        it.time = System.currentTimeMillis()
    }


    private fun pushLocationServicesAvailable() {
        Mockito.`when`(locationClient.checkLocationServicesAvailability(MockitoUtil.any())).thenReturn(LocationTasks.getLocationServicesAvailableTask())
    }

    @Test
    fun checksLocationServicesAvailability() {
        pushLocationServicesAvailable()

        val throwable = locationSource.checkLocationServicesAvailability().blockingGet()

        assertNull(throwable)
    }

    private fun pushLastLocation(location: Location) {
        Mockito.`when`(locationClient.lastLocation()).thenReturn(LocationTasks.getLastKnownLocationTask(location))
    }

    @Test
    fun returnsLastKnownLocation() {
        pushLastLocation(sampleLocation)

        val location = locationSource.getLastKnownLocation().blockingGet()

        assertEquals(sampleLocation, location)
    }

    private fun pushNoLastKnownLocation() {
        Mockito.`when`(locationClient.lastLocation()).thenReturn(LocationTasks.getNoLastKnownLocationTask())
    }

    @Test
    fun completesOnNoLastKnownLocation() {
        pushNoLastKnownLocation()

        val location = locationSource.getLastKnownLocation().blockingGet()

        assertNull(location)
    }

    private fun pushLocationUpdates(location: Location) {
        Mockito.`when`(locationClient.startLocationUpdates(MockitoUtil.any(), MockitoUtil.any())).then { invocation ->
            val callback = invocation.arguments[1] as LocationCallback
            Thread {
                while (true) {
                    TimeUnit.SECONDS.sleep(1)
                    callback.onLocationResult(LocationResult.create(listOf(location)))
                }
            }.start()
        }
    }

    @Test
    fun pushesLocationUpdatesToObservable() {
        pushLocationUpdates(sampleLocation)

        locationSource.startLocationUpdates()
        val location = locationSource.getLocationUpdatesObservable().firstOrError().blockingGet()

        assertEquals(sampleLocation, location)
    }
}