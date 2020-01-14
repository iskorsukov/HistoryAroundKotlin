package com.iskorsukov.historyaround.presentation.view.map.viewaction

import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction

class NavigateToDetailsAction(item: ArticleItem): ViewAction<ArticleItem>(item)