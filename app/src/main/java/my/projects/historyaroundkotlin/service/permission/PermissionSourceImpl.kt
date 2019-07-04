package my.projects.historyaroundkotlin.service.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import io.reactivex.Single
import java.security.Permission
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionSourceImpl @Inject constructor(
    private val context: Context,
    private val permissions: Array<String>
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
}