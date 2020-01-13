package com.iskorsukov.historyaround.service.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iskorsukov.historyaround.data.response.article.ArticleItemResponse
import com.iskorsukov.historyaround.data.response.article.ArticlePagesResponse
import com.iskorsukov.historyaround.data.response.article.ArticleQueryResponse
import com.iskorsukov.historyaround.data.response.common.CoordinatesResponse
import com.iskorsukov.historyaround.data.response.detail.DetailDataResponse
import com.iskorsukov.historyaround.data.response.detail.DetailItemResponse
import com.iskorsukov.historyaround.data.response.detail.DetailQueryResponse
import com.iskorsukov.historyaround.data.response.geo.GeoDataResponse
import com.iskorsukov.historyaround.data.response.geo.GeoItemResponse
import com.iskorsukov.historyaround.data.response.geo.GeoQueryResponse
import com.iskorsukov.historyaround.mapper.response.ArticleResponsesMapper
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.model.detail.ArticleDetails
import com.iskorsukov.historyaround.utils.MockitoUtil
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class WikiSourceTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockApi = Mockito.mock(WikiApi::class.java)
    private val mapper = ArticleResponsesMapper()
    private val wikiSource = WikiSourceImpl(mockApi, mapper)

    private fun getSampleGeoQueryResponse(): GeoQueryResponse {
        return GeoQueryResponse(GeoDataResponse(listOf(GeoItemResponse(1L), GeoItemResponse(2L))))
    }

    private fun getSampleArticles(): List<ArticleItem> {
        return getSampleArticleResponses().map { mapper.mapArticleResponse(it, "en") }
    }

    private fun getSampleArticleResponses(): List<ArticleItemResponse> {
        val first = ArticleItemResponse(
            1L,
            "First",
            "First desc",
            listOf(CoordinatesResponse(1.0, 1.0)),
            null
        )
        val second = ArticleItemResponse(
            2L,
            "Second",
            "Second desc",
            listOf(CoordinatesResponse(2.0, 2.0)),
            null
        )
        return listOf(first, second)
    }

    private fun getSampleArticlesQueryResponse(): ArticleQueryResponse {
        val articlesMap = HashMap<String, ArticleItemResponse>()
        getSampleArticleResponses().forEachIndexed {index, item -> articlesMap[index.toString()] = item }
        return ArticleQueryResponse(ArticlePagesResponse(articlesMap))
    }

    private fun pushSampleData() {
        Mockito.`when`(mockApi.loadGeoData(MockitoUtil.any(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())).thenReturn(Single.just(getSampleGeoQueryResponse()))
        Mockito.`when`(mockApi.loadArticlesData(MockitoUtil.any(), ArgumentMatchers.anyString())).thenReturn(Single.just(getSampleArticlesQueryResponse()))
    }

    @Test
    fun loadsArticles() {
        pushSampleData()

        val articles = wikiSource.loadArticleItems("en", 1.0 to 1.0, 500).blockingGet()

        assertEquals(2, articles.size)
        articles.forEachIndexed { index, item -> assertEquals(getSampleArticles()[index], item) }
    }

    private fun pushEmptyData() {
        Mockito.`when`(mockApi.loadGeoData(MockitoUtil.any(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())).thenReturn(Single.just(
            GeoQueryResponse(GeoDataResponse(emptyList()))
        ))
        Mockito.`when`(mockApi.loadArticlesData(MockitoUtil.any(), ArgumentMatchers.anyString())).thenReturn(Single.just(
            ArticleQueryResponse(ArticlePagesResponse(emptyMap()))
        ))
    }

    @Test
    fun returnsEmptyListOnNoArticles() {
        pushEmptyData()

        val articles = wikiSource.loadArticleItems("en", 1.0 to 1.0, 500).blockingGet()

        assertEquals(0, articles.size)
    }

    private fun getSampleDetailsResponse(): DetailItemResponse {
        return DetailItemResponse(
            1L,
            "Title",
            "Desc",
            null,
            listOf(CoordinatesResponse(1.0, 1.0)),
            "fullurl"
        )
    }

    private fun getSampleDetails(): ArticleDetails {
        return mapper.mapArticleDetailsResponse(getSampleDetailsResponse(), "en")
    }

    private fun pushDetailsData() {
        Mockito.`when`(mockApi.loadDetailsData(MockitoUtil.any(), ArgumentMatchers.anyString())).thenReturn(Single.just(
            DetailQueryResponse(DetailDataResponse(mapOf("1" to getSampleDetailsResponse())))
        ))
    }

    @Test
    fun loadsArticleDetails() {
        pushDetailsData()

        val details = wikiSource.loadArticleDetails("en", "1").blockingGet()

        assertEquals(getSampleDetails(), details)
    }
}