package my.projects.historyaroundkotlin.presentation.view.common.viewstate

import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewdata.ViewData
import java.io.Serializable

open class ViewState<L: LoadingItem, C: ViewData, E: ErrorItem>(val lceState: LCEState, val loading: L?, val content: C?, val error: E?): Serializable {
    companion object {
        fun <L: LoadingItem, C: ViewData, E: ErrorItem> content(content: C): ViewState<L, C, E> {
            return ViewState(
                LCEState.CONTENT,
                null,
                content,
                null
            )
        }

        fun <L: LoadingItem, C: ViewData, E: ErrorItem> loading(loading: L): ViewState<L, C, E> {
            return ViewState(
                LCEState.LOADING,
                loading,
                null,
                null
            )
        }

        fun <L: LoadingItem, C: ViewData, E: ErrorItem> error(error: E): ViewState<L, C, E> {
            return ViewState(
                LCEState.ERROR,
                null,
                null,
                error
            )
        }
    }
}