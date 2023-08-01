package com.iskorsukov.historyaround.presentation.view.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.iskorsukov.historyaround.R

abstract class BaseTitledFragment: Fragment() {

    abstract fun inflateContent(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

    @StringRes
    open fun titleRes(): Int {
        return 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setTitle()
        return inflateContent(inflater, container, savedInstanceState)
    }

    private fun setTitle() {
        if (titleRes() != 0) {
            activity?.findViewById<Toolbar>(R.id.toolbar)?.setTitle(titleRes())
        }
    }
}