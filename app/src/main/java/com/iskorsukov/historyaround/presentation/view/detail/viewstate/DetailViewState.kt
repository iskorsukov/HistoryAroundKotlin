package com.iskorsukov.historyaround.presentation.view.detail.viewstate

import com.iskorsukov.historyaround.presentation.view.common.viewstate.LCEState
import com.iskorsukov.historyaround.presentation.view.common.viewstate.ViewState
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.viewdata.DetailViewData

class DetailViewState(lceState: LCEState, loading: DetailLoadingItem?, content: DetailViewData?, error: DetailErrorItem?): ViewState<DetailLoadingItem, DetailViewData, DetailErrorItem>(lceState, loading, content, error)