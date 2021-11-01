package com.iskorsukov.historyaround.presentation.view.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.iskorsukov.historyaround.HistoryAroundApp
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.databinding.FragmentBaseLceBinding
import com.iskorsukov.historyaround.service.navigation.NavControllerSource

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
        setTitle()
        return baseBinding.root
    }

    private fun inflateLCEParts(baseView: View, inflater: LayoutInflater) {
        baseView.apply {
            contentBinding = DataBindingUtil.inflate(inflater, contentLayout(), baseBinding.content, false)
            baseBinding.content.addView(contentBinding.root)
            errorBinding = DataBindingUtil.inflate(inflater, errorLayout(), baseBinding.error, false)
            baseBinding.error.addView(errorBinding.root)
            loadingBinding = DataBindingUtil.inflate(inflater, loadingLayout(), baseBinding.loading, false)
            baseBinding.loading.addView(loadingBinding.root)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavControllerSource()
        configureRetryButton()
    }

    private fun initNavControllerSource() {
        navControllerSource = (requireContext().applicationContext as HistoryAroundApp).appComponent.navControllerSource()
    }

    private fun setTitle() {
        if (titleRes() != 0) {
            activity?.findViewById<Toolbar>(R.id.toolbar)?.setTitle(titleRes())
        }
    }

    private fun configureRetryButton() {
        view?.findViewById<Button>(R.id.error_retry)?.setOnClickListener { onErrorRetry() }
    }
}