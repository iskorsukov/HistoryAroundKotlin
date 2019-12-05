package my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles

import my.projects.historyaroundkotlin.mock.Mockable
import my.projects.historyaroundkotlin.model.article.ArticleItem

@Mockable
data class ArticleItemViewData(val item: ArticleItem, val markedFavorite: Boolean)