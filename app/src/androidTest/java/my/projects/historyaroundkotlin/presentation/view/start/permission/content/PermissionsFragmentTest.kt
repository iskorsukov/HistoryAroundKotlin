package my.projects.historyaroundkotlin.presentation.view.start.permission.content

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.presentation.argument.RationaleListArgument
import my.projects.historyaroundkotlin.presentation.view.base.BaseViewModelFragmentTest
import my.projects.historyaroundkotlin.presentation.view.start.permission.PermissionMockViewModelBindings
import my.projects.historyaroundkotlin.presentation.viewmodel.start.permission.PermissionFlowViewModel
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultState
import my.projects.historyaroundkotlin.presentation.viewstate.start.permission.PermissionRationale
import my.projects.historyaroundkotlin.presentation.viewstate.start.permission.PermissionViewState
import my.projects.historyaroundkotlin.service.permission.PermissionRationaleUtils
import my.projects.historyaroundkotlin.utils.MockitoUtil
import my.projects.historyaroundkotlin.utils.RecyclerViewMatcherUtils
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.argThat
import org.mockito.Mockito
import org.mockito.Mockito.timeout
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@LargeTest
class PermissionsFragmentTest: BaseViewModelFragmentTest<PermissionFragment, PermissionFlowViewModel>() {

    override fun getViewModelClass(): Class<out PermissionFlowViewModel> = PermissionFlowViewModel::class.java

    override fun getViewModelBindings(): TestViewModelBindings = PermissionMockViewModelBindings(mockViewModel)

    private val viewStateLiveData: MutableLiveData<PermissionViewState> = MutableLiveData()

    @Before
    fun setupMockViewModel() {
        Mockito.`when`(mockViewModel.checkPermissions()).thenReturn(viewStateLiveData)
        Mockito.doNothing().`when`(mockViewModel).requestPermissions(MockitoUtil.any())
    }

    private fun getExamplePermissions(): List<PermissionRationale> {
        return ArrayList(mutableListOf(PermissionRationaleUtils.STORAGE_RATIONALE, PermissionRationaleUtils.LOCATION_RATIONALE))
    }

    private fun setupPermissionsArgs(): Bundle {
        val args = Bundle()
        val rationaleListArgument = RationaleListArgument(getExamplePermissions() as ArrayList<PermissionRationale>)
        args.putSerializable("rationaleListArgument", rationaleListArgument)
        return args
    }

    private fun checkShowsRationale(position: Int, rationale: PermissionRationale) {
        onView(RecyclerViewMatcherUtils.onViewInPosition(position, R.id.permissionsRecyclerView)).check(matches(withChild(
            withText(rationale.titleRes))))
        onView(RecyclerViewMatcherUtils.onViewInPosition(position, R.id.permissionsRecyclerView)).check(matches(withChild(
            withText(rationale.messageRes))))
    }

    private fun pressGrantButton() {
        onView(withId(R.id.grantPermissionsButton)).perform(click())
    }

    private fun pushPermissionNotGrantedViewState() {
        viewStateLiveData.postValue(PermissionViewState(LoadingResultState.content(), listOf(PermissionRationaleUtils.LOCATION_RATIONALE)))
    }

    private fun pushAllPermissionsGrantedViewState() {
        viewStateLiveData.postValue(PermissionViewState(LoadingResultState.content(), emptyList()))
    }

    @Test
    fun  showsRationale() {
        launchFragment(PermissionFragment::class.java, setupPermissionsArgs())

        checkShowsRationale(0, PermissionRationaleUtils.STORAGE_RATIONALE)
        checkShowsRationale(1, PermissionRationaleUtils.LOCATION_RATIONALE)
    }

    @Test
    fun requestsPermissionsOnButtonPress() {
        launchFragment(PermissionFragment::class.java, setupPermissionsArgs())

        pressGrantButton()

        Mockito.verify(mockViewModel).requestPermissions(MockitoUtil.any())
    }

    @Test
    fun navigatesToMainFlowAfterAllPermissionsGranted() {
        launchFragment(PermissionFragment::class.java, setupPermissionsArgs())

        scenario.onFragment {
            it.onRequestPermissionsResult(PermissionFlowViewModel.PERMISSIONS_REQUEST_CODE, emptyArray(), intArrayOf())
        }
        pushAllPermissionsGrantedViewState()

        Mockito.verify(mockViewModel).checkPermissions()
        Mockito.verify(mockNavController, timeout(TimeUnit.SECONDS.toMillis(2))).navigate(argThat { directions: NavDirections -> directions.actionId == R.id.action_permissionFragment_to_main_flow } )
    }

    @Test
    fun rechecksPermissionsAfterPermissionsInteraction() {
        launchFragment(PermissionFragment::class.java, setupPermissionsArgs())

        scenario.onFragment {
            it.onRequestPermissionsResult(PermissionFlowViewModel.PERMISSIONS_REQUEST_CODE, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), intArrayOf(PackageManager.PERMISSION_GRANTED))
        }

        Mockito.verify(mockViewModel).checkPermissions()
    }

    @Test
    fun showsRationaleAfterSomePermissionsNotGranted() {
        launchFragment(PermissionFragment::class.java, setupPermissionsArgs())

        scenario.onFragment {
            it.onRequestPermissionsResult(PermissionFlowViewModel.PERMISSIONS_REQUEST_CODE, emptyArray(), intArrayOf())
        }
        pushPermissionNotGrantedViewState()

        checkShowsRationale(0, PermissionRationaleUtils.LOCATION_RATIONALE)
    }
}