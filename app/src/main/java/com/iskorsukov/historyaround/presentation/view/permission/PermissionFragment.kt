package com.iskorsukov.historyaround.presentation.view.permission

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.databinding.FragmentPermissionBinding
import com.iskorsukov.historyaround.mock.Mockable
import com.iskorsukov.historyaround.presentation.view.common.adapter.ItemListener
import com.iskorsukov.historyaround.presentation.view.common.fragment.BaseNavViewActionFragment
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.permission.adapter.PermissionsAdapter
import com.iskorsukov.historyaround.presentation.view.permission.viewaction.NavigateToMapAction
import com.iskorsukov.historyaround.presentation.view.permission.viewaction.ShowPermissionDeniedDialogAction
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.PermissionErrorItem
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.viewdata.PermissionsViewData
import com.iskorsukov.historyaround.presentation.view.util.viewModelFactory
import com.iskorsukov.historyaround.presentation.viewmodel.permission.PermissionViewModel

@Mockable
class PermissionFragment : BaseNavViewActionFragment(), ItemListener {

    private lateinit var viewModel: PermissionViewModel

    private lateinit var contentBinding: FragmentPermissionBinding

    override fun titleRes(): Int {
        return R.string.permissions_fragment_title
    }

    override fun inflateContent(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        contentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_permission, container, false)
        return contentBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this, viewModelFactory())[PermissionViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewState()
        if (savedInstanceState == null) viewModel.checkPermissions()
    }

    private fun observeViewState() {
        viewModel.permissionDataLiveData.observe(viewLifecycleOwner, this::showContent)
        viewModel.permissionErrorLiveData.observe(viewLifecycleOwner, this::handleError)
        viewModel.permissionActionLiveEvent.observe(viewLifecycleOwner, this::applyViewAction)
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
        val builder = AlertDialog.Builder(requireContext())
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

    private fun showContent(content: PermissionsViewData) {
        contentBinding.fragment = this
        contentBinding.permissionsRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        contentBinding.permissionsRecyclerView.adapter =
            PermissionsAdapter(
                content.rationaleList,
                this@PermissionFragment
            )
    }

    private fun handleError(error: PermissionErrorItem) {
        TODO("Show error dialog")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode != PermissionViewModel.PERMISSIONS_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        } else {
            viewModel.onRequestPermissionsResult(permissions, grantResults, this)
        }
    }
}