package com.iskorsukov.historyaround.presentation.view.detail.viewaction

import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction

data class ViewInBrowserAction(private val url: String): ViewAction<String>(url)