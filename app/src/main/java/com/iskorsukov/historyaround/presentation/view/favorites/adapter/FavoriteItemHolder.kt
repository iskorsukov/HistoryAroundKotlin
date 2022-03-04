package com.iskorsukov.historyaround.presentation.view.favorites.adapter

import com.iskorsukov.historyaround.databinding.HolderFavoriteItemBinding
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.presentation.view.common.adapter.BindViewHolder

class FavoriteItemHolder(dataBinding: HolderFavoriteItemBinding, listener: FavoritesListener?): BindViewHolder<ArticleItem, HolderFavoriteItemBinding, FavoritesListener>(dataBinding, listener) {

    override fun bind(item: ArticleItem) {
        dataBinding.item = item
        dataBinding.executePendingBindings()
        itemView.setOnClickListener { listener?.onItemSelected(item) }
    }

}