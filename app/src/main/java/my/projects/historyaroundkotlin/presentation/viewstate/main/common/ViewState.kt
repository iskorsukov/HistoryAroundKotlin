package my.projects.historyaroundkotlin.presentation.viewstate.main.common

abstract class ViewState<E: ErrorStatus>(val lceState: LoadingResultState<E>)