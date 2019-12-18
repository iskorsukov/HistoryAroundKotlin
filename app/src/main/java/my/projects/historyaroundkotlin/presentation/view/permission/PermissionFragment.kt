package my.projects.historyaroundkotlin.presentation.view.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_permission.*
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.mock.Mockable
import my.projects.historyaroundkotlin.presentation.common.adapter.ItemListener
import my.projects.historyaroundkotlin.presentation.view.common.fragment.BaseLCEViewStateActionFragment
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewaction.ViewAction
import my.projects.historyaroundkotlin.presentation.view.permission.adapter.PermissionsAdapter
import my.projects.historyaroundkotlin.presentation.view.permission.viewaction.NavigateToMapAction
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.PermissionErrorItem
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.viewdata.PermissionsViewData
import my.projects.historyaroundkotlin.presentation.view.util.viewModelFactory
import my.projects.historyaroundkotlin.presentation.viewmodel.permission.PermissionViewModel

@Mockable
class PermissionFragment : BaseLCEViewStateActionFragment<PermissionsViewData, PermissionErrorItem>(), ItemListener {

    private lateinit var viewModel: PermissionViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
        initGrantPermissionsButton()

        initViewModel()
        observeViewState()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory())[PermissionViewModel::class.java]
    }

    private fun configureRecyclerView() {
        permissionsRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun initGrantPermissionsButton() {
        grantPermissionsButton.setOnClickListener { requestMissingPermissions() }
    }

    private fun observeViewState() {
        viewModel.viewStateLiveData.observe(viewLifecycleOwner, Observer {
            applyViewState(it)
        })
        viewModel.viewActionLiveEvent.observe(viewLifecycleOwner, Observer {
            applyViewAction(it)
        })
    }

    private fun requestMissingPermissions() {
        viewModel.requestPermissions(this)
    }

    override fun applyViewAction(viewAction: ViewAction<*>) {
        when (viewAction) {
            is NavigateToMapAction ->
                navController().navigate(PermissionFragmentDirections.actionPermissionFragmentToMapFragment())
        }
    }

    override fun showContent(content: PermissionsViewData) {
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
            // TODO push request result to viewmodel
            var permissionDenied = false
            for (i in grantResults.indices) {
                permissionDenied = grantResults[i] == PackageManager.PERMISSION_DENIED && !shouldShowRequestPermissionRationale(permissions[i])
                if (permissionDenied) break
            }
            if (permissionDenied) {
                showGrantPermissionFromSettingsDialog()
            }
            //viewModel.checkPermissions()
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
}