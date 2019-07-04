package my.projects.historyaroundkotlin.injection

import dagger.Component
import my.projects.historyaroundkotlin.presentation.viewmodel.detail.DetailViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.favourites.FavouritesViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.map.MapViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.permission.PermissionViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.splash.SplashViewModel
import my.projects.historyaroundkotlin.service.api.WikiApi

@Component(modules = arrayOf(ApiModule::class, LocationModule::class))
public interface AppComponent {
    fun inject(viewModel: DetailViewModel)
    fun inject(viewModel: SplashViewModel)
    fun inject(viewModel: MapViewModel)
    fun inject(viewModel: PermissionViewModel)
    fun inject(viewModel: FavouritesViewModel)
}