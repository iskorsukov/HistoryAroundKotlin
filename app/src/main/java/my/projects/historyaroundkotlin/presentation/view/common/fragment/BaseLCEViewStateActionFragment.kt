package my.projects.historyaroundkotlin.presentation.view.common.fragment

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import my.projects.historyaroundkotlin.databinding.FragmentBaseLoadingBinding
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewdata.ViewData
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.ErrorItem
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LoadingItem
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewaction.ViewAction

abstract class BaseLCEViewStateActionFragment<L: LoadingItem, C: ViewData, E: ErrorItem, VM: ViewModel, CB: ViewDataBinding>: BaseLCEViewStateFragment<L, C, E, VM, CB>() {
    abstract fun applyViewAction(viewAction: ViewAction<*>)
}