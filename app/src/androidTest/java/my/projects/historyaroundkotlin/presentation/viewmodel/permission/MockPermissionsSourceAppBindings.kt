package my.projects.historyaroundkotlin.presentation.viewmodel.permission

import dagger.Module
import my.projects.historyaroundkotlin.injection.test.TestAppBindings
import my.projects.historyaroundkotlin.service.permission.PermissionSource
import my.projects.historyaroundkotlin.service.permission.PermissionSourceImpl

@Module
class MockPermissionsSourceAppBindings(private val mockPermissionsSource: PermissionSource): TestAppBindings() {

    override fun bindsPermissionsSource(permissionSourceImpl: PermissionSourceImpl): PermissionSource {
        return mockPermissionsSource
    }
}