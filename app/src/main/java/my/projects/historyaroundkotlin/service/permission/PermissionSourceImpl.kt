package my.projects.historyaroundkotlin.service.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.reactivex.Single
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.injection.PermissionsList
import my.projects.historyaroundkotlin.presentation.viewstate.start.permission.PermissionRationale
import javax.inject.Inject

class PermissionSourceImpl @Inject constructor(
    private val context: Context,
    @PermissionsList private val permissions: List<String>
): PermissionSource {

    private val permissionRationaleMap = HashMap<String, PermissionRationale>()

    init {
        permissionRationaleMap[Manifest.permission.ACCESS_FINE_LOCATION] = PermissionRationaleUtils.LOCATION_RATIONALE
        permissionRationaleMap[Manifest.permission.INTERNET] = PermissionRationaleUtils.INTERNET_RATIONALE
        permissionRationaleMap[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PermissionRationaleUtils.STORAGE_RATIONALE
        permissionRationaleMap[Manifest.permission.ACCESS_NETWORK_STATE] = PermissionRationaleUtils.NETWORK_STATE_RATIONALE
    }

    override fun allPermissionsGranted(): Single<Boolean> {
        return Single.fromCallable {
            permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }
        }
    }

    override fun getNotGrantedPermissions(): Single<List<String>> {
        return Single.fromCallable {
            permissions.filter { ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED }
        }
    }

    override fun requestPermissions(permissions: List<String>, fragment: Fragment, requestCode: Int) {
        fragment.requestPermissions(permissions.toTypedArray(), requestCode)
    }

    override fun mapPermissionToRationale(permission: String): PermissionRationale? {
        return permissionRationaleMap[permission]
    }
}