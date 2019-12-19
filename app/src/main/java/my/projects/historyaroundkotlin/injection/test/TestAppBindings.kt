package my.projects.historyaroundkotlin.injection.test

import dagger.Module
import dagger.Provides
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

// Override this to provide custom/mocked source classes
@Module
open class TestAppBindings {

    @Provides
    @Singleton
    open fun bindsLocationSource(locationSourceImpl: LocationSourceImpl): LocationSource {
        return locationSourceImpl
    }

    @Provides
    @Singleton
    open fun bindsPermissionsSource(permissionSourceImpl: PermissionSourceImpl): PermissionSource {
        return permissionSourceImpl
    }

    @Provides
    @Singleton
    open fun bindsFavouritesSource(favouritesSourceImpl: FavoritesSourceImpl): FavoritesSource {
        return favouritesSourceImpl
    }

    @Provides
    @Singleton
    open fun bindsWikiSource(wikiSourceImpl: WikiSourceImpl): WikiSource {
        return wikiSourceImpl
    }

    @Provides
    @Singleton
    open fun navControllerSource(navControllerSourceImpl: NavControllerSourceImpl): NavControllerSource {
        return navControllerSourceImpl
    }

    @Provides
    @Singleton
    open fun preferencesSource(preferencesSourceImpl: PreferencesSourceImpl): PreferencesSource {
        return preferencesSourceImpl
    }
}