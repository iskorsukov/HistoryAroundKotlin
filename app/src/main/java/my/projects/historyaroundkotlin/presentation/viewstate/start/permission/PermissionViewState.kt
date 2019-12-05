package my.projects.historyaroundkotlin.presentation.viewstate.start.permission

import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultState
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.ViewState

class PermissionViewState(loadingResultState: LoadingResultState<PermissionErrorStatus>, val rationaleList: List<PermissionRationale>?): ViewState<PermissionErrorStatus>(loadingResultState)