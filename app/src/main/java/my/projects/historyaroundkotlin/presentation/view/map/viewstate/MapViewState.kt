package my.projects.historyaroundkotlin.presentation.view.map.viewstate

import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata.MapViewData
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.ViewState

class MapViewState(lceState: LCEState, content: MapViewData?, error: MapErrorItem?): ViewState<MapViewData, MapErrorItem>(lceState, content, error)