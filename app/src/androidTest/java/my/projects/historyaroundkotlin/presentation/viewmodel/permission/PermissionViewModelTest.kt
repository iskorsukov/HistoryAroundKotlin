package my.projects.historyaroundkotlin.presentation.viewmodel.permission

import android.Manifest
import android.content.pm.PackageManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.permission.PermissionFragment
import my.projects.historyaroundkotlin.presentation.view.permission.viewaction.NavigateToMapAction
import my.projects.historyaroundkotlin.presentation.view.permission.viewaction.ShowPermissionDeniedDialogAction
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.PermissionErrorItem
import my.projects.historyaroundkotlin.service.permission.PermissionRationaleMapper
import my.projects.historyaroundkotlin.service.permission.PermissionRationaleUtils
import my.projects.historyaroundkotlin.service.permission.PermissionSource
import my.projects.historyaroundkotlin.utils.MockitoUtil
import my.projects.historyaroundkotlin.utils.waitForValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class PermissionViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: PermissionViewModel
    private val mockPermissionSource = Mockito.mock(PermissionSource::class.java)

    @Before
    fun initViewModel() {
        viewModel = Mockito.spy(PermissionViewModel(mockPermissionSource))
    }

    private fun getSamplePermissions(): List<String> {
        return listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE)
    }

    private fun pushSampleNotGrantedPermissions() {
        Mockito.`when`(mockPermissionSource.getNotGrantedPermissions()).thenReturn(Single.just(getSamplePermissions()))
        Mockito.`when`(mockPermissionSource.mapPermissionToRationale(ArgumentMatchers.anyString())).then { invocation: InvocationOnMock? ->
            val mapper = PermissionRationaleMapper()
            return@then mapper.mapToRationale(invocation!!.arguments[0] as String)
        }
    }

    private fun pushDelayNotGrantedPermissions() {
        Mockito.`when`(mockPermissionSource.getNotGrantedPermissions()).thenReturn(Single.never())
    }

    private fun pushAllPermissionsGranted() {
        Mockito.`when`(mockPermissionSource.getNotGrantedPermissions()).thenReturn(Single.just(emptyList()))
    }

    @Test
    fun pushesLoadingStateOnGetViewStateLiveData() {
        pushDelayNotGrantedPermissions()

        val liveData = viewModel.viewStateLiveData

        val viewState = waitForValue(liveData)
        assertEquals(LCEState.LOADING, viewState.lceState)
    }

    @Test
    fun callsSourceOnCheckPermissions() {
        pushSampleNotGrantedPermissions()

        viewModel.checkPermissions()

        Mockito.verify(mockPermissionSource).getNotGrantedPermissions()
    }

    @Test
    fun mapsPermissionsToRationaleOnCheckPermissions() {
        pushSampleNotGrantedPermissions()

        viewModel.viewStateLiveData
        TimeUnit.SECONDS.sleep(2) // wait for value to change from loading to content

        Mockito.verify(mockPermissionSource, Mockito.times(2)).mapPermissionToRationale(ArgumentMatchers.anyString())
    }

    @Test
    fun returnsViewStateWithNotGrantedPermissionRationale() {
        pushSampleNotGrantedPermissions()

        val liveData = viewModel.viewStateLiveData
        TimeUnit.SECONDS.sleep(2) // wait for value to change from loading to content

        val viewState = waitForValue(liveData)
        assertEquals(LCEState.CONTENT, viewState.lceState)
        assertEquals(2, viewState.content!!.rationaleList.size)
        assertTrue(viewState.content!!.rationaleList.contains(PermissionRationaleUtils.NETWORK_STATE_RATIONALE))
        assertTrue(viewState.content!!.rationaleList.contains(PermissionRationaleUtils.LOCATION_RATIONALE))
    }

    @Test
    fun pushesNavigateActionOnAllPermissionsGranted() {
        pushAllPermissionsGranted()

        val liveData = viewModel.viewActionLiveEvent
        viewModel.checkPermissions()
        TimeUnit.SECONDS.sleep(2)

        val action = waitForValue(liveData)
        assertTrue(action is NavigateToMapAction)
    }

    private fun pushErrorOnGetNotGrantedPermissions() {
        Mockito.`when`(mockPermissionSource.getNotGrantedPermissions()).thenReturn(Single.error(IllegalArgumentException()))
    }

    @Test
    fun pushesErrorStateOnError() {
        pushErrorOnGetNotGrantedPermissions()

        val liveData = viewModel.viewStateLiveData
        TimeUnit.SECONDS.sleep(2) // wait for value to change from loading to error

        val viewState = liveData.value!!
        assertEquals(LCEState.ERROR, viewState.lceState)
        assertEquals(PermissionErrorItem.PERMISSIONS_ERROR, viewState.error)
    }

    @Test
    fun callsSourceOnRequestPermissions() {
        pushSampleNotGrantedPermissions()
        val mockFragment = Mockito.mock(PermissionFragment::class.java)

        viewModel.requestPermissions(mockFragment)

        Mockito.verify(mockPermissionSource).getNotGrantedPermissions()
        Mockito.verify(mockPermissionSource).requestPermissions(getSamplePermissions(), mockFragment, PermissionViewModel.PERMISSIONS_REQUEST_CODE)
    }

    @Test
    fun rechecksPermissionsAfterPermissionResult() {
        pushSampleNotGrantedPermissions()
        val mockFragment = Mockito.mock(PermissionFragment::class.java)
        Mockito.`when`(mockPermissionSource.shouldShowRequestPermissionRationale(ArgumentMatchers.anyString(), MockitoUtil.any())).thenReturn(true)

        viewModel.onRequestPermissionsResult(getSamplePermissions().toTypedArray(), intArrayOf(PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_GRANTED), mockFragment)

        Mockito.verify(viewModel).checkPermissions()
    }

    @Test
    fun pushesShowPermissionDeniedDialogOnPermissionDenied() {
        pushSampleNotGrantedPermissions()
        val mockFragment = Mockito.mock(PermissionFragment::class.java)
        Mockito.`when`(mockPermissionSource.shouldShowRequestPermissionRationale(ArgumentMatchers.anyString(), MockitoUtil.any())).thenReturn(false)

        val liveData = viewModel.viewActionLiveEvent
        viewModel.onRequestPermissionsResult(getSamplePermissions().toTypedArray(), intArrayOf(PackageManager.PERMISSION_DENIED, PackageManager.PERMISSION_GRANTED), mockFragment)

        val action = liveData.value
        assertTrue(action is ShowPermissionDeniedDialogAction)
    }
}