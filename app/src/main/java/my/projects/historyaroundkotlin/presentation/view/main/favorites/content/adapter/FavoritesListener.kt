package my.projects.historyaroundkotlin.presentation.view.main.favorites.content.adapter

import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.common.adapter.ItemListener

interface FavoritesListener: ItemListener {
    fun onItemSelected(item: ArticleItem)
}