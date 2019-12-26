package my.projects.historyaroundkotlin.utils

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> waitForValue(liveData: LiveData<T>): T {
    val latch = CountDownLatch(1)
    val handler = Handler(Looper.getMainLooper())
    var value: T? = liveData.value
    if (value == null) {
        handler.post {
            liveData.observeForever {
                value = it
                latch.countDown()
            }
        }
        latch.await(2, TimeUnit.SECONDS)
    }
    if (value == null) {
        throw TimeoutException("Did not get a value before timeout")
    } else {
        return value!!
    }
}