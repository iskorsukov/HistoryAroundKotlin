package com.iskorsukov.historyaround.service.favorites

import com.iskorsukov.historyaround.model.article.ArticleItem
import kotlinx.coroutines.flow.Flow

interface FavoritesSource {
    suspend fun getFavoriteArticles(): Flow<List<ArticleItem>>
    suspend fun addToFavorites(articleItem: ArticleItem)
    suspend fun removeFromFavorites(articleItem: ArticleItem)
    suspend fun isFavorite(pageid: String): Boolean
}