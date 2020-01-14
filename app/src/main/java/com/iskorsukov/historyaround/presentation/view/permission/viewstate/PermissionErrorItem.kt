package com.iskorsukov.historyaround.presentation.view.permission.viewstate

import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.presentation.view.common.viewstate.ErrorItem

class PermissionErrorItem(label: Int, message: Int): ErrorItem(label, message) {
    companion object {
        val PERMISSIONS_ERROR =
            PermissionErrorItem(
                R.string.loading_permissions_error_label,
                R.string.loading_permissions_error_message
            )
    }
}