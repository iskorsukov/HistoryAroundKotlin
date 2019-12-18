package my.projects.historyaroundkotlin.presentation.view.permission.viewstate

import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.ErrorItem

class PermissionErrorItem(label: Int, message: Int): ErrorItem(label, message) {
    companion object {
        val PERMISSIONS_ERROR =
            PermissionErrorItem(
                R.string.loading_permissions_error_label,
                R.string.loading_permissions_error_message
            )
    }
}