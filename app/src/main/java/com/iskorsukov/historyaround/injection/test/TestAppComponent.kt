package com.iskorsukov.historyaround.injection.test

import dagger.Component
import com.iskorsukov.historyaround.injection.*
import javax.inject.Singleton

@Component(modules = [ApiModule::class, LocationModule::class, AppModule::class, TestAppBindings::class, PermissionsModule::class, TestViewModelBindings::class, ViewModelFactoryModule::class, StorageModule::class])
@Singleton
interface TestAppComponent: AppComponent