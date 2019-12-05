package my.projects.historyaroundkotlin.model.detail

import my.projects.historyaroundkotlin.mock.Mockable
import my.projects.historyaroundkotlin.model.common.ArticleThumbnail
import java.io.Serializable

@Mockable
data class ArticleDetails(val pageid: Long,
                          val title: String,
                          val extract: String,
                          val thumbnail: ArticleThumbnail?,
                          val coordinates: Pair<Double, Double>,
                          val url: String
): Serializable