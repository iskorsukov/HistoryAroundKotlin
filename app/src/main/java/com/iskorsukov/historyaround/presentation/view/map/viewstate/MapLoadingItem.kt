package com.iskorsukov.historyaround.presentation.view.map.viewstate

import androidx.annotation.StringRes
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.presentation.view.common.viewstate.LoadingItem

class MapLoadingItem(@StringRes messageRes: Int): LoadingItem(messageRes) {
    companion object {
        val LOADING_LOCATION = MapLoadingItem(R.string.loading_location_label)
        val LOADING_ARTICLES = MapLoadingItem(R.string.loading_articles_label)
    }
}