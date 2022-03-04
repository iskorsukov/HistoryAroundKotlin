package com.iskorsukov.historyaround.presentation.view.map.adapter

import com.iskorsukov.historyaround.databinding.HolderArticleListItemBinding
import com.iskorsukov.historyaround.presentation.view.common.adapter.BindViewHolder
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticleItemViewData

class ArticleListItemHolder(dataBinding: HolderArticleListItemBinding, listener: ArticleListItemListener?):
    BindViewHolder<ArticleItemViewData, HolderArticleListItemBinding, ArticleListItemListener>(dataBinding, listener) {

    override fun bind(item: ArticleItemViewData) {
        dataBinding.item = item
        dataBinding.executePendingBindings()
        itemView.setOnClickListener{ listener?.onItemSelected(item.item) }
    }
}