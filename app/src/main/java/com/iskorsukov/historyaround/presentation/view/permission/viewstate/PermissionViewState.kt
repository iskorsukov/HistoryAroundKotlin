package com.iskorsukov.historyaround.presentation.view.permission.viewstate

import com.iskorsukov.historyaround.presentation.view.common.viewstate.LCEState
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.viewdata.PermissionsViewData
import com.iskorsukov.historyaround.presentation.view.common.viewstate.ViewState

class PermissionViewState(lceState: LCEState, loading: PermissionLoadingItem?, content: PermissionsViewData?, error: PermissionErrorItem?):
    ViewState<PermissionLoadingItem, PermissionsViewData, PermissionErrorItem>(lceState, loading, content, error) {
    constructor(content: PermissionsViewData): this(LCEState.CONTENT, null, content, null)
    constructor(loading: PermissionLoadingItem): this(LCEState.LOADING, loading, null, null)
    constructor(error: PermissionErrorItem): this(LCEState.ERROR, null, null, error)
}