package com.iskorsukov.historyaround.presentation.viewmodel.permission

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.iskorsukov.historyaround.mock.Mockable
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ShowLoadingAction
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.permission.viewaction.NavigateToMapAction
import com.iskorsukov.historyaround.presentation.view.permission.viewaction.ShowPermissionDeniedDialogAction
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.PermissionErrorItem
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.viewdata.PermissionsViewData
import com.iskorsukov.historyaround.service.permission.PermissionSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@Mockable
class PermissionViewModel @Inject constructor(private val permissionSource: PermissionSource) : ViewModel() {

    companion object {
        const val PERMISSIONS_REQUEST_CODE = 1007
    }

    private var permissionsCheckDisposable: Disposable? = null

    private val _permissionDataLiveData = MutableLiveData<PermissionsViewData>()
    val permissionDataLiveData: LiveData<PermissionsViewData>
        get() = _permissionDataLiveData

    private val _permissionErrorLiveData = MutableLiveData<PermissionErrorItem>()
    val permissionErrorLiveData: LiveData<PermissionErrorItem>
        get() = _permissionErrorLiveData

    val permissionActionLiveEvent: LiveEvent<ViewAction<*>> = LiveEvent()

    fun checkPermissions() {
        permissionActionLiveEvent.value = ShowLoadingAction()

        permissionsCheckDisposable?.dispose()

        permissionsCheckDisposable = permissionSource.getNotGrantedPermissions()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ permissions: List<String> ->
                val rationaleList = permissions.mapNotNull { permissionSource.mapPermissionToRationale(it) }
                if (rationaleList.isEmpty()) {
                    permissionActionLiveEvent.value = NavigateToMapAction()
                } else {
                    _permissionDataLiveData.value = PermissionsViewData(rationaleList)
                }
            }, { throwable ->
                throwable.printStackTrace()
                _permissionErrorLiveData.value = PermissionErrorItem.PERMISSIONS_ERROR
            })
    }

    fun requestPermissions(fragment: Fragment) {
        permissionsCheckDisposable?.dispose()

        permissionsCheckDisposable = permissionSource.getNotGrantedPermissions().subscribe({ permissions: List<String> ->
            permissionSource.requestPermissions(permissions, fragment, PERMISSIONS_REQUEST_CODE)
        }, { throwable ->
            throwable.printStackTrace()
            _permissionErrorLiveData.value = PermissionErrorItem.PERMISSIONS_ERROR
        })
    }

    fun onRequestPermissionsResult(permissions: Array<out String>, grantResults: IntArray, fragment: Fragment) {
        var permissionDenied = false
        for (i in grantResults.indices) {
            permissionDenied = grantResults[i] == PackageManager.PERMISSION_DENIED && !permissionSource.shouldShowRequestPermissionRationale(permissions[i], fragment)
            if (permissionDenied) break
        }
        if (permissionDenied) {
            permissionActionLiveEvent.value = ShowPermissionDeniedDialogAction()
        }
        checkPermissions()
    }

    fun onRetry() {
        checkPermissions()
    }

    override fun onCleared() {
        permissionsCheckDisposable?.dispose()
        super.onCleared()
    }
}