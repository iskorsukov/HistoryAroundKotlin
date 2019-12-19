package my.projects.historyaroundkotlin.presentation.view.detail.viewstate

import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.ViewState
import my.projects.historyaroundkotlin.presentation.view.detail.viewstate.viewdata.DetailViewData

class DetailViewState(lceState: LCEState, content: DetailViewData?, error: DetailErrorItem?): ViewState<DetailViewData, DetailErrorItem>(lceState, content, error)