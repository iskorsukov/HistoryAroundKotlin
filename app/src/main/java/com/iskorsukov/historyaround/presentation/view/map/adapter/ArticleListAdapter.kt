package com.iskorsukov.historyaround.presentation.view.map.adapter

import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.databinding.HolderArticleListItemBinding
import com.iskorsukov.historyaround.presentation.view.common.adapter.BindRecyclerViewAdapter
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticleItemViewData

class ArticleListAdapter(items: List<ArticleItemViewData>, listener: ArticleListItemListener?):
    BindRecyclerViewAdapter<ArticleItemViewData, HolderArticleListItemBinding, ArticleListItemListener, ArticleListItemHolder>(items, listener) {

    init {
        setHasStableIds(true)
    }

    override fun layoutRes(): Int {
        return R.layout.holder_article_list_item
    }

    override fun getViewHolder(dataBinding: HolderArticleListItemBinding): ArticleListItemHolder {
        return ArticleListItemHolder(dataBinding, listener)
    }

    override fun getItemId(position: Int): Long {
        return items[position].item.pageid.toLong()
    }
}