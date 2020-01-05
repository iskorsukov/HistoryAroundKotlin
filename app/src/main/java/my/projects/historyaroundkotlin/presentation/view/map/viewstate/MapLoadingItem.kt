package my.projects.historyaroundkotlin.presentation.view.map.viewstate

import androidx.annotation.StringRes
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LoadingItem

class MapLoadingItem(@StringRes messageRes: Int): LoadingItem(messageRes) {
    companion object {
        val LOADING_LOCATION = MapLoadingItem(R.string.loading_location_label)
        val LOADING_ARTICLES = MapLoadingItem(R.string.loading_articles_label)
    }
}