package my.projects.historyaroundkotlin.mapper.entity

import my.projects.historyaroundkotlin.data.entity.ArticleEntity
import my.projects.historyaroundkotlin.data.entity.ArticleThumbnailEntity
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.model.common.ArticleThumbnail
import javax.inject.Inject

class ArticleEntitiesMapper @Inject constructor() {

    fun mapArticleEntity(articleEntity: ArticleEntity): ArticleItem {
        return ArticleItem(
            articleEntity.pageid.toString(),
            articleEntity.title,
            articleEntity.description,
            articleEntity.lat to articleEntity.lon,
            articleEntity.thumbnail?.run {
                mapThumbnailEntity(this)
            }
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
            articleItem.thumbnail?.run { mapThumbnailToEntity(this) }
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