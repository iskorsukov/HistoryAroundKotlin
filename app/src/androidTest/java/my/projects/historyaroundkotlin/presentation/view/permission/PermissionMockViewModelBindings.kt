package my.projects.historyaroundkotlin.presentation.view.permission

import androidx.lifecycle.ViewModel
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.presentation.viewmodel.permission.PermissionViewModel

class PermissionMockViewModelBindings(val permissionViewModel: PermissionViewModel): TestViewModelBindings() {
    override fun bindPermissionsViewModel(myViewModel: PermissionViewModel): ViewModel {
        return permissionViewModel
    }
}