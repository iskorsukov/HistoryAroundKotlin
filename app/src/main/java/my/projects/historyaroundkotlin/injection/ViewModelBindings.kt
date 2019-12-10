package my.projects.historyaroundkotlin.injection

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import my.projects.historyaroundkotlin.presentation.viewmodel.main.detail.DetailFlowViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.main.favourites.FavouritesViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.main.map.MapFlowViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.start.permission.PermissionFlowViewModel
import javax.inject.Singleton

@Module
abstract class ViewModelBindings {

    @Binds
    @IntoMap
    @ViewModelKey(PermissionFlowViewModel::class)
    abstract fun bindPermissionsViewModel(myViewModel: PermissionFlowViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapFlowViewModel::class)
    abstract fun bindMapViewModel(myViewModel: MapFlowViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailFlowViewModel::class)
    @Singleton
    abstract fun bindDetailsViewModel(myViewModel: DetailFlowViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavouritesViewModel::class)
    abstract fun bindFavoritesViewModel(myViewModel: FavouritesViewModel): ViewModel
}