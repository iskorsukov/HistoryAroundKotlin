package com.iskorsukov.historyaround.presentation.view.favorites

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.databinding.FragmentFavoritesBinding
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.presentation.view.common.fragment.BaseLCEViewStateActionFragment
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.favorites.adapter.FavoritesAdapter
import com.iskorsukov.historyaround.presentation.view.favorites.viewaction.NavigateToDetailsAction
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.FavoritesErrorItem
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.FavoritesLoadingItem
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.viewdata.FavoritesViewData
import com.iskorsukov.historyaround.presentation.viewmodel.favourites.FavouritesViewModel

class FavoritesFragment : BaseLCEViewStateActionFragment<FavoritesLoadingItem, FavoritesViewData, FavoritesErrorItem, FavouritesViewModel, FragmentFavoritesBinding>() {

    override fun viewModelClass(): Class<FavouritesViewModel> {
        return FavouritesViewModel::class.java
    }

    override fun contentLayout(): Int {
        return R.layout.fragment_favorites
    }

    override fun titleRes(): Int {
        return R.string.favorites_fragment_title
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
        initViewModel()
        observeViewState()
    }

    private fun configureRecyclerView() {
        contentBinding.favoritesRecycler.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
    }

    private fun observeViewState() {
        viewModel.viewStateLiveData.observe(viewLifecycleOwner, Observer {
            applyViewState(it)
        })
        viewModel.viewActionLiveData.observe(viewLifecycleOwner, Observer {
            applyViewAction(it)
        })
    }

    override fun applyViewAction(viewAction: ViewAction<*>) {
        when (viewAction) {
            is NavigateToDetailsAction -> navigateToDetails(viewAction.data!!)
        }
    }

    private fun navigateToDetails(articleItem: ArticleItem) {
        navController().navigate(
            FavoritesFragmentDirections.actionFavoritesFragmentToDetailFragment(
                articleItem.pageid,
                articleItem.languageCode
            )
        )
    }

    override fun showContent(content: FavoritesViewData) {
        contentBinding.favoritesRecycler.adapter =
            FavoritesAdapter(
                content.items,
                viewModel
            )
    }

    override fun onErrorRetry() {
        viewModel.onRetry()
    }

}