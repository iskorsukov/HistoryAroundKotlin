package com.iskorsukov.historyaround.service.api

import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.model.detail.ArticleDetails

interface WikiSource {

    suspend fun loadArticleItems(languageCode: String?, latlng: Pair<Double, Double>, radius: Int): List<ArticleItem>

    suspend fun loadArticleDetails(languageCode: String?, pageid: String): ArticleDetails
}