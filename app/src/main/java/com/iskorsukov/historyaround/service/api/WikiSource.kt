package com.iskorsukov.historyaround.service.api

import io.reactivex.Single
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.model.detail.ArticleDetails

interface WikiSource {
    fun loadArticleItems(languageCode: String?, latlng: Pair<Double, Double>, radius: Int): Single<List<ArticleItem>>
    fun loadArticleDetails(languageCode: String?, pageid: String): Single<ArticleDetails>
}