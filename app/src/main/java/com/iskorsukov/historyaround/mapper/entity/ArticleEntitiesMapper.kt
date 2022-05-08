package com.iskorsukov.historyaround.mapper.entity

import com.iskorsukov.historyaround.data.entity.ArticleEntity
import com.iskorsukov.historyaround.data.entity.ArticleThumbnailEntity
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.model.common.ArticleThumbnail
import javax.inject.Inject

class ArticleEntitiesMapper @Inject constructor() {

    fun mapArticleEntityList(articleEntityList: List<ArticleEntity>): List<ArticleItem> {
        return articleEntityList.map { mapArticleEntity(it) }
    }

    fun mapArticleEntity(articleEntity: ArticleEntity): ArticleItem {
        return ArticleItem(
            articleEntity.pageid.toString(),
            articleEntity.title,
            articleEntity.description,
            articleEntity.lat to articleEntity.lon,
            articleEntity.thumbnail?.run {
                mapThumbnailEntity(this)
            },
            articleEntity.languageCode
        )
    }

    private fun mapThumbnailEntity(thumbnailEntity: ArticleThumbnailEntity): ArticleThumbnail {
        return ArticleThumbnail(
            thumbnailEntity.thumbnailUrl,
            thumbnailEntity.thumbnailWidth,
            thumbnailEntity.thumbnailHeight
        )
    }

    fun mapArticleToEntity(articleItem: ArticleItem): ArticleEntity {
        return ArticleEntity(
            articleItem.pageid.toLong(),
            articleItem.title,
            articleItem.description,
            articleItem.latlng.first,
            articleItem.latlng.second,
            articleItem.thumbnail?.run { mapThumbnailToEntity(this) },
            articleItem.languageCode
        )
    }

    private fun mapThumbnailToEntity(thumbnail: ArticleThumbnail): ArticleThumbnailEntity {
        return ArticleThumbnailEntity(
            null,
            thumbnail.url,
            thumbnail.width,
            thumbnail.height
        )
    }
}