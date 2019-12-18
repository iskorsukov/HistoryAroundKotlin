package my.projects.historyaroundkotlin.presentation.view.common.fragment

import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewdata.ViewData
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.ErrorItem
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewaction.ViewAction

abstract class BaseLCEViewStateActionFragment<C: ViewData, E: ErrorItem>: BaseLCEViewStateFragment<C, E>() {
    abstract fun applyViewAction(viewAction: ViewAction<*>)
}