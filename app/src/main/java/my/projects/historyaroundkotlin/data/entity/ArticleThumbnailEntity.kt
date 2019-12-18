package my.projects.historyaroundkotlin.data.entity

import androidx.room.PrimaryKey

data class ArticleThumbnailEntity(
    @PrimaryKey(autoGenerate = true) val thumbnailId: Long?,
    val thumbnailUrl: String,
    val thumbnailWidth: Int,
    val thumbnailHeight: Int
)