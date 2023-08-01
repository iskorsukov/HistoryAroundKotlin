package com.iskorsukov.historyaround.injection.service

import com.iskorsukov.historyaround.service.api.WikiSource
import com.iskorsukov.historyaround.service.api.WikiSourceImpl
import com.iskorsukov.historyaround.service.favorites.FavoritesSource
import com.iskorsukov.historyaround.service.favorites.FavoritesSourceImpl
import com.iskorsukov.historyaround.service.permission.PermissionSource
import com.iskorsukov.historyaround.service.permission.PermissionSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelBindings {

    @Binds
    abstract fun bindsPermissionsSource(permissionSourceImpl: PermissionSourceImpl): PermissionSource

    @Binds
    abstract fun bindsFavouritesSource(favouritesSourceImpl: FavoritesSourceImpl): FavoritesSource

    @Binds
    abstract fun bindsWikiSource(wikiSourceImpl: WikiSourceImpl): WikiSource
}