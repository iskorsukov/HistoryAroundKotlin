package my.projects.historyaroundkotlin.presentation.view.start.permission.content.adapter

import android.view.View
import kotlinx.android.synthetic.main.holder_permission_rationale_item.view.*
import my.projects.historyaroundkotlin.presentation.common.adapter.BindViewHolder
import my.projects.historyaroundkotlin.presentation.common.adapter.ItemListener
import my.projects.historyaroundkotlin.presentation.viewstate.start.permission.PermissionRationale

class PermissionRationaleHolder(view: View, listener: ItemListener): BindViewHolder<PermissionRationale, ItemListener>(view, listener)  {
    override fun bind(item: PermissionRationale) {
        itemView.apply {
            rationaleIcon.setImageDrawable(context.getDrawable(item.iconRes))
            rationaleTitle.setText(item.titleRes)
            rationaleMessage.setText(item.messageRes)
        }
    }
}