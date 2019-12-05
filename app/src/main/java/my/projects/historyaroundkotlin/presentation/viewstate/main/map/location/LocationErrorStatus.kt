package my.projects.historyaroundkotlin.presentation.viewstate.main.map.location

import my.projects.historyaroundkotlin.presentation.viewstate.main.common.ErrorStatus

enum class LocationErrorStatus: ErrorStatus {
    LOCATION_UNAVAILABLE,
    LOCATION_SERVICE_UNAVAILABLE,
    UNKNOWN
}