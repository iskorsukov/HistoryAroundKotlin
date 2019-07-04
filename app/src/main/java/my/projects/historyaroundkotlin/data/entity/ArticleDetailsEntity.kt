package my.projects.historyaroundkotlin.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = ArticleThumbnailEntity::class,
    parentColumns = arrayOf("thumbnailId"),
    childColumns = arrayOf("thumbnailId"))],
    tableName = "details"
)
data class ArticleDetailsEntity(
    @PrimaryKey val pageid: Long,
    val title: String,
    val extract: String,
    val thumbnailId: Long?,
    val url: String
)