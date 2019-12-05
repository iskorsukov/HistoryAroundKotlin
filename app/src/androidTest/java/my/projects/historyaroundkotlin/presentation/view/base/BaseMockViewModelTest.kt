package my.projects.historyaroundkotlin.presentation.view.base

import androidx.lifecycle.ViewModel
import my.projects.historyaroundkotlin.injection.test.DaggerTestAppComponent
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import org.mockito.Mockito

abstract class BaseMockViewModelTest<VM: ViewModel>: BaseMockNavControllerTest() {

    protected val mockViewModel: VM = Mockito.mock(getViewModelClass())

    abstract fun getViewModelClass(): Class<out VM>

    abstract fun getViewModelBindings(): TestViewModelBindings

    override fun testAppComponentBuilder(): DaggerTestAppComponent.Builder {
        return super.testAppComponentBuilder().testViewModelBindings(getViewModelBindings())
    }
}