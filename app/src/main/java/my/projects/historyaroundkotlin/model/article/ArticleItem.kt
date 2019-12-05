package my.projects.historyaroundkotlin.model.article

import my.projects.historyaroundkotlin.model.common.ArticleThumbnail
import java.io.Serializable

data class ArticleItem(val pageid: String,
                       val title: String,
                       val description: String,
                       val latlng: Pair<Double, Double>,
                       val thumbnail: ArticleThumbnail?
): Serializable