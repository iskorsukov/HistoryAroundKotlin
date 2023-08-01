package com.iskorsukov.historyaround.injection.service

import com.iskorsukov.historyaround.service.location.LocationSource
import com.iskorsukov.historyaround.service.location.LocationSourceImpl
import com.iskorsukov.historyaround.service.preferences.PreferencesSource
import com.iskorsukov.historyaround.service.preferences.PreferencesSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindings {

    @Binds
    @Singleton
    abstract fun bindsLocationSource(locationSourceImpl: LocationSourceImpl): LocationSource

    @Binds
    @Singleton
    abstract fun bindsPreferencesSource(preferencesSourceImpl: PreferencesSourceImpl): PreferencesSource
}