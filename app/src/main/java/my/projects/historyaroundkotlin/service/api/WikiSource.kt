package my.projects.historyaroundkotlin.service.api

import io.reactivex.Single
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.model.detail.ArticleDetails

interface WikiSource {
    fun loadArticleItems(languageCode: String?, latlng: Pair<Double, Double>, radius: Int): Single<List<ArticleItem>>
    fun loadArticleDetails(languageCode: String?, pageid: String): Single<ArticleDetails>
}