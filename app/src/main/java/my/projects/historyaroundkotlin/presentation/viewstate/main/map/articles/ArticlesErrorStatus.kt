package my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles

import my.projects.historyaroundkotlin.presentation.viewstate.main.common.ErrorStatus

enum class ArticlesErrorStatus: ErrorStatus {
    CONNECTION_ERROR,
    PARSING_ERROR
}