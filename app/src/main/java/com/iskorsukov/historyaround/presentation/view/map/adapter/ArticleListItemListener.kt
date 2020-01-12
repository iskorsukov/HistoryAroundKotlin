package com.iskorsukov.historyaround.presentation.view.map.adapter

import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.presentation.view.common.adapter.ItemListener

interface ArticleListItemListener: ItemListener {
    fun onItemSelected(articleItem: ArticleItem)
}