package com.iskorsukov.historyaround.service.api

import com.google.common.truth.Truth.assertThat
import com.iskorsukov.historyaround.CommonValues
import com.iskorsukov.historyaround.mapper.response.ArticleResponsesMapper
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.model.detail.ArticleDetails
import com.iskorsukov.historyaround.data.response.ArticleQueryResponseCreator
import com.iskorsukov.historyaround.data.response.DetailQueryResponseCreator
import com.iskorsukov.historyaround.data.response.GeoQueryResponseCreator
import com.iskorsukov.historyaround.data.response.ResponseDataProvider
import com.iskorsukov.historyaround.model.ArticleDetailsCreator
import com.iskorsukov.historyaround.model.ModelDataProvider
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class WikiSourceTest {

    private val wikiApiMock: WikiApi = mockk()
    private val responseMapper = spyk(ArticleResponsesMapper())

    private val languageCode = "en"
    private val latlon = 0.0 to 0.0
    private val radius = 1000
    val pageid = "1"

    @Before
    fun mockNormalData() {
        mockApiLoadGeoData()
        mockApiLoadArticlesData()
        mockApiLoadDetailsData()
    }

    @Test
    fun loadArticleItems() = runTest {

        val articles = loadArticleItems(testScheduler)
        advanceUntilIdle()

        assertThat(articles).containsExactlyElementsIn(ModelDataProvider.articleItemsBase)
        coVerifyOrder {
            wikiApiMock.loadGeoData(languageCode, radius, "${latlon.first}|${latlon.second}")
            wikiApiMock.loadArticlesData(languageCode, CommonValues.pageids.joinToString(separator = "|"))
        }
        verify {
            responseMapper.mapGeoResponseList(GeoQueryResponseCreator.createNormal().query.data)
            responseMapper.mapArticleResponseList(ResponseDataProvider.aritcleItemResponses, languageCode)
        }
    }

    @Test
    fun loadArticleItems_EmptyGeoData() = runTest {
        mockApiLoadGeoData_Empty()

        val articles = loadArticleItems(testScheduler)
        advanceUntilIdle()

        assertThat(articles).isEmpty()
        coVerify {
            wikiApiMock.loadGeoData(languageCode, radius, "${latlon.first}|${latlon.second}")
        }
        coVerify(exactly = 0) {
            wikiApiMock.loadArticlesData(languageCode, any())
        }
        verify {
            responseMapper.mapGeoResponseList(GeoQueryResponseCreator.createEmpty().query.data)
        }
        verify(exactly = 0) {
            responseMapper.mapArticleResponseList(any(), languageCode)
        }
    }

    @Test
    fun loadArticleItems_EmptyArticles() = runTest {
        mockApiLoadArticlesData_Empty()

        val articles = loadArticleItems(testScheduler)
        advanceUntilIdle()

        assertThat(articles).isEmpty()
        coVerifyOrder {
            wikiApiMock.loadGeoData(languageCode, radius, "${latlon.first}|${latlon.second}")
            wikiApiMock.loadArticlesData(languageCode, CommonValues.pageids.joinToString(separator = "|"))
        }
        verify {
            responseMapper.mapGeoResponseList(GeoQueryResponseCreator.createNormal().query.data)
            responseMapper.mapArticleResponseList(emptyList(), languageCode)
        }
    }

    @Test(expected = IOException::class)
    fun loadArticleItems_GeoDataIOException() = runTest {
        mockApiLoadGeoData_IOException()

        loadArticleItems(testScheduler)
        advanceUntilIdle()

        coVerify {
            wikiApiMock.loadGeoData(languageCode, radius, "${latlon.first}|${latlon.second}")
        }
        coVerify(exactly = 0) {
            wikiApiMock.loadArticlesData(languageCode, any())
        }
    }

    @Test(expected = IOException::class)
    fun loadArticleItems_ArticlesDataIOException() = runTest {
        mockApiLoadArticlesData_IOException()

        loadArticleItems(testScheduler)
        advanceUntilIdle()
    }

    @Test
    fun loadArticleDetail() = runTest {
        val details = loadDetailsData(testScheduler)
        advanceUntilIdle()

        assertThat(details).isEqualTo(ArticleDetailsCreator.createNormal())
        coVerify {
            wikiApiMock.loadDetailsData(languageCode, pageid)
        }
        verify {
            responseMapper.mapArticleDetailsResponseList(
                DetailQueryResponseCreator.createNormal().query.pages.values.toList(),
                languageCode
            )
        }
    }

    @Test(expected = NoSuchElementException::class)
    fun loadArticleDetail_Empty_ThrowsNoSuchElementException() = runTest {
        mockApiLoadDetailsData_Empty()

        val details = loadDetailsData(testScheduler)
        advanceUntilIdle()

        coVerify {
            wikiApiMock.loadDetailsData(languageCode, pageid)
        }
        verify {
            responseMapper.mapArticleDetailsResponseList(
                DetailQueryResponseCreator.createEmpty().query.pages.values.toList(),
                languageCode
            )
        }
    }

    @Test(expected = IOException::class)
    fun loadArticleDetail_IOException() = runTest {
        mockApiLoadDetailsData_IOException()

        val details = loadDetailsData(testScheduler)
        advanceUntilIdle()

        coVerify {
            wikiApiMock.loadDetailsData(languageCode, pageid)
        }
        verify {
            responseMapper wasNot called
        }
    }

    private suspend fun loadArticleItems(testScheduler: TestCoroutineScheduler): List<ArticleItem> {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val wikiSource = WikiSourceImpl(wikiApiMock, responseMapper, dispatcher)

        return wikiSource.loadArticleItems(languageCode, latlon, radius)
    }

    private suspend fun loadDetailsData(testScheduler: TestCoroutineScheduler): ArticleDetails {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val wikiSource = WikiSourceImpl(wikiApiMock, responseMapper, dispatcher)

        return wikiSource.loadArticleDetails(languageCode, pageid)
    }

    private fun mockApiLoadGeoData() {
        coEvery { wikiApiMock.loadGeoData(any(), any(), any()) } returns GeoQueryResponseCreator.createNormal()
    }

    private fun mockApiLoadGeoData_Empty() {
        coEvery {
            wikiApiMock.loadGeoData(any(), any(), any())
        } returns GeoQueryResponseCreator.createEmpty()
    }

    private fun mockApiLoadGeoData_IOException() {
        coEvery {
            wikiApiMock.loadGeoData(any(), any(), any())
        } throws IOException()
    }

    private fun mockApiLoadArticlesData() {
        coEvery {
            wikiApiMock.loadArticlesData(any(), any())
        } returns ArticleQueryResponseCreator.createNormalUnfiltered()
    }

    private fun mockApiLoadArticlesData_Empty() {
        coEvery {
            wikiApiMock.loadArticlesData(any(), any())
        } returns ArticleQueryResponseCreator.createEmpty()
    }

    private fun mockApiLoadArticlesData_IOException() {
        coEvery {
            wikiApiMock.loadArticlesData(any(), any())
        } throws IOException()
    }

    private fun mockApiLoadDetailsData() {
        coEvery {
            wikiApiMock.loadDetailsData(any(), any())
        } returns DetailQueryResponseCreator.createNormal()
    }

    private fun mockApiLoadDetailsData_Empty() {
        coEvery {
            wikiApiMock.loadDetailsData(any(), any())
        } returns DetailQueryResponseCreator.createEmpty()
    }

    private fun mockApiLoadDetailsData_IOException() {
        coEvery {
            wikiApiMock.loadDetailsData(any(), any())
        } throws IOException()
    }
}