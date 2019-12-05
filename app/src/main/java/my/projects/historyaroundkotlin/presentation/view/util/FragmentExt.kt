package my.projects.historyaroundkotlin.presentation.view.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import my.projects.historyaroundkotlin.HistoryAroundApp

fun Fragment.viewModelFactory(): ViewModelProvider.Factory {
    return (context!!.applicationContext as HistoryAroundApp).appComponent.viewModelFactory()
}