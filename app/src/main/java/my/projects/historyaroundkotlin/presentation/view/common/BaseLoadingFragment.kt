package my.projects.historyaroundkotlin.presentation.view.common

import androidx.fragment.app.Fragment
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.ErrorStatus
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultStatus
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.ViewState

abstract class BaseLoadingFragment<E: ErrorStatus, VS: ViewState<E>>: BaseFragment() {

    open fun applyViewState(viewState: VS) {
        when(viewState.lceState.status) {
            LoadingResultStatus.ERROR -> applyErrorState(viewState.lceState.errors)
            LoadingResultStatus.CONTENT -> applyContentState(viewState)
        }
    }

    protected abstract fun applyErrorState(errors: List<E>)
    protected abstract fun applyContentState(viewState: VS)
}