package my.projects.historyaroundkotlin.presentation.view.favorites.adapter

import my.projects.historyaroundkotlin.databinding.HolderFavoriteItemBinding
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.view.common.adapter.BindViewHolder

class FavoriteItemHolder(dataBinding: HolderFavoriteItemBinding, listener: FavoritesListener): BindViewHolder<ArticleItem, HolderFavoriteItemBinding, FavoritesListener>(dataBinding, listener) {

    override fun bind(item: ArticleItem) {
        dataBinding.item = item
        dataBinding.executePendingBindings()
        itemView.setOnClickListener { listener.onItemSelected(item) }
    }

}