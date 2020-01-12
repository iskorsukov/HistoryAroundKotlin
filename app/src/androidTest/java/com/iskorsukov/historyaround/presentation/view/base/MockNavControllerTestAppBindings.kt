package com.iskorsukov.historyaround.presentation.view.base

import com.iskorsukov.historyaround.service.navigation.NavControllerSource
import com.iskorsukov.historyaround.service.navigation.NavControllerSourceImpl
import com.iskorsukov.historyaround.injection.test.TestAppBindings

class MockNavControllerTestAppBindings(val mockNavControllerSource: NavControllerSource): TestAppBindings() {

    override fun navControllerSource(navControllerSourceImpl: NavControllerSourceImpl): NavControllerSource {
        return mockNavControllerSource
    }
}