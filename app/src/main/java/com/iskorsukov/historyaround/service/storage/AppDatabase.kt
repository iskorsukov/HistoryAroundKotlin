package com.iskorsukov.historyaround.service.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iskorsukov.historyaround.data.entity.ArticleEntity
import com.iskorsukov.historyaround.service.favorites.FavouritesDao

@Database(entities = [ArticleEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getFavouritesDao(): FavouritesDao
}