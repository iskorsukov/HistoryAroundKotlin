package my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles

import my.projects.historyaroundkotlin.mock.Mockable
import my.projects.historyaroundkotlin.model.article.ArticleItem
import java.io.Serializable

@Mockable
data class ArticleItemViewData(val item: ArticleItem, val markedFavorite: Boolean): Serializable