package com.iskorsukov.historyaround.service.api

import com.iskorsukov.historyaround.mapper.response.ArticleResponsesMapper
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.model.detail.ArticleDetails
import com.iskorsukov.historyaround.model.geo.GeoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WikiSourceImpl @Inject constructor (
    private val api: WikiApi,
    private val mapper: ArticleResponsesMapper
): WikiSource {

    override suspend fun loadArticleItems(languageCode: String?, latlng: Pair<Double, Double>, radius: Int): List<ArticleItem> {
        return withContext(Dispatchers.IO) {
            val geoData = api.loadGeoData(
                languageCode,
                radius,
                "${latlng.first}|${latlng.second}"
            )
            val geoItems: List<GeoItem> = geoData.query.data.map(mapper::mapGeoResponse)
            val pageIdsString: String = geoItems
                .take(50)
                .joinToString("|") { geoItem -> geoItem.pageid.toString() }
            val articleQueryData = api.loadArticlesData(languageCode, pageIdsString)
            return@withContext articleQueryData.query?.pages
                ?.filter { responseEntry -> responseEntry.value.coordinates != null }
                ?.map { responseEntry ->
                    mapper.mapArticleResponse(responseEntry.value, languageCode ?: "")
                } ?: emptyList()
        }
    }

    override suspend fun loadArticleDetails(languageCode: String?, pageid: String): ArticleDetails {
        return withContext(Dispatchers.IO) {
            val detailQueryData = api.loadDetailsData(languageCode, pageid)
            return@withContext detailQueryData.query.pages
                .map { responseEntry ->
                    mapper.mapArticleDetailsResponse(
                        responseEntry.value, languageCode ?: ""
                    )
                }
                .first()
        }
    }
}