package my.projects.historyaroundkotlin.presentation.view.start.splash.content

import android.os.Bundle
import android.view.View
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.view.common.BaseFragment
import java.util.concurrent.TimeUnit

class SplashFragment : BaseFragment() {

    override fun fragmentLayout(): Int {
        return R.layout.fragment_splash
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController().navigate(SplashFragmentDirections.actionSplashFragmentToPermissionsFlow())
    }
}