package my.projects.historyaroundkotlin.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "thumbnails")
data class ArticleThumbnailEntity(
    @PrimaryKey(autoGenerate = true) val thumbnailId: Long,
    val url: String,
    val width: Int,
    val height: Int
)