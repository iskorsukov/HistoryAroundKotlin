package com.iskorsukov.historyaround.service.location

import android.app.Activity
import android.location.Location
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import java.lang.Exception
import java.util.concurrent.Executor

class LocationTasks {
    companion object {
        fun getLocationServicesAvailableTask(): Task<LocationSettingsResponse> {
            return CompletedSuccessfulTask(LocationSettingsResponse())
        }

        fun getLastKnownLocationTask(location: Location): Task<Location> {
            return CompletedSuccessfulTask(location)
        }

        fun getNoLastKnownLocationTask(): Task<Location> {
            return CompletedSuccessfulTask(null)
        }
    }
}

abstract class BaseTask<T>: Task<T>() {

    override fun addOnFailureListener(p0: OnFailureListener): Task<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addOnFailureListener(p0: Executor, p1: OnFailureListener): Task<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addOnFailureListener(p0: Activity, p1: OnFailureListener): Task<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <X : Throwable?> getResult(p0: Class<X>): T? {
        return result
    }

    override fun addOnSuccessListener(p0: OnSuccessListener<in T>): Task<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addOnSuccessListener(p0: Executor, p1: OnSuccessListener<in T>): Task<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addOnSuccessListener(p0: Activity, p1: OnSuccessListener<in T>): Task<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class CompletedSuccessfulTask<T>(private val data: T?): BaseTask<T>() {
    override fun getResult(): T? {
        return data
    }

    override fun isComplete(): Boolean {
        return true
    }

    override fun getException(): Exception? {
        return null
    }

    override fun isSuccessful(): Boolean {
        return true
    }

    override fun isCanceled(): Boolean {
        return false
    }

    override fun addOnCompleteListener(p0: OnCompleteListener<T>): Task<T> {
        p0.onComplete(this)
        return this
    }
}