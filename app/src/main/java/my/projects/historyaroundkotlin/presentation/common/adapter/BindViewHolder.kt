package my.projects.historyaroundkotlin.presentation.common.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BindViewHolder<T, L: ItemListener>(view: View, protected val listener: L): RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}