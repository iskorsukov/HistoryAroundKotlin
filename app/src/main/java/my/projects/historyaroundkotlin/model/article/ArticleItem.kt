package my.projects.historyaroundkotlin.model.article

import my.projects.historyaroundkotlin.model.common.ArticleThumbnail

data class ArticleItem(val pageid: Long,
                       val title: String,
                       val description: String,
                       val latlng: Pair<Double, Double>,
                       val thumbnail: ArticleThumbnail?
)