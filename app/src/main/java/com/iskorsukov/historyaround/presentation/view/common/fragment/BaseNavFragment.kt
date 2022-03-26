package com.iskorsukov.historyaround.presentation.view.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.iskorsukov.historyaround.HistoryAroundApp
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.service.navigation.NavControllerSource

abstract class BaseNavFragment: Fragment() {

    private lateinit var navControllerSource: NavControllerSource

    protected fun navController(): NavController {
        return navControllerSource.navController(this)
    }

    abstract fun inflateContent(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

    @StringRes
    open fun titleRes(): Int {
        return 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setTitle()
        return inflateContent(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavControllerSource()
    }

    private fun initNavControllerSource() {
        navControllerSource = (requireContext().applicationContext as HistoryAroundApp).appComponent.navControllerSource()
    }

    private fun setTitle() {
        if (titleRes() != 0) {
            activity?.findViewById<Toolbar>(R.id.toolbar)?.setTitle(titleRes())
        }
    }
}