package my.projects.historyaroundkotlin.presentation.view.favorites.adapter

import android.view.View
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.common.adapter.BindRecyclerViewAdapter

class FavoritesAdapter(items: List<ArticleItem>, listener: FavoritesListener): BindRecyclerViewAdapter<ArticleItem, FavoritesListener, FavoriteItemHolder>(items, listener) {
    override fun layoutRes(): Int {
        return R.layout.holder_favorite_item
    }

    override fun getViewHolder(view: View): FavoriteItemHolder {
        return FavoriteItemHolder(
            view,
            listener
        )
    }
}