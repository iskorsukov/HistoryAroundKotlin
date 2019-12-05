package my.projects.historyaroundkotlin.mapper.entity

import my.projects.historyaroundkotlin.data.entity.ArticleDetailsEntity
import my.projects.historyaroundkotlin.data.entity.ArticleEntity
import my.projects.historyaroundkotlin.data.entity.ArticleThumbnailEntity
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.model.common.ArticleThumbnail
import my.projects.historyaroundkotlin.model.detail.ArticleDetails

class ArticleEntitiesMapper {

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

    fun mapArticleDetailsEntity(articleDetailsEntity: ArticleDetailsEntity, thumbnailEntity: ArticleThumbnailEntity?): ArticleDetails {
        return ArticleDetails(
            articleDetailsEntity.pageid,
            articleDetailsEntity.title,
            articleDetailsEntity.extract,
            thumbnailEntity?.run {
                mapThumbnailEntity(this)
            },
            articleDetailsEntity.lat to articleDetailsEntity.lon,
            articleDetailsEntity.url
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

    fun mapDetailsToEntity(details: ArticleDetails): ArticleDetailsEntity {
        return ArticleDetailsEntity(
            details.pageid,
            details.title,
            details.extract,
            null,
            details.coordinates.first,
            details.coordinates.second,
            details.url
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