package com.iskorsukov.historyaround.presentation.view.favorites.viewstate

import com.iskorsukov.historyaround.presentation.view.common.viewstate.LCEState
import com.iskorsukov.historyaround.presentation.view.common.viewstate.ViewState
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.viewdata.FavoritesViewData

class FavoritesViewState(lceState: LCEState, loading: FavoritesLoadingItem?, content: FavoritesViewData?, error: FavoritesErrorItem?):
    ViewState<FavoritesLoadingItem, FavoritesViewData, FavoritesErrorItem>(lceState, loading, content, error) {
    constructor(content: FavoritesViewData): this(LCEState.CONTENT, null, content, null)
    constructor(loading: FavoritesLoadingItem): this(LCEState.LOADING, loading, null, null)
    constructor(error: FavoritesErrorItem): this(LCEState.ERROR, null, null, error)
}