package my.projects.historyaroundkotlin.service.favorites

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import my.projects.historyaroundkotlin.data.entity.ArticleEntity
import my.projects.historyaroundkotlin.data.entity.ArticleThumbnailEntity

@Dao
abstract class FavouritesDao {
    @Query("select * from articles")
    abstract fun getArticles(): Observable<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertArticle(article: ArticleEntity): Completable

    @Delete
    abstract fun deleteArticle(article: ArticleEntity): Completable

    @Query("select * from articles where pageid = :pageid")
    abstract fun getArticleByPageId(pageid: Long): Maybe<ArticleEntity>
}