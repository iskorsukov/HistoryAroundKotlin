package my.projects.historyaroundkotlin.presentation.view.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_base_error.*
import kotlinx.android.synthetic.main.fragment_base_lce.view.*
import my.projects.historyaroundkotlin.HistoryAroundApp
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.databinding.FragmentBaseLceBinding
import my.projects.historyaroundkotlin.service.navigation.NavControllerSource

abstract class BaseLCEFragment<LB: ViewDataBinding, CB: ViewDataBinding, EB: ViewDataBinding>: Fragment() {

    private lateinit var navControllerSource: NavControllerSource

    protected lateinit var baseBinding: FragmentBaseLceBinding
    protected lateinit var loadingBinding: LB
    protected lateinit var contentBinding: CB
    protected lateinit var errorBinding: EB

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

    @StringRes
    open fun titleRes(): Int {
        return 0
    }

    abstract fun onErrorRetry()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        baseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_lce, container, false)
        inflateLCEParts(baseBinding.root, inflater)
        return baseBinding.root
    }

    private fun inflateLCEParts(baseView: View, inflater: LayoutInflater) {
        baseView.apply {
            contentBinding = DataBindingUtil.inflate(inflater, contentLayout(), content, false)
            content.addView(contentBinding.root)
            errorBinding = DataBindingUtil.inflate(inflater, errorLayout(), error, false)
            error.addView(errorBinding.root)
            loadingBinding = DataBindingUtil.inflate(inflater, loadingLayout(), loading, false)
            loading.addView(loadingBinding.root)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavControllerSource()
        setTitle()
        configureRetryButton()
    }

    private fun initNavControllerSource() {
        navControllerSource = (context!!.applicationContext as HistoryAroundApp).appComponent.navControllerSource()
    }

    private fun setTitle() {
        if (titleRes() != 0) {
            activity?.toolbar?.setTitle(titleRes())
        }
    }

    private fun configureRetryButton() {
        error_retry?.setOnClickListener { onErrorRetry() }
    }
}