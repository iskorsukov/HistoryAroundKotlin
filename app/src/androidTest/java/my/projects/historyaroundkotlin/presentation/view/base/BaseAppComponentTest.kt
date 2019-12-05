package my.projects.historyaroundkotlin.presentation.view.base

import androidx.test.platform.app.InstrumentationRegistry
import my.projects.historyaroundkotlin.HistoryAroundApp
import my.projects.historyaroundkotlin.injection.AppModule
import my.projects.historyaroundkotlin.injection.test.DaggerTestAppComponent
import org.junit.Before

abstract class BaseAppComponentTest {

    abstract fun testAppComponentBuilder(): DaggerTestAppComponent.Builder

    @Before
    fun setupAppComponent() {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as HistoryAroundApp
        app.appComponent = testAppComponentBuilder().appModule(AppModule(app)).build()
    }
}