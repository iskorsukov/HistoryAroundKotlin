package my.projects.historyaroundkotlin.presentation.view.permission.adapter

import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.databinding.HolderPermissionRationaleItemBinding
import my.projects.historyaroundkotlin.presentation.view.common.adapter.BindRecyclerViewAdapter
import my.projects.historyaroundkotlin.presentation.view.common.adapter.ItemListener
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.viewdata.PermissionRationale

class PermissionsAdapter(items: List<PermissionRationale>, listener: ItemListener):
    BindRecyclerViewAdapter<PermissionRationale, HolderPermissionRationaleItemBinding, ItemListener, PermissionRationaleHolder>(items, listener) {
    override fun layoutRes(): Int {
        return R.layout.holder_permission_rationale_item
    }

    override fun getViewHolder(dataBinding: HolderPermissionRationaleItemBinding): PermissionRationaleHolder {
        return PermissionRationaleHolder(dataBinding, listener)
    }
}