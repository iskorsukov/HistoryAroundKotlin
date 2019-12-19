package my.projects.historyaroundkotlin.presentation.view.detail.viewstate

import androidx.annotation.StringRes
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.ErrorItem

class DetailErrorItem(@StringRes labelRes: Int, @StringRes messageRes: Int): ErrorItem(labelRes, messageRes) {
    companion object {
        val ERROR = DetailErrorItem(
            R.string.loading_details_error_label,
            R.string.loading_details_error_message
        )
    }
}