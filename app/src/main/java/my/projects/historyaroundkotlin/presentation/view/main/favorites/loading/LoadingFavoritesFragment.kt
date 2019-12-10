package my.projects.historyaroundkotlin.presentation.view.main.favorites.loading

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.argument.FavoritesArgument
import my.projects.historyaroundkotlin.presentation.view.common.BaseLoadingFragment
import my.projects.historyaroundkotlin.presentation.view.util.viewModelFactory
import my.projects.historyaroundkotlin.presentation.viewmodel.main.favourites.FavouritesViewModel
import my.projects.historyaroundkotlin.presentation.viewstate.main.favorites.FavoritesErrorStatus
import my.projects.historyaroundkotlin.presentation.viewstate.main.favorites.FavoritesViewState

class LoadingFavoritesFragment: BaseLoadingFragment<FavoritesErrorStatus, FavoritesViewState>() {

    private lateinit var viewModel: FavouritesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        loadFavoriteItems()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory())[FavouritesViewModel::class.java]
    }

    private fun loadFavoriteItems() {
        viewModel.loadFavoriteItems().observe(this, Observer {
            applyViewState(it)
        })
    }

    override fun applyErrorState(errors: List<FavoritesErrorStatus>) {
        navController().navigate(LoadingFavoritesFragmentDirections.actionLoadingFavoritesFragmentToErrorFavoritesFragment())
    }

    override fun applyContentState(viewState: FavoritesViewState) {
        navController().navigate(LoadingFavoritesFragmentDirections.actionLoadingFavoritesFragmentToFavouritesFragment(
            FavoritesArgument(ArrayList(viewState.favoriteItems!!))
        ))
    }

    override fun fragmentLayout(): Int {
        return R.layout.fragment_loading_favorites
    }

}