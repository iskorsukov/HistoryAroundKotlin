package com.iskorsukov.historyaround.service.favorites

import com.google.common.truth.Truth.assertThat
import com.iskorsukov.historyaround.CommonValues
import com.iskorsukov.historyaround.data.entity.ArticleEntitiesCreator
import com.iskorsukov.historyaround.data.entity.ArticleEntity
import com.iskorsukov.historyaround.mapper.entity.ArticleEntitiesMapper
import com.iskorsukov.historyaround.model.ArticleItemCreator
import com.iskorsukov.historyaround.service.storage.AppDatabase
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class FavoritesSourceTest {

    private val databaseMock: AppDatabase = mockk()
    private val daoMock: FavouritesDao = mockk(relaxed = true)
    private val mapper: ArticleEntitiesMapper = spyk(ArticleEntitiesMapper())

    private val articlesFlow: MutableStateFlow<List<ArticleEntity>> = MutableStateFlow(emptyList())

    private lateinit var favoritesSource: FavoritesSource

    @Before
    fun setUp() {
        mockData()
        favoritesSource = FavoritesSourceImpl(databaseMock, mapper)
    }

    @Test
    fun getFavoriteArticles() = runTest {
        val favorites = favoritesSource.getFavoriteArticles().first()

        assertThat(favorites).containsExactlyElementsIn(ArticleItemCreator.createNormalList())
        coVerify { daoMock.getArticles() }
        verify { mapper.mapArticleEntityList(ArticleEntitiesCreator.createNormal()) }
    }

    @Test
    fun getFavoriteArticles_Empty() = runTest {
        articlesFlow.value = ArticleEntitiesCreator.createEmpty()

        val favorites = favoritesSource.getFavoriteArticles().first()

        assertThat(favorites).isEmpty()
        coVerify { daoMock.getArticles() }
        verify { mapper.mapArticleEntityList(any()) }
    }

    @Test(expected = IOException::class)
    fun getFavoriteArticles_Exception() = runTest {
        every { daoMock.getArticles() } throws IOException()

        favoritesSource.getFavoriteArticles().first()
    }

    @Test
    fun addToFavorites() = runTest {
        favoritesSource.addToFavorites(ArticleItemCreator.createNormalList().first())

        coVerify { daoMock.insertArticle(ArticleEntitiesCreator.createNormal().first()) }
        verify { mapper.mapArticleToEntity(ArticleItemCreator.createNormalList().first()) }
    }

    @Test(expected = IOException::class)
    fun addToFavorites_Exception() = runTest {
        coEvery { daoMock.insertArticle(any()) } throws IOException()

        favoritesSource.addToFavorites(ArticleItemCreator.createNormalList().first())
    }

    @Test
    fun removeFromFavorites() = runTest {
        favoritesSource.removeFromFavorites(ArticleItemCreator.createNormalList().first())

        coVerify { daoMock.deleteArticle(ArticleEntitiesCreator.createNormal().first()) }
        verify { mapper.mapArticleToEntity(ArticleItemCreator.createNormalList().first()) }
    }

    @Test(expected = IOException::class)
    fun removeFromFavorites_Exception() = runTest {
        coEvery { daoMock.deleteArticle(any()) } throws IOException()

        favoritesSource.removeFromFavorites(ArticleItemCreator.createNormalList().first())
    }

    @Test
    fun isFavorite_True() = runTest {
        val pageId = CommonValues.pageids[0]

        val isFavorite = favoritesSource.isFavorite(pageId.toString())

        assertThat(isFavorite).isTrue()
        coVerify { daoMock.getArticleByPageId(pageId) }
    }

    @Test
    fun isFavorite_False() = runTest {
        val pageId = CommonValues.pageids[0]
        coEvery { daoMock.getArticleByPageId(any()) } returns null

        val isFavorite = favoritesSource.isFavorite(pageId.toString())

        assertThat(isFavorite).isFalse()
        coVerify { daoMock.getArticleByPageId(pageId) }
    }

    @Test(expected = IOException::class)
    fun isFavorite_Exception() = runTest {
        val pageId = CommonValues.pageids[0]
        coEvery { daoMock.getArticleByPageId(any()) } throws IOException()

        favoritesSource.isFavorite(pageId.toString())
    }

    private fun mockData() {
        every { databaseMock.getFavouritesDao() } returns daoMock
        every { daoMock.getArticles() } returns articlesFlow
        coEvery { daoMock.getArticleByPageId(any()) } returns ArticleEntitiesCreator.createNormal().first()
        articlesFlow.value = ArticleEntitiesCreator.createNormal()
    }
}