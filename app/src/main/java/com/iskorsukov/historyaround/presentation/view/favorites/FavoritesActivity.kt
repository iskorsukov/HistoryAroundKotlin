package com.iskorsukov.historyaround.presentation.view.favorites

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.iskorsukov.historyaround.databinding.ActivityFavoritesBinding
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.presentation.view.common.error.ErrorDialog
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.detail.DetailActivity
import com.iskorsukov.historyaround.presentation.view.favorites.viewaction.NavigateToDetailsAction
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.FavoritesErrorItem
import com.iskorsukov.historyaround.presentation.viewmodel.favourites.FavouritesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesActivity: AppCompatActivity() {

    private lateinit var contentBinding: ActivityFavoritesBinding

    private val viewModel: FavouritesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentBinding = ActivityFavoritesBinding.inflate(layoutInflater)
        contentBinding.lifecycleOwner = this
        contentBinding.viewModel = viewModel
        setContentView(contentBinding.root)

        if (savedInstanceState == null) viewModel.loadFavoriteItems()

        configureRecyclerView()
        observeViewState()
    }

    private fun configureRecyclerView() {
        contentBinding.favoritesRecycler.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
    }

    private fun observeViewState() {
        viewModel.favouritesErrorLiveEvent.observe(this, this::handleError)
        viewModel.favouritesActionLiveEvent.observe(this, this::applyViewAction)
    }

    private fun applyViewAction(viewAction: ViewAction<*>) {
        when (viewAction) {
            is NavigateToDetailsAction -> navigateToDetails(viewAction.data!!)
        }
    }

    private fun navigateToDetails(articleItem: ArticleItem) {
        val intent = DetailActivity.getIntent(
            this,
            articleItem.pageid,
            articleItem.languageCode
        )
        startActivity(intent)
    }

    private fun handleError(error: FavoritesErrorItem) {
        val dialog = ErrorDialog.newInstance(error)
        val listener = object : ErrorDialog.ErrorDialogListener {
            override fun onActionClick() {
                dialog.dismiss()
                viewModel.loadFavoriteItems()
            }

            override fun onCancelClick() {
                dialog.dismiss()
                finish()
            }
        }
        dialog.listener = listener
        dialog.show(supportFragmentManager, ErrorDialog.TAG)
    }
}