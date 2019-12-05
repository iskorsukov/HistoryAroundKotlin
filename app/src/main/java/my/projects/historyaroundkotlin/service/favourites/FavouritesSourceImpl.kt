package my.projects.historyaroundkotlin.service.favourites

import io.reactivex.*
import io.reactivex.rxkotlin.flatMapIterable
import my.projects.historyaroundkotlin.data.entity.ArticleDetailsEntity
import my.projects.historyaroundkotlin.data.entity.ArticleEntity
import my.projects.historyaroundkotlin.mapper.entity.ArticleEntitiesMapper
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.model.detail.ArticleDetails
import my.projects.historyaroundkotlin.service.storage.AppDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouritesSourceImpl @Inject constructor(
    database: AppDatabase,
    private val mapper: ArticleEntitiesMapper
): FavouritesSource {
    private val articlesDao: FavouritesDao = database.getFavouritesDao()

    override fun addToFavourites(articleItem: ArticleItem, articleDetails: ArticleDetails): Completable {
        val articleEntity = mapper.mapArticleToEntity(articleItem)
        val detailsEntity = mapper.mapDetailsToEntity(articleDetails)
        val thumbnail = articleItem.thumbnail ?: articleDetails.thumbnail
        val thumbnailEntity = thumbnail?.let {
            mapper.mapThumbnailToEntity(thumbnail)
        }
        return if (thumbnailEntity == null) {
            articlesDao.insertArticleAndDetails(articleEntity, detailsEntity)
        } else {
            articlesDao.insertArticleAndDetailsWithThumbnail(articleEntity, detailsEntity, thumbnailEntity)
        }
    }

    override fun getFavouriteArticles(): Single<List<ArticleItem>> {
        return articlesDao.getArticles()
            .flatMapIterable()
            .flatMap { articleEntity: ArticleEntity ->
                articleEntity.thumbnailId?.run {
                    Observable.create<ArticleItem> { emitter: ObservableEmitter<ArticleItem> ->
                        articlesDao.getThumbnail(this).subscribe(
                            { thumbnailEntity -> emitter.onNext(mapper.mapArticleEntity(articleEntity, thumbnailEntity)) },
                            { emitter.onError(it) },
                            { emitter.onNext(mapper.mapArticleEntity(articleEntity, null)) }
                        )
                    }
                } ?: Observable.just(mapper.mapArticleEntity(articleEntity, null))
            }.toList()
    }

    override fun getFavouriteArticleDetails(pageid: Long): Maybe<ArticleDetails> {
        return articlesDao.getDetails(pageid)
            .flatMapObservable {detailsEntity: ArticleDetailsEntity ->
                detailsEntity.thumbnailId?.run {
                    Observable.create<ArticleDetails> { emitter: ObservableEmitter<ArticleDetails> ->
                        articlesDao.getThumbnail(this).subscribe(
                            { thumbnailEntity -> emitter.onNext(mapper.mapArticleDetailsEntity(detailsEntity, thumbnailEntity)) },
                            { emitter.onError(it) },
                            { emitter.onNext(mapper.mapArticleDetailsEntity(detailsEntity, null)) }
                        )
                    }
                } ?: Observable.just(mapper.mapArticleDetailsEntity(detailsEntity, null))
            }.firstElement()
    }
}