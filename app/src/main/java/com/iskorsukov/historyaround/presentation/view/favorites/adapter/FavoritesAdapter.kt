package com.iskorsukov.historyaround.presentation.view.favorites.adapter

import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.databinding.HolderFavoriteItemBinding
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.presentation.view.common.adapter.BindRecyclerViewAdapter

class FavoritesAdapter(items: List<ArticleItem>, listener: FavoritesListener):
    BindRecyclerViewAdapter<ArticleItem, HolderFavoriteItemBinding, FavoritesListener, FavoriteItemHolder>(items, listener) {
    override fun layoutRes(): Int {
        return R.layout.holder_favorite_item
    }

    override fun getViewHolder(dataBinding: HolderFavoriteItemBinding): FavoriteItemHolder {
        return FavoriteItemHolder(dataBinding, listener)
    }
}