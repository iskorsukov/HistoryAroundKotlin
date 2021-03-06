package com.iskorsukov.historyaround.presentation.view.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.ViewModel
import com.iskorsukov.historyaround.R

abstract class BaseViewModelFragmentTest<F: Fragment, VM: ViewModel>: BaseMockViewModelTest<VM>() {
    protected lateinit var scenario: FragmentScenario<F>

    open fun launchFragment(fragmentClass: Class<F>, args: Bundle? = null) {
        scenario = FragmentScenario.launchInContainer(fragmentClass, args, R.style.AppTheme, null)
    }
}