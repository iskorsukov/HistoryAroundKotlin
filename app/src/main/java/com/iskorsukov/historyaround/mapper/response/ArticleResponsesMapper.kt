package com.iskorsukov.historyaround.mapper.response

import com.iskorsukov.historyaround.data.response.article.ArticleItemResponse
import com.iskorsukov.historyaround.data.response.common.ThumbnailResponse
import com.iskorsukov.historyaround.data.response.detail.DetailItemResponse
import com.iskorsukov.historyaround.data.response.geo.GeoItemResponse
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.model.common.ArticleThumbnail
import com.iskorsukov.historyaround.model.detail.ArticleDetails
import com.iskorsukov.historyaround.model.geo.GeoItem
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