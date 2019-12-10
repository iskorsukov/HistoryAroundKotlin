package my.projects.historyaroundkotlin.presentation.viewstate.main.detail

import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultState
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.ViewState

class DetailViewState(loadingResultState: LoadingResultState<DetailErrorStatus>, val viewData: ArticleDetailsViewData?): ViewState<DetailErrorStatus>(loadingResultState)