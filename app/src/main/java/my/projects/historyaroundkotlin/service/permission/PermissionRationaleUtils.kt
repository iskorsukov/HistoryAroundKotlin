package my.projects.historyaroundkotlin.service.permission

import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.viewdata.PermissionRationale

class PermissionRationaleUtils {

    companion object {
        val LOCATION_RATIONALE =
            PermissionRationale(
                R.drawable.ic_location,
                R.string.location_permission_label,
                R.string.location_rationale
            )

        val INTERNET_RATIONALE =
            PermissionRationale(
                R.drawable.ic_internet,
                R.string.internet_permission_label,
                R.string.internet_rationale
            )

        val STORAGE_RATIONALE =
            PermissionRationale(
                R.drawable.ic_storage,
                R.string.storage_permission_label,
                R.string.storage_rationale
            )

        val NETWORK_STATE_RATIONALE =
            PermissionRationale(
                R.drawable.ic_network_state,
                R.string.network_state_permission_label,
                R.string.network_state_rationale
            )
    }
}