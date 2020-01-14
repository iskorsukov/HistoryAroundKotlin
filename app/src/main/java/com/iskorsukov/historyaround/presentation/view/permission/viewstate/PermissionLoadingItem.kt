package com.iskorsukov.historyaround.presentation.view.permission.viewstate

import androidx.annotation.StringRes
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.presentation.view.common.viewstate.LoadingItem

class PermissionLoadingItem(@StringRes messageRes: Int): LoadingItem(messageRes) {
    companion object {
        val PERMISSION_LOADING = PermissionLoadingItem(R.string.loading_permissions_label)
    }
}