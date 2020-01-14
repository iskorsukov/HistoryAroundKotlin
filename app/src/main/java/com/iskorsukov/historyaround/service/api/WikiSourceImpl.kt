package com.iskorsukov.historyaround.service.api

import io.reactivex.Single
import com.iskorsukov.historyaround.mapper.response.ArticleResponsesMapper
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.model.detail.ArticleDetails
import com.iskorsukov.historyaround.model.geo.GeoItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WikiSourceImpl @Inject constructor (
    private val api: WikiApi,
    private val mapper: ArticleResponsesMapper
): WikiSource {

    override fun loadArticleItems(languageCode: String?, latlng: Pair<Double, Double>, radius: Int): Single<List<ArticleItem>> {
        return api.loadGeoData(languageCode, radius, latlng.first.toString() + "|" + latlng.second.toString())
            .flatMap { geoResponse ->
                    val geoItems: List<GeoItem> = geoResponse.query.data.map(mapper::mapGeoResponse)
                    val pageIdsString: String = geoItems.take(50).joinToString("|") {geoItem -> geoItem.pageid.toString() }
                    api.loadArticlesData(languageCode, pageIdsString)
            }
            .map {
                it.query?.pages?.filter { responseEntry -> responseEntry.value.coordinates != null }?.map { responseEntry -> mapper.mapArticleResponse(responseEntry.value, languageCode ?: "") } ?: emptyList()
            }
            .retry(3)
    }

    override fun loadArticleDetails(languageCode: String?, pageid: String): Single<ArticleDetails> {
        return api.loadDetailsData(languageCode, pageid)
            .flatMap {
                Single.just(it.query.pages.map { responseEntry -> mapper.mapArticleDetailsResponse(responseEntry.value, languageCode ?: "") }.first())
            }
            .retry(3)
    }
}