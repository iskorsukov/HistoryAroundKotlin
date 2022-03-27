package com.iskorsukov.historyaround.presentation.view.splash

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.presentation.view.common.fragment.BaseNavViewActionFragment
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.splash.viewaction.NavigateToMapSplashAction
import com.iskorsukov.historyaround.presentation.view.splash.viewaction.NavigateToPermissionsAction
import com.iskorsukov.historyaround.presentation.view.util.viewModelFactory
import com.iskorsukov.historyaround.presentation.viewmodel.permission.PermissionViewModel
import com.iskorsukov.historyaround.presentation.viewmodel.splash.SplashViewModel
import java.util.concurrent.TimeUnit

class SplashFragment : BaseNavViewActionFragment() {

    private lateinit var viewModel: SplashViewModel

    override fun inflateContent(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this, viewModelFactory())[SplashViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) viewModel.checkPermissions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.splashActionLiveEvent.observe(viewLifecycleOwner, this::applyViewAction)
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    override fun applyViewAction(viewAction: ViewAction<*>) {
        when (viewAction) {
            is NavigateToMapSplashAction ->
                navController().navigate(
                    SplashFragmentDirections.actionSplashFragmentToMapFragment()
                )
            is NavigateToPermissionsAction ->
                navController().navigate(
                    SplashFragmentDirections.actionSplashFragmentToPermissionFragment()
                )
        }
    }
}