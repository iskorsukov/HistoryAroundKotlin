package my.projects.historyaroundkotlin.service.permission

import io.reactivex.Single

interface PermissionSource {
    fun allPermissionsGranted(): Single<Boolean>
    fun getNotGrantedPermissions(): Single<List<String>>
}