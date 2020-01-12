package com.iskorsukov.historyaround.presentation.view.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.iskorsukov.historyaround.HistoryAroundApp

fun Fragment.viewModelFactory(): ViewModelProvider.Factory {
    return (context!!.applicationContext as HistoryAroundApp).appComponent.viewModelFactory()
}