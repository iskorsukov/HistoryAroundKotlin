package my.projects.historyaroundkotlin.presentation.view.permission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.viewmodel.permission.PermissionViewModel

class PermissionFragment : Fragment() {

    private lateinit var viewModel: PermissionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(PermissionViewModel::class.java)
        } ?: throw IllegalStateException("Activity not found")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_permission, container, false)
    }
}