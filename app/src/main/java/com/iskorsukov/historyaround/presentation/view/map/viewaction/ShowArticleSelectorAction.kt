package com.iskorsukov.historyaround.presentation.view.map.viewaction

import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticleItemViewData

data class ShowArticleSelectorAction(private val articles: List<ArticleItemViewData>):
    ViewAction<List<ArticleItemViewData>>(articles)