package my.projects.historyaroundkotlin.injection.test

import dagger.Component
import my.projects.historyaroundkotlin.injection.*
import javax.inject.Singleton

@Component(modules = [ApiModule::class, LocationModule::class, AppModule::class, TestAppBindings::class, PermissionsModule::class, TestViewModelBindings::class, ViewModelFactoryModule::class, StorageModule::class])
@Singleton
interface TestAppComponent: AppComponent