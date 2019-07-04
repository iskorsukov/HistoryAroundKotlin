package my.projects.historyaroundkotlin.service.api

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
        return api.loadGeoData(radius, latlng.first.toString() + "," + latlng.second.toString())
            .flatMap { geoResponse ->
                    val geoItems: List<GeoItem> = geoResponse.query.data.map(mapper::mapGeoResponse)
                    val pageIdsString: String = geoItems.joinToString { geoItem -> geoItem.pageid.toString() }
                    api.loadArticlesData(pageIdsString)
            }
            .retry(3)
            .flatMap {
                Single.just(it.query.pages.map { responseEntry -> mapper.mapArticleResponse(responseEntry.value) })
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadArticleDetails(pageid: Long): Single<ArticleDetails> {
        return api.loadDetailsData(pageid.toString())
            .flatMap {
                // TODO check if has any elements first
                Single.just(it.query.pages.map { responseEntry -> mapper.mapArticleDetailsResponse(responseEntry.value) }.first())
            }
            .retry(3)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}