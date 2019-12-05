package my.projects.historyaroundkotlin.service.favourites

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import my.projects.historyaroundkotlin.data.entity.ArticleDetailsEntity
import my.projects.historyaroundkotlin.data.entity.ArticleEntity
import my.projects.historyaroundkotlin.data.entity.ArticleThumbnailEntity
import my.projects.historyaroundkotlin.model.article.ArticleItem

@Dao
abstract class FavouritesDao {
    @Query("select * from articles")
    abstract fun getArticles(): Observable<List<ArticleEntity>>

    @Query("select * from details where pageid = :pageid")
    abstract fun getDetails(pageid: Long): Maybe<ArticleDetailsEntity>

    @Query("select * from thumbnails where thumbnailId = :thumbnailId")
    abstract fun getThumbnail(thumbnailId: Long): Maybe<ArticleThumbnailEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertThumbnail(thumbnail: ArticleThumbnailEntity): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertArticle(article: ArticleEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertDetails(details: ArticleDetailsEntity): Completable

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticleAndDetailsWithThumbnail(article: ArticleEntity, details: ArticleDetailsEntity, thumbnail: ArticleThumbnailEntity): Completable {
        return insertThumbnail(thumbnail).flatMapCompletable {
            insertArticle(article.copy(thumbnailId = it))
            insertDetails(details.copy(thumbnailId = it))
        }
    }

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticleAndDetails(article: ArticleEntity, details: ArticleDetailsEntity): Completable {
        return insertArticle(article).andThen(insertDetails(details))
    }
}