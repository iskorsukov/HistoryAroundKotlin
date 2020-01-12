package com.iskorsukov.historyaround.injection.test

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.iskorsukov.historyaround.injection.ViewModelKey
import com.iskorsukov.historyaround.presentation.viewmodel.detail.DetailViewModel
import com.iskorsukov.historyaround.presentation.viewmodel.favourites.FavouritesViewModel
import com.iskorsukov.historyaround.presentation.viewmodel.map.MapViewModel
import com.iskorsukov.historyaround.presentation.viewmodel.permission.PermissionViewModel

// Override this to provide custom/mock viewmodels
@Module
open class TestViewModelBindings {

    @Provides
    @IntoMap
    @ViewModelKey(PermissionViewModel::class)
    open fun bindPermissionsViewModel(myViewModel: PermissionViewModel): ViewModel {
        return myViewModel
    }

    @Provides
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    open fun bindMapViewModel(myViewModel: MapViewModel): ViewModel {
        return myViewModel
    }

    @Provides
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    open fun bindDetailsViewModel(myViewModel: DetailViewModel): ViewModel {
        return myViewModel
    }

    @Provides
    @IntoMap
    @ViewModelKey(FavouritesViewModel::class)
    open fun bindFavoritesViewModel(myViewModel: FavouritesViewModel): ViewModel {
        return myViewModel
    }
}