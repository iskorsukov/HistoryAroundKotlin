package my.projects.historyaroundkotlin.presentation.view.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import kotlinx.android.synthetic.main.fragment_base_lce.view.*
import my.projects.historyaroundkotlin.HistoryAroundApp
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.service.navigation.NavControllerSource

abstract class BaseLCEFragment: Fragment() {

    private lateinit var navControllerSource: NavControllerSource

    protected fun navController(): NavController {
        return navControllerSource.navController(this)
    }

    @LayoutRes
    abstract fun contentLayout(): Int

    @LayoutRes
    open fun loadingLayout(): Int {
        return R.layout.fragment_base_loading
    }

    @LayoutRes
    open fun errorLayout(): Int {
        return R.layout.fragment_base_error
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val baseView = inflater.inflate(R.layout.fragment_base_lce, container, false)
        inflateLCEParts(baseView, inflater)
        return baseView
    }

    private fun inflateLCEParts(baseView: View, inflater: LayoutInflater) {
        baseView.apply {
            content.addView(inflater.inflate(contentLayout(), content, false))
            error.addView(inflater.inflate(errorLayout(), error, false))
            loading.addView(inflater.inflate(loadingLayout(), loading, false))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavControllerSource()
    }

    private fun initNavControllerSource() {
        navControllerSource = (context!!.applicationContext as HistoryAroundApp).appComponent.navControllerSource()
    }
}