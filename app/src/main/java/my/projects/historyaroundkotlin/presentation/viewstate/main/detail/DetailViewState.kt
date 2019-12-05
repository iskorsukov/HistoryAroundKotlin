package my.projects.historyaroundkotlin.presentation.viewstate.main.detail

import my.projects.historyaroundkotlin.model.detail.ArticleDetails
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultState
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.ViewState

class DetailViewState(loadingResultState: LoadingResultState<DetailErrorStatus>, val articleDetails: ArticleDetails?): ViewState<DetailErrorStatus>(loadingResultState)