package com.iskorsukov.historyaround.service.favorites

import com.iskorsukov.historyaround.mapper.entity.ArticleEntitiesMapper
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.service.storage.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

class FavoritesSourceImpl @Inject constructor(
    database: AppDatabase,
    private val mapper: ArticleEntitiesMapper
): FavoritesSource {
    private val articlesDao: FavouritesDao = database.getFavouritesDao()

    override suspend fun addToFavorites(articleItem: ArticleItem) {
        val articleEntity = mapper.mapArticleToEntity(articleItem)
        articlesDao.insertArticle(articleEntity)
    }

    override suspend fun getFavoriteArticles(): Flow<List<ArticleItem>> {
        return articlesDao.getArticles().map { mapper.mapArticleEntityList(it) }
    }

    override suspend fun removeFromFavorites(articleItem: ArticleItem) {
        articleItem.thumbnail?.run {
            articlesDao.deleteArticle(mapper.mapArticleToEntity(articleItem))
        } ?: articlesDao.deleteArticle(mapper.mapArticleToEntity(articleItem))
    }

    override suspend fun isFavorite(pageid: String): Boolean {
        val article = articlesDao.getArticleByPageId(pageid.toLong())
        return article != null
    }
}