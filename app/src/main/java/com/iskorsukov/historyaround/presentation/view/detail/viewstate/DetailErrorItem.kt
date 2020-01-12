package com.iskorsukov.historyaround.presentation.view.detail.viewstate

import androidx.annotation.StringRes
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.presentation.view.common.viewstate.ErrorItem

class DetailErrorItem(@StringRes labelRes: Int, @StringRes messageRes: Int): ErrorItem(labelRes, messageRes) {
    companion object {
        val ERROR = DetailErrorItem(
            R.string.loading_details_error_label,
            R.string.loading_details_error_message
        )
    }
}