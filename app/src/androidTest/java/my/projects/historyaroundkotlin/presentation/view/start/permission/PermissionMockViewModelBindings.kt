package my.projects.historyaroundkotlin.presentation.view.start.permission

import androidx.lifecycle.ViewModel
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.presentation.viewmodel.start.permission.PermissionFlowViewModel

class PermissionMockViewModelBindings(val permissionFlowViewModel: PermissionFlowViewModel): TestViewModelBindings() {
    override fun bindPermissionsViewModel(myViewModel: PermissionFlowViewModel): ViewModel {
        return permissionFlowViewModel
    }
}