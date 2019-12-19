package my.projects.historyaroundkotlin.injection

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import my.projects.historyaroundkotlin.presentation.viewmodel.detail.DetailFlowViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.favourites.FavouritesViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.map.MapFlowViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.permission.PermissionViewModel
import javax.inject.Singleton

@Module
abstract class ViewModelBindings {

    @Binds
    @IntoMap
    @ViewModelKey(PermissionViewModel::class)
    abstract fun bindPermissionsViewModel(myViewModel: PermissionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapFlowViewModel::class)
    abstract fun bindMapViewModel(myViewModel: MapFlowViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailFlowViewModel::class)
    abstract fun bindDetailsViewModel(myViewModel: DetailFlowViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavouritesViewModel::class)
    abstract fun bindFavoritesViewModel(myViewModel: FavouritesViewModel): ViewModel
}