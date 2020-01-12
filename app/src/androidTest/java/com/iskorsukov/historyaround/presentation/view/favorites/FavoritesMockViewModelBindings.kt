package com.iskorsukov.historyaround.presentation.view.favorites

import androidx.lifecycle.ViewModel
import com.iskorsukov.historyaround.injection.test.TestViewModelBindings
import com.iskorsukov.historyaround.presentation.viewmodel.favourites.FavouritesViewModel

class FavoritesMockViewModelBindings(private val viewModel: FavouritesViewModel): TestViewModelBindings() {
    override fun bindFavoritesViewModel(myViewModel: FavouritesViewModel): ViewModel {
        return viewModel
    }
}