package my.projects.historyaroundkotlin.presentation.view.map.adapter

import android.view.View
import com.bumptech.glide.Glide
import my.projects.historyaroundkotlin.presentation.common.adapter.BindViewHolder
import kotlinx.android.synthetic.main.holder_article_list_item.view.*
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata.ArticleItemViewData

class ArticleListItemHolder(view: View, listener: ArticleListItemListener): BindViewHolder<ArticleItemViewData, ArticleListItemListener>(view, listener) {

    override fun bind(item: ArticleItemViewData) {
        itemView.apply {
            articleItemTitle.text = item.item.title
            item.item.thumbnail?.apply {
                Glide.with(itemView).load(this.url).into(articleItemImage)
            }
            setOnClickListener { listener.onItemSelected(item.item) }
        }
    }
}