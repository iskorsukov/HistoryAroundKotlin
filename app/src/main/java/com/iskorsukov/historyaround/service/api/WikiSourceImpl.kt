package com.iskorsukov.historyaround.service.api

import com.iskorsukov.historyaround.injection.DispatcherIO
import com.iskorsukov.historyaround.mapper.response.ArticleResponsesMapper
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.model.detail.ArticleDetails
import com.iskorsukov.historyaround.model.geo.GeoItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WikiSourceImpl @Inject constructor (
    private val api: WikiApi,
    private val mapper: ArticleResponsesMapper,
    @DispatcherIO private val dispatcher: CoroutineDispatcher
): WikiSource {

    override suspend fun loadArticleItems(languageCode: String?, latlng: Pair<Double, Double>, radius: Int): List<ArticleItem> {
        return withContext(dispatcher) {
            val geoData = api.loadGeoData(
                languageCode,
                radius,
                "${latlng.first}|${latlng.second}"
            )
            val geoItems: List<GeoItem> = mapper.mapGeoResponseList(geoData.query.data)
            if (geoItems.isEmpty()) {
                return@withContext emptyList()
            }
            ensureActive()
            val pageIdsString: String = geoItems
                .take(50)
                .joinToString("|") { geoItem -> geoItem.pageid.toString() }
            val articleQueryData = api.loadArticlesData(languageCode, pageIdsString)
            return@withContext mapper.mapArticleResponseList(
                articleQueryData.query?.pages?.values?.toList() ?: emptyList(),
                languageCode ?: ""
            )
        }
    }

    override suspend fun loadArticleDetails(languageCode: String?, pageid: String): ArticleDetails {
        return withContext(dispatcher) {
            val detailQueryData = api.loadDetailsData(languageCode, pageid)
            return@withContext mapper.mapArticleDetailsResponseList(
                detailQueryData.query.pages.values.toList(), languageCode ?: ""
            ).first()
        }
    }
}