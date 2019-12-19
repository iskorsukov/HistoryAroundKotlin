package my.projects.historyaroundkotlin.injection

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import my.projects.historyaroundkotlin.presentation.view.MainActivity
import my.projects.historyaroundkotlin.service.navigation.NavControllerSource
import my.projects.historyaroundkotlin.service.preferences.PreferencesSource
import javax.inject.Singleton

@Component(modules = [ApiModule::class, LocationModule::class, AppModule::class, AppBindings::class, PermissionsModule::class, ViewModelFactoryModule::class, ViewModelBindings::class, StorageModule::class])
@Singleton
interface AppComponent {
    fun viewModelFactory(): ViewModelProvider.Factory
    fun navControllerSource(): NavControllerSource
    fun preferencesSource(): PreferencesSource

    fun inject(mainActivity: MainActivity)
}