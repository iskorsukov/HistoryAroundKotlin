package com.iskorsukov.historyaround.presentation.view.favorites.viewstate

import androidx.annotation.StringRes
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.presentation.view.common.viewstate.LoadingItem

class FavoritesLoadingItem(@StringRes messageRes: Int): LoadingItem(messageRes) {
    companion object {
        val LOADING_FAVORITES = FavoritesLoadingItem(R.string.loading_articles_label)
    }
}