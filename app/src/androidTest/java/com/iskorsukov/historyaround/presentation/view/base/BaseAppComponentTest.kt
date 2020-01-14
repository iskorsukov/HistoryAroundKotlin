package com.iskorsukov.historyaround.presentation.view.base

import androidx.test.platform.app.InstrumentationRegistry
import com.iskorsukov.historyaround.HistoryAroundApp
import com.iskorsukov.historyaround.injection.AppModule
import com.iskorsukov.historyaround.injection.test.DaggerTestAppComponent
import org.junit.Before

abstract class BaseAppComponentTest {

    abstract fun testAppComponentBuilder(): DaggerTestAppComponent.Builder

    @Before
    fun setupAppComponent() {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as HistoryAroundApp
        app.appComponent = testAppComponentBuilder().appModule(AppModule(app)).build()
    }
}