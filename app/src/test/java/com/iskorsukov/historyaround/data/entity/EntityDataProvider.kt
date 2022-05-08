package com.iskorsukov.historyaround.data.entity

import com.iskorsukov.historyaround.CommonValues

object EntityDataProvider {

    val articleEntitiesBase = listOf(
        ArticleEntity(
            CommonValues.pageids[0],
            CommonValues.articleTitles[0],
            CommonValues.articleDescriptions[0]!!,
            CommonValues.articleCoordinatePairs[0]!!.first().first,
            CommonValues.articleCoordinatePairs[0]!!.first().second,
            CommonValues.articleThumbnailTriples[0]!!.run {
                ArticleThumbnailEntity(null, first, second, third) },
            CommonValues.languageCode
        )
    )
}