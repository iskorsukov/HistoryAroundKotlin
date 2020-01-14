package com.iskorsukov.historyaround.presentation.view.detail.viewstate

import androidx.annotation.StringRes
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.presentation.view.common.viewstate.LoadingItem

class DetailLoadingItem(@StringRes messageRes: Int): LoadingItem(messageRes) {
    companion object {
        val LOADING_DETAILS = DetailLoadingItem(R.string.loading_details_label)
    }
}