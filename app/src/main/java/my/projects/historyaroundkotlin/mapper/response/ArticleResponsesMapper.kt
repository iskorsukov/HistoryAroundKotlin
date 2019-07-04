package my.projects.historyaroundkotlin.mapper.response

import my.projects.historyaroundkotlin.data.response.article.ArticleItemResponse
import my.projects.historyaroundkotlin.data.response.common.ThumbnailResponse
import my.projects.historyaroundkotlin.data.response.detail.DetailItemResponse
import my.projects.historyaroundkotlin.data.response.geo.GeoItemResponse
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.model.common.ArticleThumbnail
import my.projects.historyaroundkotlin.model.detail.ArticleDetails
import my.projects.historyaroundkotlin.model.geo.GeoItem

class ArticleResponsesMapper {

    fun mapGeoResponse(geoItemResponse: GeoItemResponse): GeoItem {
        return GeoItem(geoItemResponse.pageid)
    }

    fun mapArticleResponse(articleResponse: ArticleItemResponse): ArticleItem {
        return ArticleItem(
            articleResponse.pageid,
            articleResponse.title,
            articleResponse.description,
            articleResponse.coordinates.lat to articleResponse.coordinates.lon,
            articleResponse.thumbnail?.run {
                mapThumbnailResponse(this)
            }
        )
    }

    fun mapArticleDetailsResponse(articleDetailsResponse: DetailItemResponse): ArticleDetails {
        return ArticleDetails(
            articleDetailsResponse.pageid,
            articleDetailsResponse.title,
            articleDetailsResponse.extract,
            articleDetailsResponse.thumbnail?.run {
                mapThumbnailResponse(this)
            },
            articleDetailsResponse.fullurl
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