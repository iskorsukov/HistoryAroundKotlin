package my.projects.historyaroundkotlin.presentation.view.start.permission.error

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_error_permissions.*
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.view.common.BaseFragment

class ErrorPermissionsFragment: BaseFragment() {

    override fun fragmentLayout(): Int {
        return R.layout.fragment_error_permissions
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetryButton()
    }

    private fun initRetryButton() {
        retry.setOnClickListener {
            navController().popBackStack()
        }
    }
}