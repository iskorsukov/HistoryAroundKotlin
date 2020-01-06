package my.projects.historyaroundkotlin.model.detail

import my.projects.historyaroundkotlin.mock.Mockable
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.model.common.ArticleThumbnail
import java.io.Serializable

@Mockable
data class ArticleDetails(val pageid: Long,
                          val title: String,
                          val extract: String,
                          val thumbnail: ArticleThumbnail?,
                          val coordinates: Pair<Double, Double>,
                          val url: String,
                          val languageCode: String
): Serializable

fun ArticleDetails.toArticleItem(): ArticleItem {
    return ArticleItem(pageid.toString(), title, extract, coordinates, thumbnail, languageCode)
}