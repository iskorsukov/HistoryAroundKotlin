package my.projects.historyaroundkotlin.presentation.view.favorites

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_favorites.*
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.databinding.FragmentFavoritesBinding
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.view.common.fragment.BaseLCEViewStateActionFragment
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewaction.ViewAction
import my.projects.historyaroundkotlin.presentation.view.favorites.adapter.FavoritesAdapter
import my.projects.historyaroundkotlin.presentation.view.favorites.viewaction.NavigateToDetailsAction
import my.projects.historyaroundkotlin.presentation.view.favorites.viewstate.FavoritesErrorItem
import my.projects.historyaroundkotlin.presentation.view.favorites.viewstate.FavoritesLoadingItem
import my.projects.historyaroundkotlin.presentation.view.favorites.viewstate.viewdata.FavoritesViewData
import my.projects.historyaroundkotlin.presentation.view.util.viewModelFactory
import my.projects.historyaroundkotlin.presentation.viewmodel.favourites.FavouritesViewModel

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
        favoritesRecycler.addItemDecoration(DividerItemDecoration(context!!, RecyclerView.VERTICAL))
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
        favoritesRecycler.adapter =
            FavoritesAdapter(
                content.items,
                viewModel
            )
    }

    override fun onErrorRetry() {
        viewModel.onRetry()
    }

}