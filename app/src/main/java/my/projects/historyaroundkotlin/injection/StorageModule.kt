package my.projects.historyaroundkotlin.injection

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import my.projects.historyaroundkotlin.service.storage.AppDatabase
import javax.inject.Singleton

@Module
class StorageModule {

    @Provides
    @Singleton
    fun providesAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "favorites-db"
        ).build()
    }
}