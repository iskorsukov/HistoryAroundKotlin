package my.projects.historyaroundkotlin.presentation.view.common.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BindViewHolder<T, B: ViewDataBinding, L: ItemListener>(protected val dataBinding: B, protected val listener: L): RecyclerView.ViewHolder(dataBinding.root) {
    abstract fun bind(item: T)
}