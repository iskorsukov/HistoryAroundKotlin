package com.iskorsukov.historyaround.presentation.viewmodel.permission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.iskorsukov.historyaround.presentation.view.common.adapter.ItemListener
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.permission.viewaction.NavigateToMapPermissionsAction
import com.iskorsukov.historyaround.presentation.view.permission.viewaction.RequestPermissionsAction
import com.iskorsukov.historyaround.presentation.view.permission.viewaction.ShowPermissionDeniedDialogAction
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.PermissionErrorItem
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.viewdata.PermissionsViewData
import com.iskorsukov.historyaround.service.permission.PermissionSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PermissionViewModel @Inject constructor(
    private val permissionSource: PermissionSource
    ) : ViewModel(), ItemListener {

    companion object {
        const val PERMISSIONS_REQUEST_CODE = 1007
    }

    private var permissionsCheckDisposable: Disposable? = null

    private val _permissionDataLiveData = MutableLiveData<PermissionsViewData>()
    val permissionDataLiveData: LiveData<PermissionsViewData>
        get() = _permissionDataLiveData

    private val _permissionIsLoadingLiveData = MutableLiveData(true)
    val permissionIsLoadingLiveData: LiveData<Boolean>
        get() = _permissionIsLoadingLiveData

    val permissionErrorLiveEvent: LiveEvent<PermissionErrorItem> = LiveEvent()

    val permissionActionLiveEvent: LiveEvent<ViewAction<*>> = LiveEvent()

    fun checkPermissions() {
        _permissionIsLoadingLiveData.value = true

        permissionsCheckDisposable?.dispose()

        permissionsCheckDisposable = permissionSource.getNotGrantedPermissions()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ permissions: List<String> ->
                val rationaleList = permissions.mapNotNull { permissionSource.mapPermissionToRationale(it) }
                if (rationaleList.isEmpty()) {
                    permissionActionLiveEvent.value =NavigateToMapPermissionsAction()
                } else {
                    _permissionDataLiveData.value = PermissionsViewData(rationaleList)
                    _permissionIsLoadingLiveData.value = false
                }
            }, { throwable ->
                throwable.printStackTrace()
                permissionErrorLiveEvent.value = PermissionErrorItem()
            })
    }

    fun requestPermissions() {
        permissionsCheckDisposable?.dispose()

        permissionsCheckDisposable = permissionSource.getNotGrantedPermissions().subscribe({ permissions: List<String> ->
            permissionActionLiveEvent.value = RequestPermissionsAction(permissions)
        }, { throwable ->
            throwable.printStackTrace()
            permissionErrorLiveEvent.value = PermissionErrorItem()
        })
    }

    fun onRequestPermissionsResult(permissionsRequestResult: Map<String, Boolean>) {
        var permissionDenied = false
        for (permission in permissionsRequestResult.keys) {
            permissionDenied = permissionsRequestResult[permission] == false
            if (permissionDenied) break
        }
        if (permissionDenied) {
            permissionActionLiveEvent.value = ShowPermissionDeniedDialogAction()
        }
        checkPermissions()
    }

    override fun onCleared() {
        permissionsCheckDisposable?.dispose()
        super.onCleared()
    }
}