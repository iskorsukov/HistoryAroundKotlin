package my.projects.historyaroundkotlin.presentation.viewstate.main.map.location

import android.location.Location
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultState
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.ViewState

class LocationViewState(loadingResultState: LoadingResultState<LocationErrorStatus>, val location: Location?): ViewState<LocationErrorStatus>(loadingResultState)