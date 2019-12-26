package my.projects.historyaroundkotlin.service.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.reactivex.Single
import my.projects.historyaroundkotlin.injection.PermissionsList
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.viewdata.PermissionRationale
import javax.inject.Inject

class PermissionSourceImpl @Inject constructor(
    private val context: Context,
    @PermissionsList private val permissions: List<String>,
    private val permissionMapper: PermissionRationaleMapper
): PermissionSource {

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
        return permissionMapper.mapToRationale(permission)
    }

    override fun shouldShowRequestPermissionRationale(permission: String, fragment: Fragment): Boolean {
        return fragment.shouldShowRequestPermissionRationale(permission)
    }
}