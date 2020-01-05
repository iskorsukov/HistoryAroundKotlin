package my.projects.historyaroundkotlin.presentation.view.favorites.viewstate

import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.ViewState
import my.projects.historyaroundkotlin.presentation.view.favorites.viewstate.viewdata.FavoritesViewData

class FavoritesViewState(lceState: LCEState, loading: FavoritesLoadingItem?, content: FavoritesViewData?, error: FavoritesErrorItem?):
    ViewState<FavoritesLoadingItem, FavoritesViewData, FavoritesErrorItem>(lceState, loading, content, error)