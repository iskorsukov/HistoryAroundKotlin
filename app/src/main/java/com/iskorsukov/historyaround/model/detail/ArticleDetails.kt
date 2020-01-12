package com.iskorsukov.historyaround.model.detail

import com.iskorsukov.historyaround.mock.Mockable
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.model.common.ArticleThumbnail
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