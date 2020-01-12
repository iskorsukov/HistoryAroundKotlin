package com.iskorsukov.historyaround.presentation.view.permission.adapter

import com.iskorsukov.historyaround.databinding.HolderPermissionRationaleItemBinding
import com.iskorsukov.historyaround.presentation.view.common.adapter.BindViewHolder
import com.iskorsukov.historyaround.presentation.view.common.adapter.ItemListener
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.viewdata.PermissionRationale

class PermissionRationaleHolder(dataBinding: HolderPermissionRationaleItemBinding, listener: ItemListener):
    BindViewHolder<PermissionRationale, HolderPermissionRationaleItemBinding, ItemListener>(dataBinding, listener)  {
    override fun bind(item: PermissionRationale) {
        dataBinding.item = item
        dataBinding.executePendingBindings()
    }
}