package my.projects.historyaroundkotlin.service.api

import io.reactivex.Single
import my.projects.historyaroundkotlin.mapper.response.ArticleResponsesMapper
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.model.detail.ArticleDetails
import my.projects.historyaroundkotlin.model.geo.GeoItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WikiSourceImpl @Inject constructor (
    private val api: WikiApi,
    private val mapper: ArticleResponsesMapper
): WikiSource {
    override fun loadArticleItems(latlng: Pair<Double, Double>, radius: Int): Single<List<ArticleItem>> {
        return api.loadGeoData(radius, latlng.first.toString() + "|" + latlng.second.toString())
            .flatMap { geoResponse ->
                    val geoItems: List<GeoItem> = geoResponse.query.data.map(mapper::mapGeoResponse)
                    val pageIdsString: String = geoItems.joinToString("|") {geoItem -> geoItem.pageid.toString() }
                    api.loadArticlesData(pageIdsString)
            }
            .map {
                it.query.pages.filter { responseEntry -> responseEntry.value.coordinates != null }.map { responseEntry -> mapper.mapArticleResponse(responseEntry.value) }
            }
            .retry(3)
    }

    override fun loadArticleDetails(pageid: String): Single<ArticleDetails> {
        return api.loadDetailsData(pageid)
            .flatMap {
                Single.just(it.query.pages.map { responseEntry -> mapper.mapArticleDetailsResponse(responseEntry.value) }.first())
            }
            .retry(3)
    }
}