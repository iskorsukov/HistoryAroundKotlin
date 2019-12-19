package my.projects.historyaroundkotlin.presentation.view.common.viewstate

import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewdata.ViewData
import java.io.Serializable

open class ViewState<C: ViewData, E: ErrorItem>(val lceState: LCEState, val content: C?, val error: E?): Serializable {
    companion object {
        fun <C: ViewData, E: ErrorItem> content(content: C): ViewState<C, E> {
            return ViewState(
                LCEState.CONTENT,
                content,
                null
            )
        }

        fun <C: ViewData, E: ErrorItem> loading(): ViewState<C, E> {
            return ViewState(
                LCEState.LOADING,
                null,
                null
            )
        }

        fun <C: ViewData, E: ErrorItem> error(error: E): ViewState<C, E> {
            return ViewState(
                LCEState.ERROR,
                null,
                error
            )
        }
    }
}