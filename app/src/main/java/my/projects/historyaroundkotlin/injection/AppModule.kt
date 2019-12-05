package my.projects.historyaroundkotlin.injection

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import my.projects.historyaroundkotlin.service.api.WikiSource
import my.projects.historyaroundkotlin.service.api.WikiSourceImpl
import my.projects.historyaroundkotlin.service.favourites.FavouritesSource
import my.projects.historyaroundkotlin.service.favourites.FavouritesSourceImpl
import my.projects.historyaroundkotlin.service.location.LocationSource
import my.projects.historyaroundkotlin.service.location.LocationSourceImpl
import my.projects.historyaroundkotlin.service.permission.PermissionSource
import my.projects.historyaroundkotlin.service.permission.PermissionSourceImpl

@Module
class AppModule(private val appContext: Context) {

    @Provides
    fun providesAppContext(): Context {
        return appContext
    }
}