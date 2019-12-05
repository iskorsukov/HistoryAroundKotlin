package my.projects.historyaroundkotlin.presentation.view.base

import my.projects.historyaroundkotlin.service.navigation.NavControllerSource
import my.projects.historyaroundkotlin.service.navigation.NavControllerSourceImpl
import my.projects.historyaroundkotlin.injection.test.TestAppBindings

class MockNavControllerTestAppBindings(val mockNavControllerSource: NavControllerSource): TestAppBindings() {

    override fun navControllerSource(navControllerSourceImpl: NavControllerSourceImpl): NavControllerSource {
        return mockNavControllerSource
    }
}