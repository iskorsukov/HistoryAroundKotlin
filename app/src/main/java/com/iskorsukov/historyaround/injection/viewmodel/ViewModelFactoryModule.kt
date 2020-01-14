package com.iskorsukov.historyaround.injection.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.iskorsukov.historyaround.injection.viewmodel.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindsViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory
}