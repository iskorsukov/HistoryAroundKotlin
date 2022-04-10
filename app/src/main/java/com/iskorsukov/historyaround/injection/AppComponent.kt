package com.iskorsukov.historyaround.injection

import androidx.lifecycle.ViewModelProvider
import com.iskorsukov.historyaround.injection.service.*
import com.iskorsukov.historyaround.injection.viewmodel.ViewModelBindings
import com.iskorsukov.historyaround.injection.viewmodel.ViewModelFactoryModule
import com.iskorsukov.historyaround.presentation.view.MainActivity
import com.iskorsukov.historyaround.service.navigation.NavControllerSource
import com.iskorsukov.historyaround.service.preferences.PreferencesSource
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    ApiModule::class,
    LocationModule::class,
    AppModule::class,
    AppBindings::class,
    PermissionsModule::class,
    ViewModelFactoryModule::class,
    ViewModelBindings::class,
    StorageModule::class
])
@Singleton
interface AppComponent {
    fun viewModelFactory(): ViewModelProvider.Factory
    fun navControllerSource(): NavControllerSource
    fun preferencesSource(): PreferencesSource

    fun inject(mainActivity: MainActivity)
}