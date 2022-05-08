package com.iskorsukov.historyaround.presentation.view.favorites.viewaction

import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction

data class NavigateToDetailsAction(private val item: ArticleItem): ViewAction<ArticleItem>(item)