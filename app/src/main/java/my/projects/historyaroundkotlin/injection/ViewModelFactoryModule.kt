package my.projects.historyaroundkotlin.injection

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindsViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory
}