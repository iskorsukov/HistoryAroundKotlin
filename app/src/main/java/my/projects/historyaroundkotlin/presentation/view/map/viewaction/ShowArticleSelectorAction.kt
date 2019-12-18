package my.projects.historyaroundkotlin.presentation.view.map.viewaction

import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewaction.ViewAction
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata.ArticleItemViewData

class ShowArticleSelectorAction(articles: List<ArticleItemViewData>): ViewAction<List<ArticleItemViewData>>(articles)