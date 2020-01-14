package com.iskorsukov.historyaround.injection.test

import dagger.Component
import com.iskorsukov.historyaround.injection.*
import com.iskorsukov.historyaround.injection.service.ApiModule
import com.iskorsukov.historyaround.injection.service.LocationModule
import com.iskorsukov.historyaround.injection.service.PermissionsModule
import com.iskorsukov.historyaround.injection.service.StorageModule
import com.iskorsukov.historyaround.injection.viewmodel.ViewModelFactoryModule
import javax.inject.Singleton

@Component(modules = [ApiModule::class, LocationModule::class, AppModule::class, TestAppBindings::class, PermissionsModule::class, TestViewModelBindings::class, ViewModelFactoryModule::class, StorageModule::class])
@Singleton
interface TestAppComponent: AppComponent