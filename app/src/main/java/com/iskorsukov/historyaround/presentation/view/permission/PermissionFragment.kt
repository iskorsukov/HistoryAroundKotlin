package com.iskorsukov.historyaround.presentation.view.permission

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_permission.*
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.databinding.FragmentPermissionBinding
import com.iskorsukov.historyaround.mock.Mockable
import com.iskorsukov.historyaround.presentation.view.common.adapter.ItemListener
import com.iskorsukov.historyaround.presentation.view.common.fragment.BaseLCEViewStateActionFragment
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.permission.adapter.PermissionsAdapter
import com.iskorsukov.historyaround.presentation.view.permission.viewaction.NavigateToMapAction
import com.iskorsukov.historyaround.presentation.view.permission.viewaction.ShowPermissionDeniedDialogAction
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.PermissionErrorItem
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.PermissionLoadingItem
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.viewdata.PermissionsViewData
import com.iskorsukov.historyaround.presentation.viewmodel.permission.PermissionViewModel

@Mockable
class PermissionFragment : BaseLCEViewStateActionFragment<PermissionLoadingItem, PermissionsViewData, PermissionErrorItem, PermissionViewModel, FragmentPermissionBinding>(), ItemListener {

    override fun viewModelClass(): Class<PermissionViewModel> {
        return PermissionViewModel::class.java
    }

    override fun titleRes(): Int {
        return R.string.permissions_fragment_title
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
        permissionsRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
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

    override fun onErrorRetry() {
        viewModel.onRetry()
    }
}