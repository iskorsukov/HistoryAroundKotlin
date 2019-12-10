package my.projects.historyaroundkotlin.service.favorites

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.SingleSource
import my.projects.historyaroundkotlin.data.entity.ArticleEntity
import my.projects.historyaroundkotlin.mapper.entity.ArticleEntitiesMapper
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.service.storage.AppDatabase
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
        val thumbnailEntity = articleItem.thumbnail?.let {
            mapper.mapThumbnailToEntity(it)
        }
        return thumbnailEntity?.let {
            articlesDao.insertArticleWithThumbnail(articleEntity, thumbnailEntity)
        } ?: articlesDao.insertArticle(articleEntity)
    }

    override fun getFavoriteArticles(): Single<List<ArticleItem>> {
        return articlesDao.getArticles()
            .toObservable()
            .flatMapIterable { item -> item }
            .flatMapSingle { articleEntity: ArticleEntity ->
                articleEntity.thumbnailId?.run {
                    articlesDao.getThumbnail(this).flatMapSingle {
                        Single.just(mapper.mapArticleEntity(articleEntity, it))
                    }
                } ?: Single.just(mapper.mapArticleEntity(articleEntity, null))
            }.toList()
    }

    override fun removeFromFavorites(articleItem: ArticleItem): Completable {
        return articleItem.thumbnail?.run {
            articlesDao.deleteArticleWithThumbnail(mapper.mapArticleToEntity(articleItem))
        } ?: articlesDao.deleteArticle(mapper.mapArticleToEntity(articleItem))
    }

    override fun isFavorite(pageid: String): Single<Boolean> {
        return articlesDao.getArticleByPageId(pageid.toLong()).flatMap {
            Maybe.just(true)
        }.toSingle(false)
    }
}