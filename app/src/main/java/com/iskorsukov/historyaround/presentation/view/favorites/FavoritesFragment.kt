package com.iskorsukov.historyaround.presentation.view.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.databinding.FragmentFavoritesBinding
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.presentation.view.common.fragment.BaseNavViewActionFragment
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.favorites.adapter.FavoritesAdapter
import com.iskorsukov.historyaround.presentation.view.favorites.viewaction.NavigateToDetailsAction
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.FavoritesErrorItem
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.viewdata.FavoritesViewData
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.PermissionErrorItem
import com.iskorsukov.historyaround.presentation.view.util.viewModelFactory
import com.iskorsukov.historyaround.presentation.viewmodel.favourites.FavouritesViewModel

class FavoritesFragment : BaseNavViewActionFragment() {

    private lateinit var viewModel: FavouritesViewModel

    private lateinit var contentBinding: FragmentFavoritesBinding

    override fun inflateContent(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        contentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        contentBinding.lifecycleOwner = this
        contentBinding.viewModel = viewModel
        return contentBinding.root
    }

    override fun titleRes(): Int {
        return R.string.favorites_fragment_title
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this, viewModelFactory())[FavouritesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) viewModel.loadFavoriteItems()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
        observeViewState()
    }

    private fun configureRecyclerView() {
        contentBinding.favoritesRecycler.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
    }

    private fun observeViewState() {
        viewModel.favouritesErrorLiveData.observe(viewLifecycleOwner, this::handleError)
        viewModel.favouritesActionLiveData.observe(viewLifecycleOwner, this::applyViewAction)
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

    private fun handleError(error: FavoritesErrorItem) {
        TODO("Show error dialog")
    }
}