package my.projects.historyaroundkotlin.presentation.view.detail.viewstate

import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.ViewState
import my.projects.historyaroundkotlin.presentation.view.detail.viewstate.viewdata.DetailViewData

class DetailViewState(lceState: LCEState, loading: DetailLoadingItem?, content: DetailViewData?, error: DetailErrorItem?): ViewState<DetailLoadingItem, DetailViewData, DetailErrorItem>(lceState, loading, content, error)