package my.projects.historyaroundkotlin.presentation.view.start.permission.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.argument.RationaleListArgument
import my.projects.historyaroundkotlin.presentation.view.common.BaseLoadingFragment
import my.projects.historyaroundkotlin.presentation.viewmodel.start.permission.PermissionFlowViewModel
import my.projects.historyaroundkotlin.presentation.view.util.viewModelFactory
import my.projects.historyaroundkotlin.presentation.viewstate.start.permission.PermissionErrorStatus
import my.projects.historyaroundkotlin.presentation.viewstate.start.permission.PermissionViewState

class LoadingPermissionsFragment: BaseLoadingFragment<PermissionErrorStatus, PermissionViewState>() {

    private lateinit var flowViewModel: PermissionFlowViewModel

    override fun fragmentLayout(): Int {
        return R.layout.fragment_loading_permissions
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_loading_permissions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        checkPermissions()
    }

    private fun initViewModel() {
        flowViewModel = ViewModelProviders.of(this, viewModelFactory())[PermissionFlowViewModel::class.java]
    }

    private fun checkPermissions() {
        flowViewModel.checkPermissions().observe(this, Observer {
            applyViewState(it)
        })
    }

    override fun applyErrorState(errors: List<PermissionErrorStatus>) {
        navController().navigate(LoadingPermissionsFragmentDirections.actionLoadingPermissionsFragmentToErrorPermissionsFragment())
    }

    override fun applyContentState(viewState: PermissionViewState) {
        if (viewState.rationaleList!!.isEmpty()) {
            navController().navigate(LoadingPermissionsFragmentDirections.actionLoadingPermissionsFragmentToMainFlow())
        } else {
            navController().navigate(
                LoadingPermissionsFragmentDirections.actionLoadingPermissionsFragmentToPermissionFragment(
                    RationaleListArgument(ArrayList(viewState.rationaleList.toMutableList()))
                )
            )
        }
    }
}