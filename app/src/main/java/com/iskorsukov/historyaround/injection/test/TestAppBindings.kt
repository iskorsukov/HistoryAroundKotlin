package com.iskorsukov.historyaround.injection.test

import dagger.Module
import dagger.Provides
import com.iskorsukov.historyaround.service.navigation.NavControllerSource
import com.iskorsukov.historyaround.service.navigation.NavControllerSourceImpl
import com.iskorsukov.historyaround.service.api.WikiSource
import com.iskorsukov.historyaround.service.api.WikiSourceImpl
import com.iskorsukov.historyaround.service.favorites.FavoritesSource
import com.iskorsukov.historyaround.service.favorites.FavoritesSourceImpl
import com.iskorsukov.historyaround.service.location.LocationSource
import com.iskorsukov.historyaround.service.location.LocationSourceImpl
import com.iskorsukov.historyaround.service.permission.PermissionSource
import com.iskorsukov.historyaround.service.permission.PermissionSourceImpl
import com.iskorsukov.historyaround.service.preferences.PreferencesSource
import com.iskorsukov.historyaround.service.preferences.PreferencesSourceImpl
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