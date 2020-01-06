package my.projects.historyaroundkotlin.mapper.response

import my.projects.historyaroundkotlin.data.response.article.ArticleItemResponse
import my.projects.historyaroundkotlin.data.response.common.ThumbnailResponse
import my.projects.historyaroundkotlin.data.response.detail.DetailItemResponse
import my.projects.historyaroundkotlin.data.response.geo.GeoItemResponse
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.model.common.ArticleThumbnail
import my.projects.historyaroundkotlin.model.detail.ArticleDetails
import my.projects.historyaroundkotlin.model.geo.GeoItem
import javax.inject.Inject

class ArticleResponsesMapper @Inject constructor() {

    fun mapGeoResponse(geoItemResponse: GeoItemResponse): GeoItem {
        return GeoItem(geoItemResponse.pageid)
    }

    fun mapArticleResponse(articleResponse: ArticleItemResponse, languageCode: String): ArticleItem {
        return ArticleItem(
            articleResponse.pageid.toString(),
            articleResponse.title,
            articleResponse.description ?: "",
            articleResponse.coordinates!![0].lat to articleResponse.coordinates!![0].lon,
            articleResponse.thumbnail?.run {
                mapThumbnailResponse(this)
            },
            languageCode
        )
    }

    fun mapArticleDetailsResponse(articleDetailsResponse: DetailItemResponse, languageCode: String): ArticleDetails {
        return ArticleDetails(
            articleDetailsResponse.pageid,
            articleDetailsResponse.title,
            articleDetailsResponse.extract,
            articleDetailsResponse.thumbnail?.run {
                mapThumbnailResponse(this)
            },
            articleDetailsResponse.coordinates!![0].lat to articleDetailsResponse.coordinates!![0].lon,
            articleDetailsResponse.fullurl,
            languageCode
        )
    }

    fun mapThumbnailResponse(thumbnailResponse: ThumbnailResponse): ArticleThumbnail {
        return ArticleThumbnail(
            thumbnailResponse.source,
            thumbnailResponse.width,
            thumbnailResponse.height
        )
    }
}