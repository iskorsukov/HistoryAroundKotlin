package my.projects.historyaroundkotlin.presentation.viewstate.start.permission

import my.projects.historyaroundkotlin.presentation.viewstate.main.common.ErrorStatus

enum class PermissionErrorStatus:
    ErrorStatus {
    PERMISSIONS_CHECK_ERROR,
    PERMISSIONS_REQUEST_ERROR
}