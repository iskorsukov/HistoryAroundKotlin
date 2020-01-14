package com.iskorsukov.historyaround.service.favorites

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import com.iskorsukov.historyaround.mapper.entity.ArticleEntitiesMapper
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.service.storage.AppDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesSourceImpl @Inject constructor(
    database: AppDatabase,
    private val mapper: ArticleEntitiesMapper
): FavoritesSource {
    private val articlesDao: FavouritesDao = database.getFavouritesDao()

    override fun addToFavorites(articleItem: ArticleItem): Completable {
        val articleEntity = mapper.mapArticleToEntity(articleItem)
        return articlesDao.insertArticle(articleEntity)
    }

    override fun getFavoriteArticles(): Observable<List<ArticleItem>> {
        return articlesDao.getArticles().map {
            it.map {entity -> mapper.mapArticleEntity(entity) }
        }
    }

    override fun removeFromFavorites(articleItem: ArticleItem): Completable {
        return articleItem.thumbnail?.run {
            articlesDao.deleteArticle(mapper.mapArticleToEntity(articleItem))
        } ?: articlesDao.deleteArticle(mapper.mapArticleToEntity(articleItem))
    }

    override fun isFavorite(pageid: String): Single<Boolean> {
        return articlesDao.getArticleByPageId(pageid.toLong()).flatMap {
            Maybe.just(true)
        }.toSingle(false)
    }
}