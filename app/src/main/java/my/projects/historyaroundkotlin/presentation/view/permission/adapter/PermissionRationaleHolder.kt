package my.projects.historyaroundkotlin.presentation.view.permission.adapter

import my.projects.historyaroundkotlin.databinding.HolderPermissionRationaleItemBinding
import my.projects.historyaroundkotlin.presentation.view.common.adapter.BindViewHolder
import my.projects.historyaroundkotlin.presentation.view.common.adapter.ItemListener
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.viewdata.PermissionRationale

class PermissionRationaleHolder(dataBinding: HolderPermissionRationaleItemBinding, listener: ItemListener):
    BindViewHolder<PermissionRationale, HolderPermissionRationaleItemBinding, ItemListener>(dataBinding, listener)  {
    override fun bind(item: PermissionRationale) {
        dataBinding.item = item
        dataBinding.executePendingBindings()
    }
}