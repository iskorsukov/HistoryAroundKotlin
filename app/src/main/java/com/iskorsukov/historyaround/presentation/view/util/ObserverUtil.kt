package com.iskorsukov.historyaround.presentation.view.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeSingle(owner: LifecycleOwner, action: (item: T) -> Unit) {
    this.observe(owner, object : Observer<T> {
        override fun onChanged(item: T) {
            this@observeSingle.removeObserver(this)
            action(item)
        }
    })
}