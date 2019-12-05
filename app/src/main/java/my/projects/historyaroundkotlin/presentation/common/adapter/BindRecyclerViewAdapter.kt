package my.projects.historyaroundkotlin.presentation.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BindRecyclerViewAdapter<T, L: ItemListener, VH: BindViewHolder<T, L>>(val items: List<T>, protected val listener: L): RecyclerView.Adapter<VH>() {

    @LayoutRes
    abstract fun layoutRes(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(layoutRes(), parent, false)
        return getViewHolder(view)
    }

    abstract fun getViewHolder(view: View): VH

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }
}