package my.projects.historyaroundkotlin.service.permission

import android.Manifest
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.viewdata.PermissionRationale
import javax.inject.Inject

class PermissionRationaleMapper @Inject constructor() {

    private val permissionRationaleMap = HashMap<String, PermissionRationale>()

    init {
        permissionRationaleMap[Manifest.permission.ACCESS_FINE_LOCATION] = PermissionRationaleUtils.LOCATION_RATIONALE
        permissionRationaleMap[Manifest.permission.INTERNET] = PermissionRationaleUtils.INTERNET_RATIONALE
        permissionRationaleMap[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PermissionRationaleUtils.STORAGE_RATIONALE
        permissionRationaleMap[Manifest.permission.ACCESS_NETWORK_STATE] = PermissionRationaleUtils.NETWORK_STATE_RATIONALE
    }

    fun mapToRationale(permission: String): PermissionRationale? {
        return permissionRationaleMap[permission]
    }
}