package com.iskorsukov.historyaround.presentation.viewmodel.splash

import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.iskorsukov.historyaround.mock.Mockable
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.permission.viewaction.NavigateToMapPermissionsAction
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.PermissionErrorItem
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.viewdata.PermissionsViewData
import com.iskorsukov.historyaround.presentation.view.splash.viewaction.NavigateToMapSplashAction
import com.iskorsukov.historyaround.presentation.view.splash.viewaction.NavigateToPermissionsAction
import com.iskorsukov.historyaround.service.permission.PermissionSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Mockable
class SplashViewModel@Inject constructor(
    private val permissionSource: PermissionSource
) : ViewModel() {

    private var permissionsCheckDisposable: Disposable? = null

    val splashActionLiveEvent: LiveEvent<ViewAction<*>> = LiveEvent()

    fun checkPermissions() {
        permissionsCheckDisposable?.dispose()

        permissionsCheckDisposable = permissionSource.getNotGrantedPermissions()
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { permissions: List<String> ->
                if (permissions.isEmpty()) {
                    splashActionLiveEvent.value = NavigateToMapSplashAction()
                } else {
                    splashActionLiveEvent.value = NavigateToPermissionsAction()
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        permissionsCheckDisposable?.dispose()
    }
}