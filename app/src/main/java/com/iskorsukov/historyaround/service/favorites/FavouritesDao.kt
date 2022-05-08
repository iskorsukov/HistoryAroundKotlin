package com.iskorsukov.historyaround.service.favorites

import androidx.room.*
import com.iskorsukov.historyaround.data.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FavouritesDao {
    @Query("select * from articles")
    abstract fun getArticles(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertArticle(article: ArticleEntity)

    @Delete
    abstract suspend fun deleteArticle(article: ArticleEntity)

    @Query("select * from articles where pageid = :pageid")
    abstract suspend fun getArticleByPageId(pageid: Long): ArticleEntity?
}