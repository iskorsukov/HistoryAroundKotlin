package com.iskorsukov.historyaround.presentation.view.map.viewstate

import com.iskorsukov.historyaround.presentation.view.common.viewstate.LCEState
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.MapViewData
import com.iskorsukov.historyaround.presentation.view.common.viewstate.ViewState

class MapViewState(lceState: LCEState, loading: MapLoadingItem?, content: MapViewData?, error: MapErrorItem?):
    ViewState<MapLoadingItem, MapViewData, MapErrorItem>(lceState, loading, content, error) {
    constructor(content: MapViewData): this(LCEState.CONTENT, null, content, null)
    constructor(loading: MapLoadingItem): this(LCEState.LOADING, loading, null, null)
    constructor(error: MapErrorItem): this(LCEState.ERROR, null, null, error)
}