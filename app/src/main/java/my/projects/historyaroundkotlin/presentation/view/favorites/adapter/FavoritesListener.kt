package my.projects.historyaroundkotlin.presentation.view.favorites.adapter

import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.view.common.adapter.ItemListener

interface FavoritesListener: ItemListener {
    fun onItemSelected(item: ArticleItem)
}