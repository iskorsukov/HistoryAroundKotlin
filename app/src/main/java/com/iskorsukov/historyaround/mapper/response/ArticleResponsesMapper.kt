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

    fun mapGeoResponseList(geoItemResponseList: List<GeoItemResponse>): List<GeoItem> {
        return geoItemResponseList.map { geoItemResponse -> GeoItem(geoItemResponse.pageid) }
    }

    fun mapArticleResponseList(articleResponseList: List<ArticleItemResponse>, languageCode: String): List<ArticleItem> {
        return articleResponseList.mapNotNull { articleItemResponse ->
            mapArticleResponse(articleItemResponse, languageCode)
        }
    }

    private fun mapArticleResponse(articleResponse: ArticleItemResponse, languageCode: String): ArticleItem? {
        if (!isArticleItemResponseValid(articleResponse)) {
            return null
        }
        return ArticleItem(
            articleResponse.pageid.toString(),
            articleResponse.title!!,
            articleResponse.description!!,
            articleResponse.coordinates!!.first().lat to articleResponse.coordinates.first().lon,
            articleResponse.thumbnail?.run {
                mapThumbnailResponse(this)
            },
            languageCode
        )
    }

    private fun isArticleItemResponseValid(articleResponse: ArticleItemResponse): Boolean {
        if (articleResponse.coordinates == null || articleResponse.coordinates.isEmpty()) {
            return false
        }
        if (articleResponse.title == null || articleResponse.title.isEmpty()) {
            return false
        }
        if (articleResponse.description == null || articleResponse.description.isEmpty()) {
            return false
        }
        return true
    }

    fun mapArticleDetailsResponseList(articleDetailsResponseList: List<DetailItemResponse>, languageCode: String): List<ArticleDetails> {
        return articleDetailsResponseList.mapNotNull { articleDetailsResponse ->
            mapArticleDetailsResponse(articleDetailsResponse, languageCode)
        }
    }

    private fun mapArticleDetailsResponse(articleDetailsResponse: DetailItemResponse, languageCode: String): ArticleDetails? {
        if (!isArticleDetailsResponseValid(articleDetailsResponse)) {
            return null
        }
        return ArticleDetails(
            articleDetailsResponse.pageid,
            articleDetailsResponse.title ?: "",
            articleDetailsResponse.extract ?: "",
            articleDetailsResponse.thumbnail?.run {
                mapThumbnailResponse(this)
            },
            articleDetailsResponse.coordinates!!.first().lat to articleDetailsResponse.coordinates.first().lon,
            articleDetailsResponse.fullurl!!,
            languageCode
        )
    }

    private fun isArticleDetailsResponseValid(articleDetailsResponse: DetailItemResponse): Boolean {
        if (articleDetailsResponse.coordinates == null || articleDetailsResponse.coordinates.isEmpty()) {
            return false
        }
        if (articleDetailsResponse.title == null || articleDetailsResponse.title.isEmpty()) {
            return false
        }
        if (articleDetailsResponse.extract == null || articleDetailsResponse.extract.isEmpty()) {
            return false
        }
        if (articleDetailsResponse.fullurl == null || articleDetailsResponse.fullurl.isEmpty()) {
            return false
        }
        return true
    }

    private fun mapThumbnailResponse(thumbnailResponse: ThumbnailResponse): ArticleThumbnail {
        return ArticleThumbnail(
            thumbnailResponse.source,
            thumbnailResponse.width,
            thumbnailResponse.height
        )
    }
}