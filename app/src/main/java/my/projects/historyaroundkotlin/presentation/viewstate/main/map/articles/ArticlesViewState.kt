package my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles

import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultState
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.ViewState

class ArticlesViewState(loadingResultState: LoadingResultState<ArticlesErrorStatus>, val items: List<ArticleItemViewData>?): ViewState<ArticlesErrorStatus>(loadingResultState)