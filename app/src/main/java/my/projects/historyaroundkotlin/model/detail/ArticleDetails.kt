package my.projects.historyaroundkotlin.model.detail

import my.projects.historyaroundkotlin.model.common.ArticleThumbnail

data class ArticleDetails(val pageid: Long,
                          val title: String,
                          val extract: String,
                          val thumbnail: ArticleThumbnail?,
                          val url: String)