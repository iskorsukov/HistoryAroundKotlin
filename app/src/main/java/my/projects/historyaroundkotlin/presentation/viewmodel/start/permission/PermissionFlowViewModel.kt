package my.projects.historyaroundkotlin.presentation.viewmodel.start.permission

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import my.projects.historyaroundkotlin.mock.Mockable
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultState
import my.projects.historyaroundkotlin.presentation.viewstate.start.permission.PermissionErrorStatus
import my.projects.historyaroundkotlin.presentation.viewstate.start.permission.PermissionRationale
import my.projects.historyaroundkotlin.presentation.viewstate.start.permission.PermissionViewState
import my.projects.historyaroundkotlin.service.permission.PermissionSource
import javax.inject.Inject

@Mockable
class PermissionFlowViewModel @Inject constructor(private val permissionSource: PermissionSource) : ViewModel() {

    companion object {
        const val PERMISSIONS_REQUEST_CODE = 1007
    }

    private var permissionsCheckDisposable: Disposable? = null

    fun checkPermissions(): LiveData<PermissionViewState> {
        permissionsCheckDisposable?.dispose()

        val liveData = MutableLiveData<PermissionViewState>()
        permissionsCheckDisposable = permissionSource.getNotGrantedPermissions()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ permissions: List<String> ->
                val rationaleList = ArrayList<PermissionRationale>()
                permissions.forEach {
                    permissionSource.mapPermissionToRationale(it)?.apply {
                        rationaleList.add(this)
                    }
                }
                liveData.value = PermissionViewState(LoadingResultState.content(), rationaleList)
            }, { throwable ->
                throwable.printStackTrace()
                liveData.value = PermissionViewState(LoadingResultState.error(listOf(PermissionErrorStatus.PERMISSIONS_CHECK_ERROR)), null)
            })

        return liveData
    }

    fun requestPermissions(fragment: Fragment) {
        permissionsCheckDisposable?.dispose()
        permissionsCheckDisposable = permissionSource.getNotGrantedPermissions().subscribe({ permissions: List<String> ->
            permissionSource.requestPermissions(permissions, fragment, PERMISSIONS_REQUEST_CODE)
        }, { throwable ->
            throwable.printStackTrace()
        })
    }

    override fun onCleared() {
        permissionsCheckDisposable?.dispose()
        super.onCleared()
    }
}