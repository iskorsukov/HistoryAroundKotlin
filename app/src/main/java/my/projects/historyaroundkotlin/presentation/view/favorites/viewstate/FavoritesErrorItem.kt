package my.projects.historyaroundkotlin.presentation.view.favorites.viewstate

import androidx.annotation.StringRes
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.ErrorItem

class FavoritesErrorItem(@StringRes labelRes: Int, @StringRes messageRes: Int): ErrorItem(labelRes, messageRes) {

    companion object {
        val ERROR = FavoritesErrorItem(
            R.string.loading_favorites_error_label,
            R.string.loading_favorites_error_message
        )
    }
}