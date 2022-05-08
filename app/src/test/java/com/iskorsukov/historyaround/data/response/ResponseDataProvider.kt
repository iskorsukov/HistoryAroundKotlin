package com.iskorsukov.historyaround.data.response

import com.iskorsukov.historyaround.CommonValues
import com.iskorsukov.historyaround.data.response.article.ArticleItemResponse
import com.iskorsukov.historyaround.data.response.common.CoordinatesResponse
import com.iskorsukov.historyaround.data.response.common.ThumbnailResponse
import com.iskorsukov.historyaround.data.response.detail.DetailItemResponse

object ResponseDataProvider {

    val aritcleItemResponses = (0 until CommonValues.pageids.size).map { index ->
        ArticleItemResponse(
            CommonValues.pageids[index],
            CommonValues.articleTitles[index],
            CommonValues.articleDescriptions[index],
            CommonValues.articleCoordinatePairs[index]?.map {
                CoordinatesResponse(it.first, it.second) },
            CommonValues.articleThumbnailTriples[index]?.run {
                ThumbnailResponse(first, second, third) }
        )
    }

    val articlePagesMap = (0 until CommonValues.pageids.size).associate { index ->
        CommonValues.pageids[index].toString() to aritcleItemResponses[index]
    }

    val articleDetailsResponses = (0 until CommonValues.pageids.size).map { index ->
        DetailItemResponse(
            CommonValues.pageids[index],
            CommonValues.articleTitles[index],
            CommonValues.articleExtracts[index],
            CommonValues.articleThumbnailTriples[index]?.run {
                ThumbnailResponse(first, second, third) },
            CommonValues.articleCoordinatePairs[index]?.map {
                CoordinatesResponse(it.first, it.second) },
            CommonValues.fullUrls[index]
        )
    }

    val articleDetailPagesMap = (0 until CommonValues.pageids.size).associate { index ->
        CommonValues.pageids[index].toString() to articleDetailsResponses[index]
    }
}