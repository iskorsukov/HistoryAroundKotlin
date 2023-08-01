package com.iskorsukov.historyaround.injection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @DispatcherIO
    fun providesDispatcherIO(): CoroutineDispatcher {
        return Dispatchers.IO
    }

}