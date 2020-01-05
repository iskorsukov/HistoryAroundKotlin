package my.projects.historyaroundkotlin.injection.test

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import my.projects.historyaroundkotlin.injection.ViewModelKey
import my.projects.historyaroundkotlin.presentation.viewmodel.detail.DetailViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.favourites.FavouritesViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.map.MapViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.permission.PermissionViewModel

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