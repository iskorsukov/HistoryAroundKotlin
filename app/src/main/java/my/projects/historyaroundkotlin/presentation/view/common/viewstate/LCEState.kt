package my.projects.historyaroundkotlin.presentation.view.common.viewstate

import java.io.Serializable

enum class LCEState: Serializable {
    LOADING,
    ERROR,
    CONTENT
}