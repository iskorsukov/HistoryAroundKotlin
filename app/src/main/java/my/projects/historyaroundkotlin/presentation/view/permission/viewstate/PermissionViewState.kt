package my.projects.historyaroundkotlin.presentation.view.permission.viewstate

import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.viewdata.PermissionsViewData
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.ViewState

class PermissionViewState(lceState: LCEState, loading: PermissionLoadingItem?, content: PermissionsViewData?, error: PermissionErrorItem?):
    ViewState<PermissionLoadingItem, PermissionsViewData, PermissionErrorItem>(lceState, loading, content, error)