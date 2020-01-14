package com.iskorsukov.historyaround.presentation.view.common.fragment

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewdata.ViewData
import com.iskorsukov.historyaround.presentation.view.common.viewstate.ErrorItem
import com.iskorsukov.historyaround.presentation.view.common.viewstate.LoadingItem
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction

abstract class BaseLCEViewStateActionFragment<L: LoadingItem, C: ViewData, E: ErrorItem, VM: ViewModel, CB: ViewDataBinding>: BaseLCEViewStateFragment<L, C, E, VM, CB>() {
    abstract fun applyViewAction(viewAction: ViewAction<*>)
}