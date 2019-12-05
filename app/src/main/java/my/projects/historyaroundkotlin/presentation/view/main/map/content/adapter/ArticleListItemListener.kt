package my.projects.historyaroundkotlin.presentation.view.main.map.content.adapter

import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.common.adapter.ItemListener

interface ArticleListItemListener: ItemListener {
    fun onItemSelected(articleItem: ArticleItem)
}