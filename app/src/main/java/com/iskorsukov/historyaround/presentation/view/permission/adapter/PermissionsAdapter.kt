package com.iskorsukov.historyaround.presentation.view.permission.adapter

import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.databinding.HolderPermissionRationaleItemBinding
import com.iskorsukov.historyaround.presentation.view.common.adapter.BindRecyclerViewAdapter
import com.iskorsukov.historyaround.presentation.view.common.adapter.ItemListener
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.viewdata.PermissionRationale

class PermissionsAdapter(items: List<PermissionRationale>, listener: ItemListener):
    BindRecyclerViewAdapter<PermissionRationale, HolderPermissionRationaleItemBinding, ItemListener, PermissionRationaleHolder>(items, listener) {
    override fun layoutRes(): Int {
        return R.layout.holder_permission_rationale_item
    }

    override fun getViewHolder(dataBinding: HolderPermissionRationaleItemBinding): PermissionRationaleHolder {
        return PermissionRationaleHolder(dataBinding, listener)
    }
}