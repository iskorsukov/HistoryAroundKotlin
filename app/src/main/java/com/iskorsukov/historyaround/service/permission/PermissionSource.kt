package com.iskorsukov.historyaround.service.permission

import androidx.fragment.app.Fragment
import io.reactivex.Single
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.viewdata.PermissionRationale

interface PermissionSource {
    fun allPermissionsGranted(): Single<Boolean>
    fun getNotGrantedPermissions(): Single<List<String>>
    fun requestPermissions(permissions: List<String>, fragment: Fragment, requestCode: Int)
    fun shouldShowRequestPermissionRationale(permission: String, fragment: Fragment): Boolean

    fun mapPermissionToRationale(permission: String): PermissionRationale?
}