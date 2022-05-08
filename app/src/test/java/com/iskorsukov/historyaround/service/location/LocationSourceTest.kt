package com.iskorsukov.historyaround.service.location

import android.app.Activity
import android.location.Location
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.concurrent.Executor

@ExperimentalCoroutinesApi
class LocationSourceTest {

    private val locationClient: LocationClient = mockk()

    private val locationSource: LocationSource = LocationSourceImpl(locationClient)

    @Test
    fun checkLocationServicesAvailability() = runTest {
        val task = TestTask<LocationSettingsResponse>()
        every {
            locationClient.checkLocationServicesAvailability(any())
        } returns task

        var locationSettingsResponse: LocationSettingsResponse? = null
        launch { locationSettingsResponse = locationSource.checkLocationServicesAvailability() }
        advanceTimeBy(100)

        task.success(LocationSettingsResponse(LocationSettingsResult(Status.RESULT_SUCCESS, null)))
        advanceUntilIdle()

        assertThat(locationSettingsResponse).isNotNull()
    }

    @Test(expected = ApiException::class)
    fun checkLocationServicesAvailability_Exception() = runTest {
        val task = TestTask<LocationSettingsResponse>()
        every {
            locationClient.checkLocationServicesAvailability(any())
        } returns task

        launch { locationSource.checkLocationServicesAvailability() }
        advanceTimeBy(100)

        task.failure(ApiException(Status.RESULT_INTERNAL_ERROR))
        advanceUntilIdle()
    }

    @Test
    fun checkLocationServicesAvailability_Cancelled() = runTest {
        val task = TestTask<LocationSettingsResponse>()
        every {
            locationClient.checkLocationServicesAvailability(any())
        } returns task

        val job = launch { locationSource.checkLocationServicesAvailability() }
        advanceTimeBy(100)

        task.cancel()
        advanceUntilIdle()

        assertThat(job.isCancelled).isTrue()
    }

    @Test
    fun getCurrentLocation() = runTest {
        val task = TestTask<Location?>()
        every {
            locationClient.currentLocation(any())
        } returns task

        var location: Location? = null
        launch { location = locationSource.getCurrentLocation() }
        advanceTimeBy(100)

        task.success(Location("test"))
        advanceUntilIdle()

        assertThat(location).isNotNull()
    }

    @Test(expected = IllegalStateException::class)
    fun getCurrentLocation_Null() = runTest {
        val task = TestTask<Location?>()
        every {
            locationClient.currentLocation(any())
        } returns task

        launch { locationSource.getCurrentLocation() }
        advanceTimeBy(100)

        task.success(null)
        advanceUntilIdle()
    }

    @Test(expected = IllegalStateException::class)
    fun getCurrentLocation_Exception() = runTest {
        val task = TestTask<Location?>()
        every {
            locationClient.currentLocation(any())
        } returns task

        launch { locationSource.getCurrentLocation() }
        advanceTimeBy(100)

        task.failure(IllegalStateException())
        advanceUntilIdle()
    }

    @Test
    fun getCurrentLocation_Cancelled() = runTest {
        val task = TestTask<Location?>()
        every {
            locationClient.currentLocation(any())
        } returns task

        val job = launch { locationSource.getCurrentLocation() }
        advanceTimeBy(100)

        task.cancel()
        advanceUntilIdle()

        assertThat(job.isCancelled).isTrue()
    }
}

class TestTask<T>: Task<T>() {
    private var onFailureListener: OnFailureListener? = null
    private var onSuccessListener: OnSuccessListener<in T>? = null
    private var onCompleteListener: OnCompleteListener<T>? = null

    private var result: T? = null
    private var exception: Exception? = null
    private var isCancelled = false

    fun failure(exception: Exception) {
        this.exception = exception
        onFailureListener?.onFailure(exception)
        onCompleteListener?.onComplete(this)
    }

    fun success(result: T?) {
        this.result = result
        exception = null
        onSuccessListener?.onSuccess(result)
        onCompleteListener?.onComplete(this)
    }

    fun cancel() {
        isCancelled = true
        onCompleteListener?.onComplete(this)
    }

    override fun addOnCompleteListener(listener: OnCompleteListener<T>): Task<T> {
        onCompleteListener = listener
        return this
    }

    override fun addOnCompleteListener(executor: Executor, listener: OnCompleteListener<T>): Task<T> {
        onCompleteListener = listener
        return this
    }

    override fun addOnFailureListener(listener: OnFailureListener): Task<T> {
        onFailureListener = listener
        return this
    }

    override fun addOnFailureListener(
        activity: Activity,
        listener: OnFailureListener
    ): Task<T> {
        onFailureListener = listener
        return this
    }

    override fun addOnFailureListener(
        executor: Executor,
        listener: OnFailureListener
    ): Task<T> {
        onFailureListener = listener
        return this
    }

    override fun addOnSuccessListener(listener: OnSuccessListener<in T>): Task<T> {
        onSuccessListener = listener
        return this
    }

    override fun addOnSuccessListener(
        activity: Activity,
        listener: OnSuccessListener<in T>
    ): Task<T> {
        onSuccessListener = listener
        return this
    }

    override fun addOnSuccessListener(
        executor: Executor,
        listener: OnSuccessListener<in T>
    ): Task<T> {
        onSuccessListener = listener
        return this
    }

    override fun getException(): Exception? {
        return exception
    }

    override fun getResult(): T {
        if (result == null) {
            throw IllegalStateException("No result")
        } else {
            return result!!
        }
    }

    override fun <X : Throwable?> getResult(clazz: Class<X>): T {
        if (result == null) {
            throw IllegalStateException("No result")
        } else {
            return result!!
        }
    }

    override fun isCanceled(): Boolean {
        return isCancelled
    }

    override fun isComplete(): Boolean {
        return result != null || exception != null || isCancelled
    }

    override fun isSuccessful(): Boolean {
        return result != null && exception == null && !isCancelled
    }
}