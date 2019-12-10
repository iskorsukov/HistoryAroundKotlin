package my.projects.historyaroundkotlin.presentation.viewstate.main.favorites

import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultState
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.ViewState

class FavoritesViewState(lceState: LoadingResultState<FavoritesErrorStatus>, val favoriteItems: List<ArticleItem>?): ViewState<FavoritesErrorStatus>(lceState)