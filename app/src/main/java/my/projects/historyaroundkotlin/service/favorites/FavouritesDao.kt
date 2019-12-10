package my.projects.historyaroundkotlin.service.favorites

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import my.projects.historyaroundkotlin.data.entity.ArticleEntity
import my.projects.historyaroundkotlin.data.entity.ArticleThumbnailEntity

@Dao
abstract class FavouritesDao {
    @Query("select * from articles")
    abstract fun getArticles(): Single<List<ArticleEntity>>

    @Query("select * from thumbnails where thumbnailId = :thumbnailId")
    abstract fun getThumbnail(thumbnailId: Long): Maybe<ArticleThumbnailEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertThumbnail(thumbnail: ArticleThumbnailEntity): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertArticle(article: ArticleEntity): Completable

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticleWithThumbnail(article: ArticleEntity, thumbnail: ArticleThumbnailEntity): Completable {
        return insertThumbnail(thumbnail).flatMapCompletable {
            insertArticle(article.copy(thumbnailId = it))
        }
    }

    @Delete
    abstract fun deleteThumbnail(thumbnail: ArticleThumbnailEntity): Completable

    @Delete
    abstract fun deleteArticle(article: ArticleEntity): Completable

    @Transaction
    @Delete
    fun deleteArticleWithThumbnail(article: ArticleEntity): Completable {
        return deleteArticle(article)
    }

    @Query("select * from articles where pageid = :pageid")
    abstract fun getArticleByPageId(pageid: Long): Maybe<ArticleEntity>
}