package my.projects.historyaroundkotlin.presentation.view.favorites.viewstate

import androidx.annotation.StringRes
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LoadingItem

class FavoritesLoadingItem(@StringRes messageRes: Int): LoadingItem(messageRes) {
    companion object {
        val LOADING_FAVORITES = FavoritesLoadingItem(R.string.loading_articles_label)
    }
}