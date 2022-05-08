package com.iskorsukov.historyaround.injection.service

import com.iskorsukov.historyaround.service.api.WikiSource
import com.iskorsukov.historyaround.service.api.WikiSourceImpl
import com.iskorsukov.historyaround.service.favorites.FavoritesSource
import com.iskorsukov.historyaround.service.favorites.FavoritesSourceImpl
import com.iskorsukov.historyaround.service.location.LocationSource
import com.iskorsukov.historyaround.service.location.LocationSourceImpl
import com.iskorsukov.historyaround.service.navigation.NavControllerSource
import com.iskorsukov.historyaround.service.navigation.NavControllerSourceImpl
import com.iskorsukov.historyaround.service.permission.PermissionSource
import com.iskorsukov.historyaround.service.permission.PermissionSourceImpl
import com.iskorsukov.historyaround.service.preferences.PreferencesSource
import com.iskorsukov.historyaround.service.preferences.PreferencesSourceImpl
import dagger.Binds
import dagger.Module
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