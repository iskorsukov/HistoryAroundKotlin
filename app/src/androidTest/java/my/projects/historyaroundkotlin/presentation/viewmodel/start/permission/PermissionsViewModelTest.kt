package my.projects.historyaroundkotlin.presentation.viewmodel.start.permission

import android.Manifest
import androidx.fragment.app.Fragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.reactivex.Single
import my.projects.historyaroundkotlin.HistoryAroundApp
import my.projects.historyaroundkotlin.injection.AppModule
import my.projects.historyaroundkotlin.injection.test.DaggerTestAppComponent
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultStatus
import my.projects.historyaroundkotlin.presentation.viewstate.start.permission.PermissionRationale
import my.projects.historyaroundkotlin.service.permission.PermissionSource
import my.projects.historyaroundkotlin.utils.MockitoUtil
import my.projects.historyaroundkotlin.utils.waitForValue
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class PermissionsViewModelTest {
    private val mockPermissionSource = Mockito.mock(PermissionSource::class.java)
    private lateinit var viewModel: PermissionFlowViewModel

    @Before
    fun instantiateMocks() {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as HistoryAroundApp
        app.appComponent = DaggerTestAppComponent.builder()
            .appModule(AppModule(app))
            .testAppBindings(MockPermissionsSourceAppBindings(mockPermissionSource))
            .build()
        viewModel = app.appComponent.viewModelFactory().create(PermissionFlowViewModel::class.java)
    }

    private fun getMissingPermissions(): List<String> {
        return listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET)
    }

    private fun getMissingPermissionsSingle(): Single<List<String>> {
        return Single.just(getMissingPermissions())
    }

    private fun prepareMissingPermissions() {
        Mockito.`when`(mockPermissionSource.getNotGrantedPermissions()).thenReturn(getMissingPermissionsSingle())
    }

    private fun prepareMapPermissionsToRationale() {
        Mockito.`when`(mockPermissionSource.mapPermissionToRationale(ArgumentMatchers.anyString())).thenReturn(
            PermissionRationale(0, 0, 0)
        )
    }

    @Test
    fun checkPermissionsCallsNonGrantedPermissionsAndReturnsViewState() {
        prepareMissingPermissions()
        prepareMapPermissionsToRationale()

        val liveData = viewModel.checkPermissions()

        val viewState = waitForValue(liveData)

        Mockito.verify(mockPermissionSource).getNotGrantedPermissions()
        Mockito.verify(mockPermissionSource, Mockito.times(2)).mapPermissionToRationale(ArgumentMatchers.anyString())
        assertThat(viewState.lceState.status, `is`(equalTo(LoadingResultStatus.CONTENT)))
        assertThat(viewState.rationaleList, `is`(notNullValue()))
        assertThat(viewState.rationaleList!!.size, `is`(2))
    }

    @Test
    fun requestPermissionsCallsNonGrantedPermissionsAndRequestPermissions() {
        prepareMissingPermissions()
        val mockFragment = Mockito.mock(Fragment::class.java)

        viewModel.requestPermissions(mockFragment)

        Mockito.verify(mockPermissionSource).getNotGrantedPermissions()
        Mockito.verify(mockPermissionSource).requestPermissions(
            MockitoUtil.argThat { list -> list.containsAll(getMissingPermissions()) },
            MockitoUtil.any(),
            ArgumentMatchers.anyInt())
    }
}