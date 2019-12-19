package my.projects.historyaroundkotlin.presentation.view.permission.loading

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.presentation.argument.RationaleListArgument
import my.projects.historyaroundkotlin.presentation.view.base.BaseViewModelFragmentTest
import my.projects.historyaroundkotlin.presentation.view.permission.PermissionMockViewModelBindings
import my.projects.historyaroundkotlin.presentation.viewmodel.permission.PermissionViewModel
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultState
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.viewdata.PermissionRationale
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.PermissionViewState
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.argThat
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoadingPermissionsFragmentTest: BaseViewModelFragmentTest<LoadingPermissionsFragment, PermissionViewModel>() {

    override fun getViewModelClass(): Class<out PermissionViewModel> = PermissionViewModel::class.java

    override fun getViewModelBindings(): TestViewModelBindings = PermissionMockViewModelBindings(mockViewModel)

    private fun setupMissingLocationPermission(): List<PermissionRationale> {
        val missingPermissions = listOf(
            PermissionRationale(
                R.drawable.ic_location,
                R.string.location_permission_label,
                R.string.location_rationale
            )
        )
        val missingPermissionsLiveData = MutableLiveData<PermissionViewState>()
        missingPermissionsLiveData.postValue(
            PermissionViewState(
                LoadingResultState.content(),
                missingPermissions
            )
        )
        doReturn(missingPermissionsLiveData).`when`(mockViewModel).checkPermissions()
        return missingPermissions
    }

    private fun setupNoMissingPermissions() {
        val missingPermissionsLiveData = MutableLiveData<PermissionViewState>()
        missingPermissionsLiveData.postValue(
            PermissionViewState(
                LoadingResultState.content(),
                emptyList()
            )
        )
        doReturn(missingPermissionsLiveData).`when`(mockViewModel).checkPermissions()
    }

    @Test
    fun checksForMissingPermissions() {
        setupMissingLocationPermission()

        launchFragment(LoadingPermissionsFragment::class.java)

        verify(mockViewModel).checkPermissions()
    }

    @Test
    fun navigatesToPermissionsFragmentWhenHasMissingPermissions() {
        val missingPermissions = setupMissingLocationPermission()

        launchFragment(LoadingPermissionsFragment::class.java)

        verify(mockNavController).navigate(argThat { directions: NavDirections ->
            directions.actionId == R.id.action_loadingPermissionsFragment_to_permissionFragment
                    && (directions.arguments["rationaleListArgument"] as RationaleListArgument).rationaleList.containsAll(missingPermissions)
        })
    }

    @Test
    fun navigatesToMainFlowWhenHasMissingAllPermissionsGranted() {
        setupNoMissingPermissions()

        launchFragment(LoadingPermissionsFragment::class.java)

        verify(mockNavController).navigate(argThat { directions: NavDirections ->
            directions.actionId == R.id.action_loadingPermissionsFragment_to_main_flow
        })
    }
}