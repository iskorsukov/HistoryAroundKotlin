package my.projects.historyaroundkotlin.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val pageid: Long,
    val title: String,
    val description: String,
    val lat: Double,
    val lon: Double,
    @Embedded val thumbnail: ArticleThumbnailEntity?
)