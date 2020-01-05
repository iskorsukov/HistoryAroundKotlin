package my.projects.historyaroundkotlin.presentation.view.permission

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.hadilq.liveevent.LiveEvent
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.presentation.view.base.BaseViewModelFragmentTest
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewaction.ViewAction
import my.projects.historyaroundkotlin.presentation.view.permission.viewaction.NavigateToMapAction
import my.projects.historyaroundkotlin.presentation.view.permission.viewaction.ShowPermissionDeniedDialogAction
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.PermissionLoadingItem
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.PermissionViewState
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.viewdata.PermissionRationale
import my.projects.historyaroundkotlin.presentation.view.permission.viewstate.viewdata.PermissionsViewData
import my.projects.historyaroundkotlin.presentation.viewmodel.permission.PermissionViewModel
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
class PermissionsFragmentTest: BaseViewModelFragmentTest<PermissionFragment, PermissionViewModel>() {

    override fun getViewModelClass(): Class<out PermissionViewModel> = PermissionViewModel::class.java

    override fun getViewModelBindings(): TestViewModelBindings = PermissionMockViewModelBindings(mockViewModel)

    private val viewStateLiveData: MutableLiveData<PermissionViewState> = MutableLiveData()
    private val viewActionLiveData: LiveEvent<ViewAction<*>> = LiveEvent()

    @Before
    fun setupMockViewModel() {
        Mockito.`when`(mockViewModel.viewStateLiveData).thenReturn(viewStateLiveData)
        Mockito.`when`(mockViewModel.viewActionLiveEvent).thenReturn(viewActionLiveData)
        Mockito.doNothing().`when`(mockViewModel).requestPermissions(MockitoUtil.any())
    }

    private fun getExamplePermissions(): List<PermissionRationale> {
        return ArrayList(mutableListOf(PermissionRationaleUtils.STORAGE_RATIONALE, PermissionRationaleUtils.LOCATION_RATIONALE))
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

    private fun pushPermissionNotGrantedViewState(notGrantedPermissions: List<PermissionRationale>) {
        viewStateLiveData.postValue(
            PermissionViewState(
                LCEState.CONTENT,
                null,
                PermissionsViewData(notGrantedPermissions),
                null
            )
        )
    }

    private fun pushLoadingState() {
        viewStateLiveData.postValue(PermissionViewState(LCEState.LOADING, PermissionLoadingItem.PERMISSION_LOADING, null, null))
    }

    private fun pushNavigateToMapAction() {
        viewActionLiveData.postValue(NavigateToMapAction())
    }

    private fun pushShowPermissionDeniedDialogAction() {
        viewActionLiveData.postValue(ShowPermissionDeniedDialogAction())
    }

    @Test
    fun observesViewState() {
        pushLoadingState()
        launchFragment(PermissionFragment::class.java)

        Mockito.verify(mockViewModel).viewStateLiveData
        Mockito.verify(mockViewModel).viewActionLiveEvent
    }

    @Test
    fun showsLoadingState() {
        pushLoadingState()
        launchFragment(PermissionFragment::class.java)

        checkShowsProgressBar()
    }

    private fun checkShowsProgressBar() {
        onView(withId(R.id.loading)).check(matches(isDisplayed()))
    }

    @Test
    fun  showsRationale() {
        pushPermissionNotGrantedViewState(getExamplePermissions())
        launchFragment(PermissionFragment::class.java)

        for (i in getExamplePermissions().indices) {
            checkShowsRationale(i, getExamplePermissions()[i])
        }
    }

    @Test
    fun requestsPermissionsOnButtonPress() {
        pushPermissionNotGrantedViewState(getExamplePermissions())
        launchFragment(PermissionFragment::class.java)

        pressGrantButton()

        Mockito.verify(mockViewModel).requestPermissions(MockitoUtil.any())
    }

    @Test
    fun navigatesToMapOnAction() {
        launchFragment(PermissionFragment::class.java)
        pushNavigateToMapAction()

        Mockito.verify(mockNavController, timeout(TimeUnit.SECONDS.toMillis(2))).navigate(argThat { directions: NavDirections -> directions.actionId == R.id.action_permissionFragment_to_mapFragment } )
    }

    @Test
    fun showsPermissionDeniedDialogOnAction() {
        launchFragment(PermissionFragment::class.java)
        pushShowPermissionDeniedDialogAction()

        checkShowsPermissionDeniedDialog()
    }

    private fun checkShowsPermissionDeniedDialog() {
        onView(withText(R.string.permissions_denied_title)).check(matches(isDisplayed()))
        onView(withText(R.string.permissions_denied_message)).check(matches(isDisplayed()))
    }
}