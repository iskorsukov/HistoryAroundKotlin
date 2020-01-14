package com.iskorsukov.historyaround.presentation.view.favorites.adapter

import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.presentation.view.common.adapter.ItemListener

interface FavoritesListener: ItemListener {
    fun onItemSelected(item: ArticleItem)
}