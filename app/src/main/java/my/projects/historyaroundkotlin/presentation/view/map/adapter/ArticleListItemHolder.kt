package my.projects.historyaroundkotlin.presentation.view.map.adapter

import my.projects.historyaroundkotlin.databinding.HolderArticleListItemBinding
import my.projects.historyaroundkotlin.presentation.view.common.adapter.BindViewHolder
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata.ArticleItemViewData

class ArticleListItemHolder(dataBinding: HolderArticleListItemBinding, listener: ArticleListItemListener):
    BindViewHolder<ArticleItemViewData, HolderArticleListItemBinding, ArticleListItemListener>(dataBinding, listener) {

    override fun bind(item: ArticleItemViewData) {
        dataBinding.item = item
        dataBinding.executePendingBindings()
        itemView.setOnClickListener{ listener.onItemSelected(item.item) }
    }
}