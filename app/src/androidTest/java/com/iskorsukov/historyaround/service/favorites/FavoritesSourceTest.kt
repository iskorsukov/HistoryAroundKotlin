package com.iskorsukov.historyaround.service.favorites

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.iskorsukov.historyaround.data.entity.ArticleEntity
import com.iskorsukov.historyaround.service.storage.AppDatabase
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class FavoritesSourceTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var favoritesDao: FavouritesDao
    private lateinit var db: AppDatabase

    @Before
    fun setupDatabase() {
        val appContent = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(appContent, AppDatabase::class.java).build()
        favoritesDao = db.getFavouritesDao()
    }

    @After
    fun closeDb() {
        db.clearAllTables()
        db.close()
    }

    private fun getSampleArticles(): List<ArticleEntity> {
        val first = ArticleEntity(
            1L,
            "First",
            "First desc",
            1.0,
            1.0,
            null,
            "en"
        )
        val second = ArticleEntity(
            2L,
            "Second",
            "Second desc",
            2.0,
            2.0,
            null,
            "ru"
        )
        return listOf(first, second)
    }

    @Test
    fun insertsAndReturnsArticles() {
        getSampleArticles().forEach { favoritesDao.insertArticle(it).blockingAwait() }

        val entities = favoritesDao.getArticles().blockingFirst()

        assertEquals(getSampleArticles().size, entities.size)
        entities.forEachIndexed { index, entity -> assertEquals(getSampleArticles()[index], entity)}
    }

    @Test
    fun removesArticle() {
        getSampleArticles().forEach { favoritesDao.insertArticle(it).blockingAwait() }

        favoritesDao.deleteArticle(getSampleArticles()[0]).blockingAwait()
        val entities = favoritesDao.getArticles().blockingFirst()

        assertEquals(getSampleArticles().size - 1, entities.size)
    }

    @Test
    fun returnsArticleByPageid() {
        getSampleArticles().forEach { favoritesDao.insertArticle(it).blockingAwait() }

        val entity = favoritesDao.getArticleByPageId(getSampleArticles()[1].pageid).blockingGet()

        assertEquals(getSampleArticles()[1], entity)
    }

    @Test
    fun returnsEmptyListWhenNoFavorites() {
        val entities = favoritesDao.getArticles().blockingFirst()

        assertEquals(0, entities.size)
    }
}