package com.iskorsukov.historyaround.presentation.view.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BindRecyclerViewAdapter<T, B: ViewDataBinding, L: ItemListener, VH: BindViewHolder<T, B, L>>(val items: List<T>, protected val listener: L): RecyclerView.Adapter<VH>() {

    @LayoutRes
    abstract fun layoutRes(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return getViewHolder(DataBindingUtil.inflate( LayoutInflater.from(parent.context), layoutRes(), parent, false))
    }

    abstract fun getViewHolder(dataBinding: B): VH

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }
}