package my.projects.historyaroundkotlin.injection

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import my.projects.historyaroundkotlin.presentation.viewmodel.detail.DetailViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.favourites.FavouritesViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.map.MapViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.permission.PermissionViewModel

@Module
abstract class ViewModelBindings {

    @Binds
    @IntoMap
    @ViewModelKey(PermissionViewModel::class)
    abstract fun bindPermissionsViewModel(myViewModel: PermissionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun bindMapViewModel(myViewModel: MapViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun bindDetailsViewModel(myViewModel: DetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavouritesViewModel::class)
    abstract fun bindFavoritesViewModel(myViewModel: FavouritesViewModel): ViewModel
}