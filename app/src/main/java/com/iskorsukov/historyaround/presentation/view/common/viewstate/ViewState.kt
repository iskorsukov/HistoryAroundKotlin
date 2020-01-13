package com.iskorsukov.historyaround.presentation.view.common.viewstate

import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewdata.ViewData
import java.io.Serializable

open class ViewState<L: LoadingItem, C: ViewData, E: ErrorItem>(
    val lceState: LCEState,
    val loading: L?,
    val content: C?,
    val error: E?): Serializable {
}
