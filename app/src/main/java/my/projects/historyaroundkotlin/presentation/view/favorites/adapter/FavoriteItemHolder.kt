package my.projects.historyaroundkotlin.presentation.view.favorites.adapter

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.holder_favorite_item.view.*
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.common.adapter.BindViewHolder

class FavoriteItemHolder(view: View, listener: FavoritesListener): BindViewHolder<ArticleItem, FavoritesListener>(view, listener) {

    override fun bind(item: ArticleItem) {
        itemView.apply {
            favoriteItemTitle.text = item.title
            favoriteItemDescription.text = item.description
            item.thumbnail?.apply {
                Glide.with(itemView).load(this.url).into(favoriteItemThumbnail)
            }
            setOnClickListener { listener.onItemSelected(item) }
        }
    }

}