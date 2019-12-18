package my.projects.historyaroundkotlin.presentation.view.map.adapter

import android.view.View
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.common.adapter.BindRecyclerViewAdapter
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata.ArticleItemViewData

class ArticleListAdapter(items: List<ArticleItemViewData>, listener: ArticleListItemListener): BindRecyclerViewAdapter<ArticleItemViewData, ArticleListItemListener, ArticleListItemHolder>(items, listener) {

    init {
        setHasStableIds(true)
    }

    override fun layoutRes(): Int {
        return R.layout.holder_article_list_item
    }

    override fun getViewHolder(view: View): ArticleListItemHolder {
        return ArticleListItemHolder(view, listener)
    }

    override fun getItemId(position: Int): Long {
        return items[position].item.pageid.toLong()
    }
}