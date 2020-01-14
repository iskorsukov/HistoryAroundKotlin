package com.iskorsukov.historyaround.presentation.view.permission

import androidx.lifecycle.ViewModel
import com.iskorsukov.historyaround.injection.test.TestViewModelBindings
import com.iskorsukov.historyaround.presentation.viewmodel.permission.PermissionViewModel

class PermissionMockViewModelBindings(val permissionViewModel: PermissionViewModel): TestViewModelBindings() {
    override fun bindPermissionsViewModel(myViewModel: PermissionViewModel): ViewModel {
        return permissionViewModel
    }
}