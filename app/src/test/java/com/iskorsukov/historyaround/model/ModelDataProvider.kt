package com.iskorsukov.historyaround.model

import com.iskorsukov.historyaround.CommonValues
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.model.common.ArticleThumbnail
import com.iskorsukov.historyaround.model.detail.ArticleDetails

object ModelDataProvider {

    val articleItemsBase = listOf(
        ArticleItem(
            CommonValues.pageids[0].toString(),
            CommonValues.articleTitles[0],
            CommonValues.articleDescriptions[0]!!,
            CommonValues.articleCoordinatePairs[0]!!.first(),
            CommonValues.articleThumbnailTriples[0]?.run {
                ArticleThumbnail(first, second, third) },
            CommonValues.languageCode
        )
    )

    val detailItemsBase = listOf(
        ArticleDetails(
            CommonValues.pageids[0],
            CommonValues.articleTitles[0],
            CommonValues.articleExtracts[0]!!,
            CommonValues.articleThumbnailTriples[0]?.run {
                ArticleThumbnail(first, second, third) },
            CommonValues.articleCoordinatePairs[0]!!.first(),
            CommonValues.fullUrls[0]!!,
            CommonValues.languageCode
        )
    )
}