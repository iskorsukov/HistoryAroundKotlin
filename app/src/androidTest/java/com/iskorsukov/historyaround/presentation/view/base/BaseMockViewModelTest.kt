package com.iskorsukov.historyaround.presentation.view.base

import androidx.lifecycle.ViewModel
import com.iskorsukov.historyaround.injection.test.DaggerTestAppComponent
import com.iskorsukov.historyaround.injection.test.TestViewModelBindings
import org.mockito.Mockito

abstract class BaseMockViewModelTest<VM: ViewModel>: BaseMockNavControllerTest() {

    protected val mockViewModel: VM = Mockito.mock(getViewModelClass())

    abstract fun getViewModelClass(): Class<out VM>

    abstract fun getViewModelBindings(): TestViewModelBindings

    override fun testAppComponentBuilder(): DaggerTestAppComponent.Builder {
        return super.testAppComponentBuilder().testViewModelBindings(getViewModelBindings())
    }
}