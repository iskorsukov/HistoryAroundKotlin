package my.projects.historyaroundkotlin.presentation.view.favorites.adapter

import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.databinding.HolderFavoriteItemBinding
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.view.common.adapter.BindRecyclerViewAdapter

class FavoritesAdapter(items: List<ArticleItem>, listener: FavoritesListener):
    BindRecyclerViewAdapter<ArticleItem, HolderFavoriteItemBinding, FavoritesListener, FavoriteItemHolder>(items, listener) {
    override fun layoutRes(): Int {
        return R.layout.holder_favorite_item
    }

    override fun getViewHolder(dataBinding: HolderFavoriteItemBinding): FavoriteItemHolder {
        return FavoriteItemHolder(dataBinding, listener)
    }
}