package my.projects.historyaroundkotlin.presentation.viewmodel.permission

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import my.projects.historyaroundkotlin.mock.Mockable
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewaction.ViewAction
import my.projects.historyaroundkotlin.presentation.view.permission.viewaction.NavigateToMapAction
import my.projects.historyaroundkotlin.presentation.view.permission.viewaction.ShowPermissionDeniedDialogAction
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.PermissionErrorItem
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.PermissionViewState
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.viewdata.PermissionsViewData
import my.projects.historyaroundkotlin.service.permission.PermissionSource
import javax.inject.Inject

@Mockable
class PermissionViewModel @Inject constructor(private val permissionSource: PermissionSource) : ViewModel() {

    companion object {
        const val PERMISSIONS_REQUEST_CODE = 1007
    }

    private var permissionsCheckDisposable: Disposable? = null

    val viewStateLiveData: LiveData<PermissionViewState> by lazy {
        MutableLiveData<PermissionViewState>().also {
            it.value = PermissionViewState(LCEState.LOADING, null, null)
            checkPermissions()
        }
    }

    val viewActionLiveEvent: LiveEvent<ViewAction<*>> = LiveEvent()

    fun checkPermissions() {
        permissionsCheckDisposable?.dispose()

        permissionsCheckDisposable = permissionSource.getNotGrantedPermissions()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ permissions: List<String> ->
                val rationaleList = permissions.mapNotNull { permissionSource.mapPermissionToRationale(it) }
                if (rationaleList.isEmpty()) {
                    viewActionLiveEvent.value =
                        NavigateToMapAction()
                } else {
                    (viewStateLiveData as MutableLiveData).value =
                        PermissionViewState(
                            LCEState.CONTENT,
                            PermissionsViewData(
                                rationaleList
                            ), null
                        )
                }
            }, { throwable ->
                throwable.printStackTrace()
                (viewStateLiveData as MutableLiveData).value =
                    PermissionViewState(
                        LCEState.ERROR,
                        null,
                        PermissionErrorItem.PERMISSIONS_ERROR
                    )
            })
    }

    fun requestPermissions(fragment: Fragment) {
        permissionsCheckDisposable?.dispose()

        permissionsCheckDisposable = permissionSource.getNotGrantedPermissions().subscribe({ permissions: List<String> ->
            permissionSource.requestPermissions(permissions, fragment, PERMISSIONS_REQUEST_CODE)
        }, { throwable ->
            throwable.printStackTrace()
            (viewStateLiveData as MutableLiveData).value =
                PermissionViewState(
                    LCEState.ERROR,
                    null,
                    PermissionErrorItem.PERMISSIONS_ERROR
                )
        })
    }

    fun onRequestPermissionsResult(permissions: Array<out String>, grantResults: IntArray, fragment: Fragment) {
        var permissionDenied = false
        for (i in grantResults.indices) {
            permissionDenied = grantResults[i] == PackageManager.PERMISSION_DENIED && !permissionSource.shouldShowRequestPermissionRationale(permissions[i], fragment)
            if (permissionDenied) break
        }
        if (permissionDenied) {
            viewActionLiveEvent.value = ShowPermissionDeniedDialogAction()
        }
    }

    override fun onCleared() {
        permissionsCheckDisposable?.dispose()
        super.onCleared()
    }
}