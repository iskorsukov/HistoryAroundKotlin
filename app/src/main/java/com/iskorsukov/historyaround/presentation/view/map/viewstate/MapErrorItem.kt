package com.iskorsukov.historyaround.presentation.view.map.viewstate

import androidx.annotation.StringRes
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.presentation.view.common.viewstate.ErrorItem

class MapErrorItem(
    @StringRes labelRes: Int,
    @StringRes messageRes: Int,
    @StringRes actionRes: Int?,
    @StringRes cancelRes: Int?
): ErrorItem(labelRes, messageRes, actionRes, cancelRes) {
    companion object {
        val ARTICLES_ERROR = MapErrorItem(
            R.string.loading_articles_error_label,
            R.string.loading_articles_error_message,
            RETRY_LABEL,
            CLOSE_LABEL
        )
        val LOCATION_ERROR = MapErrorItem(
            R.string.location_error_label,
            R.string.location_error_message,
            RETRY_LABEL,
            null
        )
        val LOCATION_SERVICES_ERROR =
            MapErrorItem(
                R.string.location_error_services_label,
                R.string.location_error_services_message,
                RETRY_LABEL,
                null
            )
    }
}