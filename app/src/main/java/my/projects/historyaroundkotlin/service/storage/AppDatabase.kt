package my.projects.historyaroundkotlin.service.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import my.projects.historyaroundkotlin.data.entity.ArticleEntity
import my.projects.historyaroundkotlin.data.entity.ArticleThumbnailEntity
import my.projects.historyaroundkotlin.service.favorites.FavouritesDao

@Database(entities = arrayOf(ArticleEntity::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getFavouritesDao(): FavouritesDao
}