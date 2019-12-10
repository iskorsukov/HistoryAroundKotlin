package my.projects.historyaroundkotlin.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = ArticleThumbnailEntity::class,
    parentColumns = arrayOf("thumbnailId"),
    childColumns = arrayOf("thumbnailId"),
    onDelete = ForeignKey.CASCADE)],
    tableName = "articles"
)
data class ArticleEntity(
    @PrimaryKey val pageid: Long,
    val title: String,
    val description: String,
    val lat: Double,
    val lon: Double,
    val thumbnailId: Long?
)