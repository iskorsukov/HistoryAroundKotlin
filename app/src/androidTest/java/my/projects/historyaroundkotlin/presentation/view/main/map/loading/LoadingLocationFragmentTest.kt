package my.projects.historyaroundkotlin.presentation.view.main.map.loading

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.presentation.view.base.BaseViewModelFragmentTest
import my.projects.historyaroundkotlin.presentation.view.main.map.MapMockViewModelBindings
import my.projects.historyaroundkotlin.presentation.viewmodel.main.map.MapFlowViewModel
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultState
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.location.LocationErrorStatus
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.location.LocationViewState
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoadingLocationFragmentTest: BaseViewModelFragmentTest<LoadingLocationFragment, MapFlowViewModel>() {

    override fun getViewModelClass(): Class<out MapFlowViewModel> = MapFlowViewModel::class.java

    override fun getViewModelBindings(): TestViewModelBindings = MapMockViewModelBindings(mockViewModel)

    private fun prepareLocationLiveData() {
        val liveData = MutableLiveData<LocationViewState>()
        val location = Location("GPS")
        liveData.postValue(LocationViewState(LoadingResultState.content(), location))

        Mockito.`when`(mockViewModel.observeLocation()).thenReturn(liveData)
    }

    private fun prepareErrorLocationLiveData(error: LocationErrorStatus) {
        val liveData = MutableLiveData<LocationViewState>()
        liveData.postValue(LocationViewState(LoadingResultState.error(listOf(error)), null))

        Mockito.`when`(mockViewModel.observeLocation()).thenReturn(liveData)
    }

    @Test
    fun loadsLocation() {
        prepareLocationLiveData()

        launchFragment(LoadingLocationFragment::class.java)

        Mockito.verify(mockViewModel).observeLocation()
    }

    @Test
    fun navigatesToLoadArticlesFragmentAfterLocationLoaded() {
        prepareLocationLiveData()

        launchFragment(LoadingLocationFragment::class.java)

        Mockito.verify(mockNavController).navigate(ArgumentMatchers.argThat { directions: NavDirections ->
            directions.actionId == R.id.action_loadingLocationFragment_to_loadingArticlesFragment
        })
    }

    @Test
    fun navigatesToErrorScreenOnLocationError() {
        prepareErrorLocationLiveData(LocationErrorStatus.LOCATION_UNAVAILABLE)

        launchFragment(LoadingLocationFragment::class.java)

        Mockito.verify(mockNavController).navigate(ArgumentMatchers.argThat { directions: NavDirections ->
            directions.actionId == R.id.action_loadingLocationFragment_to_errorLocationFragment
        })
    }

    @Test
    fun navigatesToErrorScreenOnLocationServicesUnavailable() {
        prepareErrorLocationLiveData(LocationErrorStatus.LOCATION_SERVICE_UNAVAILABLE)

        launchFragment(LoadingLocationFragment::class.java)

        Mockito.verify(mockNavController).navigate(ArgumentMatchers.argThat { directions: NavDirections ->
            directions.actionId == R.id.action_loadingLocationFragment_to_errorLocationFragment
        })

    }
}