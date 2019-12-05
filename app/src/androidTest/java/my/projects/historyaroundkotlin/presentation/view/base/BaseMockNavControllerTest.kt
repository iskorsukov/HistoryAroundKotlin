package my.projects.historyaroundkotlin.presentation.view.base

import androidx.navigation.NavController
import my.projects.historyaroundkotlin.injection.navigation.NavControllerSource
import my.projects.historyaroundkotlin.injection.test.DaggerTestAppComponent
import my.projects.historyaroundkotlin.utils.MockitoUtil
import org.mockito.Mockito

abstract class BaseMockNavControllerTest: BaseAppComponentTest() {
    protected lateinit var mockNavController: NavController

    override fun testAppComponentBuilder(): DaggerTestAppComponent.Builder {
        val mockNavControllerSource = getMockNavControllerSource()

        return DaggerTestAppComponent.builder()
            .testAppBindings(
                MockNavControllerTestAppBindings(
                    mockNavControllerSource
                )
            )
    }

    private fun getMockNavControllerSource(): NavControllerSource {
        val mockNavControllerSource = Mockito.mock(NavControllerSource::class.java)
        mockNavController = Mockito.mock(NavController::class.java)
        Mockito.`when`(mockNavControllerSource.navController(MockitoUtil.any())).thenReturn(mockNavController)

        return mockNavControllerSource
    }
}