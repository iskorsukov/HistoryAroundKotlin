package my.projects.historyaroundkotlin.mapper.entity

import my.projects.historyaroundkotlin.data.entity.ArticleEntity
import my.projects.historyaroundkotlin.data.entity.ArticleThumbnailEntity
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.model.common.ArticleThumbnail
import javax.inject.Inject

class ArticleEntitiesMapper @Inject constructor() {

    fun mapArticleEntity(articleEntity: ArticleEntity, thumbnailEntity: ArticleThumbnailEntity?): ArticleItem {
        return ArticleItem(
            articleEntity.pageid.toString(),
            articleEntity.title,
            articleEntity.description,
            articleEntity.lat to articleEntity.lon,
            thumbnailEntity?.run {
                mapThumbnailEntity(this)
            }
        )
    }

    fun mapThumbnailEntity(thumbnailEntity: ArticleThumbnailEntity): ArticleThumbnail {
        return ArticleThumbnail(
            thumbnailEntity.url,
            thumbnailEntity.width,
            thumbnailEntity.height
        )
    }

    fun mapArticleToEntity(articleItem: ArticleItem): ArticleEntity {
        return ArticleEntity(
            articleItem.pageid.toLong(),
            articleItem.title,
            articleItem.description,
            articleItem.latlng.first,
            articleItem.latlng.second,
            null
        )
    }

    fun mapThumbnailToEntity(thumbnail: ArticleThumbnail): ArticleThumbnailEntity {
        return ArticleThumbnailEntity(
            -1L,
            thumbnail.url,
            thumbnail.width,
            thumbnail.height
        )
    }
}