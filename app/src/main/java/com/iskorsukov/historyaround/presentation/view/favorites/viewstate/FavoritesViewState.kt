package com.iskorsukov.historyaround.presentation.view.favorites.viewstate

import com.iskorsukov.historyaround.presentation.view.common.viewstate.LCEState
import com.iskorsukov.historyaround.presentation.view.common.viewstate.ViewState
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.viewdata.FavoritesViewData

class FavoritesViewState(lceState: LCEState, loading: FavoritesLoadingItem?, content: FavoritesViewData?, error: FavoritesErrorItem?):
    ViewState<FavoritesLoadingItem, FavoritesViewData, FavoritesErrorItem>(lceState, loading, content, error)