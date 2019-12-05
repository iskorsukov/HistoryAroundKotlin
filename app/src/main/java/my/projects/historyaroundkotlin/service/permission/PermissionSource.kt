package my.projects.historyaroundkotlin.service.permission

import androidx.fragment.app.Fragment
import io.reactivex.Single
import my.projects.historyaroundkotlin.presentation.viewstate.start.permission.PermissionRationale

interface PermissionSource {
    fun allPermissionsGranted(): Single<Boolean>
    fun getNotGrantedPermissions(): Single<List<String>>
    fun requestPermissions(permissions: List<String>, fragment: Fragment, requestCode: Int)

    fun mapPermissionToRationale(permission: String): PermissionRationale?
}