package my.projects.historyaroundkotlin.presentation.view.permission

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_permission.*
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.databinding.FragmentPermissionBinding
import my.projects.historyaroundkotlin.mock.Mockable
import my.projects.historyaroundkotlin.presentation.view.common.adapter.ItemListener
import my.projects.historyaroundkotlin.presentation.view.common.fragment.BaseLCEViewStateActionFragment
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewaction.ViewAction
import my.projects.historyaroundkotlin.presentation.view.permission.adapter.PermissionsAdapter
import my.projects.historyaroundkotlin.presentation.view.permission.viewaction.NavigateToMapAction
import my.projects.historyaroundkotlin.presentation.view.permission.viewaction.ShowPermissionDeniedDialogAction
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.PermissionErrorItem
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.PermissionLoadingItem
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.viewdata.PermissionsViewData
import my.projects.historyaroundkotlin.presentation.viewmodel.permission.PermissionViewModel

@Mockable
class PermissionFragment : BaseLCEViewStateActionFragment<PermissionLoadingItem, PermissionsViewData, PermissionErrorItem, PermissionViewModel, FragmentPermissionBinding>(), ItemListener {

    override fun viewModelClass(): Class<PermissionViewModel> {
        return PermissionViewModel::class.java
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        observeViewState()
    }

    private fun observeViewState() {
        viewModel.viewStateLiveData.observe(viewLifecycleOwner, Observer {
            applyViewState(it)
        })
        viewModel.viewActionLiveEvent.observe(viewLifecycleOwner, Observer {
            applyViewAction(it)
        })
    }

    fun requestMissingPermissions() {
        viewModel.requestPermissions(this)
    }

    override fun applyViewAction(viewAction: ViewAction<*>) {
        when (viewAction) {
            is NavigateToMapAction ->
                navController().navigate(PermissionFragmentDirections.actionPermissionFragmentToMapFragment())
            is ShowPermissionDeniedDialogAction ->
                showGrantPermissionFromSettingsDialog()
        }
    }

    private fun showGrantPermissionFromSettingsDialog() {
        val builder = AlertDialog.Builder(context!!)
        builder.apply {
            setTitle(R.string.permissions_denied_title)
            setMessage(R.string.permissions_denied_message)
            setPositiveButton(R.string.settings) { dialog, _ ->
                startActivity(Intent(Settings.ACTION_SETTINGS))
                dialog.dismiss()
            }
            setNegativeButton(R.string.ignore) { dialog, _ ->
                dialog.dismiss()
            }
        }
        builder.create().show()
    }

    override fun showContent(content: PermissionsViewData) {
        contentBinding.fragment = this
        permissionsRecyclerView.adapter =
            PermissionsAdapter(
                content.rationaleList,
                this@PermissionFragment
            )
    }

    override fun contentLayout(): Int {
        return R.layout.fragment_permission
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode != PermissionViewModel.PERMISSIONS_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        } else {
            viewModel.onRequestPermissionsResult(permissions, grantResults, this)
        }
    }
}