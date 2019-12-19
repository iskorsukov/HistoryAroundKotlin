package my.projects.historyaroundkotlin.injection

import dagger.Binds
import dagger.Module
import my.projects.historyaroundkotlin.service.navigation.NavControllerSource
import my.projects.historyaroundkotlin.service.navigation.NavControllerSourceImpl
import my.projects.historyaroundkotlin.service.api.WikiSource
import my.projects.historyaroundkotlin.service.api.WikiSourceImpl
import my.projects.historyaroundkotlin.service.favorites.FavoritesSource
import my.projects.historyaroundkotlin.service.favorites.FavoritesSourceImpl
import my.projects.historyaroundkotlin.service.location.LocationSource
import my.projects.historyaroundkotlin.service.location.LocationSourceImpl
import my.projects.historyaroundkotlin.service.permission.PermissionSource
import my.projects.historyaroundkotlin.service.permission.PermissionSourceImpl
import my.projects.historyaroundkotlin.service.preferences.PreferencesSource
import my.projects.historyaroundkotlin.service.preferences.PreferencesSourceImpl
import javax.inject.Singleton

@Module
abstract class AppBindings {
    @Binds
    @Singleton
    abstract fun bindsLocationSource(locationSourceImpl: LocationSourceImpl): LocationSource

    @Binds
    @Singleton
    abstract fun bindsPermissionsSource(permissionSourceImpl: PermissionSourceImpl): PermissionSource

    @Binds
    @Singleton
    abstract fun bindsFavouritesSource(favouritesSourceImpl: FavoritesSourceImpl): FavoritesSource

    @Binds
    @Singleton
    abstract fun bindsWikiSource(wikiSourceImpl: WikiSourceImpl): WikiSource

    @Binds
    @Singleton
    abstract fun bindsNavControllerSource(navControllerSourceImpl: NavControllerSourceImpl): NavControllerSource

    @Binds
    @Singleton
    abstract fun bindsPreferencesSource(preferencesSourceImpl: PreferencesSourceImpl): PreferencesSource
}