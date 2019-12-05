package my.projects.historyaroundkotlin.service.api

import io.reactivex.Single
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.model.detail.ArticleDetails

interface WikiSource {
    fun loadArticleItems(latlng: Pair<Double, Double>, radius: Int): Single<List<ArticleItem>>
    fun loadArticleDetails(pageid: String): Single<ArticleDetails>
}