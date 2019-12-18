package my.projects.historyaroundkotlin.presentation.view.common.fragment

import android.view.View
import kotlinx.android.synthetic.main.fragment_base_error.*
import kotlinx.android.synthetic.main.fragment_base_lce.*
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewdata.ViewData
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.ErrorItem
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.ViewState

abstract class BaseLCEViewStateFragment<C: ViewData, E: ErrorItem>: BaseLCEFragment() {

    open fun applyViewState(viewState: ViewState<C, E>) {
        applyLCEVisibility(viewState.lceState)
        when(viewState.lceState) {
            LCEState.LOADING -> applyLoadingState(viewState)
            LCEState.CONTENT -> applyContentState(viewState)
            LCEState.ERROR -> applyErrorState(viewState)
        }
    }

    private fun applyLCEVisibility(lceState: LCEState) {
        loading.visibility = if (lceState == LCEState.LOADING) View.VISIBLE else View.GONE
        content.visibility = if (lceState == LCEState.CONTENT) View.VISIBLE else View.GONE
        error.visibility = if (lceState == LCEState.ERROR) View.VISIBLE else View.GONE
    }

    open fun applyLoadingState(viewState: ViewState<C, E>) {

    }

    open fun applyContentState(viewState: ViewState<C, E>) {
        showContent(viewState.content!!)
    }

    open fun applyErrorState(viewState: ViewState<C, E>) {
        viewState.error?.apply {
            error_label.setText(labelRes)
            error_text.setText(messageRes)
        }
    }

    abstract fun showContent(content: C)
}