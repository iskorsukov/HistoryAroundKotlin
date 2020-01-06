package my.projects.historyaroundkotlin.presentation.view.map.adapter

import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.databinding.HolderArticleListItemBinding
import my.projects.historyaroundkotlin.presentation.view.common.adapter.BindRecyclerViewAdapter
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata.ArticleItemViewData

class ArticleListAdapter(items: List<ArticleItemViewData>, listener: ArticleListItemListener):
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