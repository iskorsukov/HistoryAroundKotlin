package my.projects.historyaroundkotlin.presentation.view.start.permission.content.adapter

import android.view.View
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.common.adapter.BindRecyclerViewAdapter
import my.projects.historyaroundkotlin.presentation.common.adapter.ItemListener
import my.projects.historyaroundkotlin.presentation.viewstate.start.permission.PermissionRationale

class PermissionsAdapter(items: List<PermissionRationale>, listener: ItemListener): BindRecyclerViewAdapter<PermissionRationale, ItemListener, PermissionRationaleHolder>(items, listener) {
    override fun layoutRes(): Int {
        return R.layout.holder_permission_rationale_item
    }

    override fun getViewHolder(view: View): PermissionRationaleHolder {
        return PermissionRationaleHolder(view, listener)
    }
}