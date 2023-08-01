package com.iskorsukov.historyaround.presentation.view.permission

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.databinding.FragmentPermissionBinding
import com.iskorsukov.historyaround.presentation.view.common.error.ErrorDialog
import com.iskorsukov.historyaround.presentation.view.common.fragment.BaseTitledViewActionFragment
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.map.MapActivity
import com.iskorsukov.historyaround.presentation.view.permission.viewaction.NavigateToMapPermissionsAction
import com.iskorsukov.historyaround.presentation.view.permission.viewaction.RequestPermissionsAction
import com.iskorsukov.historyaround.presentation.view.permission.viewaction.ShowPermissionDeniedDialogAction
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.PermissionErrorItem
import com.iskorsukov.historyaround.presentation.viewmodel.permission.PermissionViewModel

class PermissionFragment : BaseTitledViewActionFragment() {

    private lateinit var viewModel: PermissionViewModel

    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>

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
        contentBinding.lifecycleOwner = this
        contentBinding.viewModel = viewModel
        return contentBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requestPermissionsLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions(),
                viewModel::onRequestPermissionsResult
            )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) viewModel.checkPermissions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewState()
    }

    private fun observeViewState() {
        viewModel.permissionActionLiveEvent.observe(viewLifecycleOwner, this::applyViewAction)
        viewModel.permissionErrorLiveEvent.observe(viewLifecycleOwner, this::handleError)
    }

    override fun applyViewAction(viewAction: ViewAction<*>) {
        when (viewAction) {
            is NavigateToMapPermissionsAction ->
                startActivity(Intent(requireContext(), MapActivity::class.java))
            is ShowPermissionDeniedDialogAction ->
                showGrantPermissionFromSettingsDialog()
            is RequestPermissionsAction ->
                viewAction.data?.let { requestPermissions(it) }
        }
    }

    private fun requestPermissions(permissions: List<String>) {
        requestPermissionsLauncher.launch(permissions.toTypedArray())
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

    fun handleError(errorItem: PermissionErrorItem) {
        val dialog = ErrorDialog.newInstance(errorItem)
        val listener = object : ErrorDialog.ErrorDialogListener {
            override fun onActionClick() {
                dialog.dismiss()
                viewModel.checkPermissions()
            }

            override fun onCancelClick() {
                dialog.dismiss()
            }
        }
        dialog.listener = listener
        dialog.show(childFragmentManager, ErrorDialog.TAG)
    }
}