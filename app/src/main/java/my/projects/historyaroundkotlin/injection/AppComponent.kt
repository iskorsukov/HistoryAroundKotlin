package my.projects.historyaroundkotlin.injection

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import my.projects.historyaroundkotlin.injection.navigation.NavControllerSource
import my.projects.historyaroundkotlin.presentation.view.MainActivity
import javax.inject.Singleton

@Component(modules = [ApiModule::class, LocationModule::class, AppModule::class, AppBindings::class, PermissionsModule::class, ViewModelFactoryModule::class, ViewModelBindings::class])
@Singleton
interface AppComponent {
    fun viewModelFactory(): ViewModelProvider.Factory
    fun navControllerSource(): NavControllerSource

    fun inject(mainActivity: MainActivity)
}