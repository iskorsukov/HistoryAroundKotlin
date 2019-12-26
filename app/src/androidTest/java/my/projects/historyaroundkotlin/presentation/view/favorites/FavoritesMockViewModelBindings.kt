package my.projects.historyaroundkotlin.presentation.view.favorites

import androidx.lifecycle.ViewModel
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.presentation.viewmodel.favourites.FavouritesViewModel

class FavoritesMockViewModelBindings(private val viewModel: FavouritesViewModel): TestViewModelBindings() {
    override fun bindFavoritesViewModel(myViewModel: FavouritesViewModel): ViewModel {
        return viewModel
    }
}