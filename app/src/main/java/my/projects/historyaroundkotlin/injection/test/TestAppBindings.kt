package my.projects.historyaroundkotlin.injection.test

import dagger.Module
import dagger.Provides
import my.projects.historyaroundkotlin.injection.navigation.NavControllerSource
import my.projects.historyaroundkotlin.injection.navigation.NavControllerSourceImpl
import my.projects.historyaroundkotlin.service.api.WikiSource
import my.projects.historyaroundkotlin.service.api.WikiSourceImpl
import my.projects.historyaroundkotlin.service.favourites.FavouritesSource
import my.projects.historyaroundkotlin.service.favourites.FavouritesSourceImpl
import my.projects.historyaroundkotlin.service.location.LocationSource
import my.projects.historyaroundkotlin.service.location.LocationSourceImpl
import my.projects.historyaroundkotlin.service.permission.PermissionSource
import my.projects.historyaroundkotlin.service.permission.PermissionSourceImpl
import javax.inject.Singleton

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
    open fun bindsFavouritesSource(favouritesSourceImpl: FavouritesSourceImpl): FavouritesSource {
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
}