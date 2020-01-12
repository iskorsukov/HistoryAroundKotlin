package com.iskorsukov.historyaround.presentation.view.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario

abstract class BaseNavControllerFragmentTest<F: Fragment>: BaseMockNavControllerTest() {
    protected lateinit var scenario: FragmentScenario<F>

    open fun launchFragment(fragmentClass: Class<F>, args: Bundle? = null) {
        scenario = FragmentScenario.launchInContainer(fragmentClass, args, androidx.appcompat.R.style.Base_Theme_AppCompat, null)
    }
}