package com.iskorsukov.historyaround.presentation.view.common.fragment

import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction

abstract class BaseTitledViewActionFragment: BaseTitledFragment() {
    abstract fun applyViewAction(viewAction: ViewAction<*>)
}