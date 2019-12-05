package my.projects.historyaroundkotlin.presentation.view.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import my.projects.historyaroundkotlin.HistoryAroundApp
import my.projects.historyaroundkotlin.injection.navigation.NavControllerSource

abstract class BaseFragment: Fragment() {

    private lateinit var navControllerSource: NavControllerSource

    protected fun navController(): NavController {
        return navControllerSource.navController(this)
    }

    @LayoutRes
    abstract fun fragmentLayout(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(fragmentLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavControllerSource()
    }

    private fun initNavControllerSource() {
        navControllerSource = (context!!.applicationContext as HistoryAroundApp).appComponent.navControllerSource()
    }
}