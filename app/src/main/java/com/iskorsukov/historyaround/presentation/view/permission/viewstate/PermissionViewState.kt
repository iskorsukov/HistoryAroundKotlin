package com.iskorsukov.historyaround.presentation.view.permission.viewstate

import com.iskorsukov.historyaround.presentation.view.common.viewstate.LCEState
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.viewdata.PermissionsViewData
import com.iskorsukov.historyaround.presentation.view.common.viewstate.ViewState

class PermissionViewState(lceState: LCEState, loading: PermissionLoadingItem?, content: PermissionsViewData?, error: PermissionErrorItem?):
    ViewState<PermissionLoadingItem, PermissionsViewData, PermissionErrorItem>(lceState, loading, content, error)