package my.projects.historyaroundkotlin.presentation.view.permission.viewstate

import androidx.annotation.StringRes
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LoadingItem

class PermissionLoadingItem(@StringRes messageRes: Int): LoadingItem(messageRes) {
    companion object {
        val PERMISSION_LOADING = PermissionLoadingItem(R.string.loading_permissions_label)
    }
}