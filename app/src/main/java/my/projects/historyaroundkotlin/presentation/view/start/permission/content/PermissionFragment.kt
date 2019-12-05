package my.projects.historyaroundkotlin.presentation.view.start.permission.content

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_permission.*
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.mock.Mockable
import my.projects.historyaroundkotlin.presentation.common.adapter.ItemListener
import my.projects.historyaroundkotlin.presentation.view.common.BaseFragment
import my.projects.historyaroundkotlin.presentation.view.start.permission.content.adapter.PermissionsAdapter
import my.projects.historyaroundkotlin.presentation.view.util.observeSingle
import my.projects.historyaroundkotlin.presentation.viewmodel.start.permission.PermissionFlowViewModel
import my.projects.historyaroundkotlin.presentation.view.util.viewModelFactory
import my.projects.historyaroundkotlin.presentation.viewstate.start.permission.PermissionRationale
import my.projects.historyaroundkotlin.presentation.viewstate.start.permission.PermissionViewState

@Mockable
class PermissionFragment : BaseFragment(), ItemListener {

    private lateinit var flowViewModel: PermissionFlowViewModel
    private val args: PermissionFragmentArgs by navArgs()

    override fun fragmentLayout(): Int {
        return R.layout.fragment_permission
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        configureRecyclerView()
        initGrantPermissionsButton()
        showRationale(args.rationaleListArgument.rationaleList)
    }

    private fun initViewModel() {
        flowViewModel = ViewModelProviders.of(this, viewModelFactory())[PermissionFlowViewModel::class.java]
    }

    private fun configureRecyclerView() {
        permissionsRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun initGrantPermissionsButton() {
        grantPermissionsButton.setOnClickListener { requestMissingPermissions() }
    }

    private fun requestMissingPermissions() {
        flowViewModel.requestPermissions(this)
    }

    private fun showRationale(rationaleList: List<PermissionRationale>?) {
        rationaleList?.apply {
            if (rationaleList.isEmpty()) {
                navController().navigate(PermissionFragmentDirections.actionPermissionFragmentToMainFlow())
            } else {
                permissionsRecyclerView.adapter = PermissionsAdapter(rationaleList, this@PermissionFragment)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode != PermissionFlowViewModel.PERMISSIONS_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        } else {
            var permissionDenied = false
            for (i in grantResults.indices) {
                permissionDenied = grantResults[i] == PackageManager.PERMISSION_DENIED && !shouldShowRequestPermissionRationale(permissions[i])
                if (permissionDenied) break
            }
            if (permissionDenied) {
                showGrantPermissionFromSettingsDialog()
            }
            flowViewModel.checkPermissions().observeSingle(this) {
                showRationale(it.rationaleList)
            }
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