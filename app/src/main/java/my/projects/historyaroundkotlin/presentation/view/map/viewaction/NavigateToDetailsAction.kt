package my.projects.historyaroundkotlin.presentation.view.map.viewaction

import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewaction.ViewAction

class NavigateToDetailsAction(item: ArticleItem): ViewAction<ArticleItem>(item)