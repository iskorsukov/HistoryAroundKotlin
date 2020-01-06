package my.projects.historyaroundkotlin.presentation.view.detail.viewstate

import androidx.annotation.StringRes
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LoadingItem

class DetailLoadingItem(@StringRes messageRes: Int): LoadingItem(messageRes) {
    companion object {
        val LOADING_DETAILS = DetailLoadingItem(R.string.loading_details_label)
    }
}