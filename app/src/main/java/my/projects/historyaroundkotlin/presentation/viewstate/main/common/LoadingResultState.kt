package my.projects.historyaroundkotlin.presentation.viewstate.main.common

open class LoadingResultState<E: ErrorStatus>(val status: LoadingResultStatus, val errors: List<E>) {
    companion object {
        fun <E: ErrorStatus> content(): LoadingResultState<E> {
            return LoadingResultState(
                LoadingResultStatus.CONTENT,
                emptyList()
            )
        }

        fun <E: ErrorStatus> error(errors: List<E>): LoadingResultState<E> {
            return LoadingResultState(
                LoadingResultStatus.ERROR,
                errors
            )
        }
    }
}