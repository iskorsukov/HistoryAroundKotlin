package my.projects.historyaroundkotlin.presentation.view.base

import my.projects.historyaroundkotlin.injection.navigation.NavControllerSource
import my.projects.historyaroundkotlin.injection.navigation.NavControllerSourceImpl
import my.projects.historyaroundkotlin.injection.test.TestAppBindings

class MockNavControllerTestAppBindings(val mockNavControllerSource: NavControllerSource): TestAppBindings() {

    override fun navControllerSource(navControllerSourceImpl: NavControllerSourceImpl): NavControllerSource {
        return mockNavControllerSource
    }
}