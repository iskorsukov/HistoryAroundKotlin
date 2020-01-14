package com.iskorsukov.historyaround.presentation.view.detail.viewstate

import com.iskorsukov.historyaround.presentation.view.common.viewstate.LCEState
import com.iskorsukov.historyaround.presentation.view.common.viewstate.ViewState
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.viewdata.DetailViewData

class DetailViewState(lceState: LCEState, loading: DetailLoadingItem?, content: DetailViewData?, error: DetailErrorItem?)
    : ViewState<DetailLoadingItem, DetailViewData, DetailErrorItem>(lceState, loading, content, error) {
    constructor(content: DetailViewData): this(LCEState.CONTENT, null, content, null)
    constructor(loading: DetailLoadingItem): this(LCEState.LOADING, loading, null, null)
    constructor(error: DetailErrorItem): this(LCEState.ERROR, null, null, error)
}