package my.projects.historyaroundkotlin.presentation.view.permission.viewstate

import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.viewdata.PermissionsViewData
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.ViewState

class PermissionViewState(lceState: LCEState, content: PermissionsViewData?, error: PermissionErrorItem?): ViewState<PermissionsViewData, PermissionErrorItem>(lceState, content, error)