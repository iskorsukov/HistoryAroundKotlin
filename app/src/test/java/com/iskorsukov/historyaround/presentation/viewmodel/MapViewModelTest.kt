package com.iskorsukov.historyaround.presentation.viewmodel

import android.Manifest
import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsResult
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.iskorsukov.historyaround.model.ArticleItemCreator
import com.iskorsukov.historyaround.model.preferences.PreferencesBundle
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.map.utils.ArticlesClusterFactory
import com.iskorsukov.historyaround.presentation.view.map.utils.toViewData
import com.iskorsukov.historyaround.presentation.view.map.viewaction.CenterOnLocationAction
import com.iskorsukov.historyaround.presentation.view.map.viewaction.NavigateToDetailsAction
import com.iskorsukov.historyaround.presentation.view.map.viewaction.ResolveLocationServicesAction
import com.iskorsukov.historyaround.presentation.view.map.viewaction.ShowArticleSelectorAction
import com.iskorsukov.historyaround.presentation.view.map.viewstate.MapErrorItem
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticleItemViewData
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.MapViewData
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.toOverlayItem
import com.iskorsukov.historyaround.presentation.viewmodel.map.MapViewModel
import com.iskorsukov.historyaround.service.api.WikiSource
import com.iskorsukov.historyaround.service.location.LocationSource
import com.iskorsukov.historyaround.service.preferences.PreferencesSource
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class MapViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val locationSource: LocationSource = mockk()
    private val wikiSource: WikiSource = mockk()
    private val preferencesSource: PreferencesSource = mockk()
    private val clusterFactory = spyk(ArticlesClusterFactory())

    private val viewModel = MapViewModel(
        locationSource, wikiSource, preferencesSource, clusterFactory
    )

    private val isLoadingObserver: Observer<Boolean> = mockk(relaxed = true)
    private val locationObserver: Observer<Pair<Double, Double>> = mockk(relaxed = true)
    private val mapViewDataObserver: Observer<MapViewData> = mockk(relaxed = true)
    private val mapActionEventObserver: Observer<ViewAction<*>> = mockk(relaxed = true)
    private val mapErrorEventObserver: Observer<MapErrorItem> = mockk(relaxed = true)

    private val latlon = 0.0 to 1.0
    private val location: Location = mockk<Location>().also {
        every { it.latitude } returns latlon.first
        every { it.longitude } returns latlon.second
    }

    private val languageCode = "en"
    private val radius = 1000
    private val preferences = PreferencesBundle(radius, languageCode)
    private val preferencesFlow = MutableStateFlow(preferences)

    private val mapViewData = MapViewData(
        clusterFactory.groupItemsIntoMarkers(
            MapViewModel.DEFAULT_ZOOM_VALUE,
            ArticleItemCreator.createNormalList().map {
                ArticleItemViewData(it, false)
            }
        )
    )

    @Before
    fun observeData() {
        viewModel.mapIsLoadingLiveData.observeForever(isLoadingObserver)
        viewModel.mapLocationLiveData.observeForever(locationObserver)
        viewModel.mapDataLiveData.observeForever(mapViewDataObserver)
        viewModel.mapActionLiveEvent.observeForever(mapActionEventObserver)
        viewModel.mapErrorLiveEvent.observeForever(mapErrorEventObserver)
    }

    @Before
    fun mockData() {
        coEvery {
            locationSource.checkLocationServicesAvailability()
        } returns LocationSettingsResponse(LocationSettingsResult(Status.RESULT_SUCCESS, null))
        coEvery {
            locationSource.getCurrentLocation()
        } returns location
        coEvery {
            wikiSource.loadArticleItems(any(), any(), any())
        } returns ArticleItemCreator.createNormalList()
        coEvery {
            preferencesSource.getPreferencesFlow()
        } returns preferencesFlow
    }

    @After
    fun removeObserver() {
        viewModel.mapIsLoadingLiveData.removeObserver(isLoadingObserver)
        viewModel.mapLocationLiveData.removeObserver(locationObserver)
        viewModel.mapDataLiveData.removeObserver(mapViewDataObserver)
        viewModel.mapActionLiveEvent.removeObserver(mapActionEventObserver)
        viewModel.mapErrorLiveEvent.removeObserver(mapErrorEventObserver)
    }

    @Test
    fun loadArticlesCoroutine() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        viewModel.loadArticlesCoroutine()
        advanceUntilIdle()

        verifyOrder {
            isLoadingObserver.onChanged(true)
            locationObserver.onChanged(latlon)
            mapActionEventObserver.onChanged(CenterOnLocationAction(latlon))
            mapViewDataObserver.onChanged(mapViewData)
            isLoadingObserver.onChanged(false)
        }
        verify(exactly = 0) {
            mapErrorEventObserver.onChanged(any())
        }
        coVerifyOrder {
            locationSource.checkLocationServicesAvailability()
            locationSource.getCurrentLocation()
            preferencesSource.getPreferencesFlow()
            wikiSource.loadArticleItems(preferences.languageCode, latlon, preferences.radius)
        }

        Dispatchers.resetMain()
    }

    @Test
    fun loadArticlesCoroutine_LocationServices_ApiException() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        coEvery {
            locationSource.checkLocationServicesAvailability()
        } throws ApiException(Status.RESULT_INTERNAL_ERROR)

        viewModel.loadArticlesCoroutine()
        advanceUntilIdle()

        verify {
            isLoadingObserver.onChanged(true)
            mapErrorEventObserver.onChanged(MapErrorItem.GOOGLE_SERVICES_ERROR)
        }
        verify(exactly = 0) {
            isLoadingObserver.onChanged(false)
            locationObserver.onChanged(latlon)
            mapViewDataObserver.onChanged(mapViewData)
        }
        coVerify {
            locationSource.checkLocationServicesAvailability()
        }
        coVerify(exactly = 0) {
            locationSource.getCurrentLocation()
            preferencesSource.getPreferencesFlow()
            wikiSource.loadArticleItems(preferences.languageCode, latlon, preferences.radius)
        }

        Dispatchers.resetMain()
    }

    @Test
    fun loadArticlesCoroutine_LocationServices_ResolvableApiException() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val exception = ResolvableApiException(Status(LocationSettingsStatusCodes.RESOLUTION_REQUIRED))
        coEvery {
            locationSource.checkLocationServicesAvailability()
        } throws exception

        viewModel.loadArticlesCoroutine()
        advanceUntilIdle()

        verify {
            isLoadingObserver.onChanged(true)
            mapActionEventObserver.onChanged(ResolveLocationServicesAction(exception))
        }
        verify(exactly = 0) {
            isLoadingObserver.onChanged(false)
            locationObserver.onChanged(latlon)
            mapViewDataObserver.onChanged(mapViewData)
            mapErrorEventObserver.onChanged(any())
        }
        coVerify {
            locationSource.checkLocationServicesAvailability()
        }
        coVerify(exactly = 0) {
            locationSource.getCurrentLocation()
            preferencesSource.getPreferencesFlow()
            wikiSource.loadArticleItems(preferences.languageCode, latlon, preferences.radius)
        }

        Dispatchers.resetMain()
    }

    @Test
    fun loadArticlesCoroutine_LocationServices_OtherException() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        coEvery {
            locationSource.checkLocationServicesAvailability()
        } throws IllegalStateException()

        viewModel.loadArticlesCoroutine()
        advanceUntilIdle()

        verify {
            isLoadingObserver.onChanged(true)
            mapErrorEventObserver.onChanged(MapErrorItem.GOOGLE_SERVICES_ERROR)
        }
        verify(exactly = 0) {
            isLoadingObserver.onChanged(false)
            locationObserver.onChanged(latlon)
            mapViewDataObserver.onChanged(mapViewData)
        }
        coVerify {
            locationSource.checkLocationServicesAvailability()
        }
        coVerify(exactly = 0) {
            locationSource.getCurrentLocation()
            preferencesSource.getPreferencesFlow()
            wikiSource.loadArticleItems(preferences.languageCode, latlon, preferences.radius)
        }

        Dispatchers.resetMain()
    }

    @Test
    fun loadArticlesCoroutine_CurrentLocation_Exception() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        coEvery { locationSource.getCurrentLocation() } throws IllegalStateException()

        viewModel.loadArticlesCoroutine()
        advanceUntilIdle()

        verifyOrder {
            isLoadingObserver.onChanged(true)
            mapErrorEventObserver.onChanged(MapErrorItem.LOCATION_ERROR)
        }
        verify(exactly = 0) {
            locationObserver.onChanged(latlon)
            mapActionEventObserver.onChanged(CenterOnLocationAction(latlon))
            mapViewDataObserver.onChanged(mapViewData)
            isLoadingObserver.onChanged(false)
        }
        coVerify {
            locationSource.checkLocationServicesAvailability()
            locationSource.getCurrentLocation()
        }
        coVerify(exactly = 0) {
            preferencesSource.getPreferencesFlow()
            wikiSource.loadArticleItems(preferences.languageCode, latlon, preferences.radius)
        }

        Dispatchers.resetMain()
    }

    @Test
    fun loadArticlesCoroutine_CurrentLocation_Timeout() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        coEvery { locationSource.getCurrentLocation() } coAnswers {
            delay(TimeUnit.SECONDS.toMillis(100))
            location
        }

        viewModel.loadArticlesCoroutine()
        advanceUntilIdle()

        verifyOrder {
            isLoadingObserver.onChanged(true)
            mapErrorEventObserver.onChanged(MapErrorItem.LOCATION_ERROR)
        }
        verify(exactly = 0) {
            locationObserver.onChanged(latlon)
            mapActionEventObserver.onChanged(CenterOnLocationAction(latlon))
            mapViewDataObserver.onChanged(mapViewData)
            isLoadingObserver.onChanged(false)
        }
        coVerify {
            locationSource.checkLocationServicesAvailability()
            locationSource.getCurrentLocation()
        }
        coVerify(exactly = 0) {
            preferencesSource.getPreferencesFlow()
            wikiSource.loadArticleItems(preferences.languageCode, latlon, preferences.radius)
        }

        Dispatchers.resetMain()
    }

    @Test
    fun loadArticlesCoroutine_LoadArticles_Exception() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        coEvery { wikiSource.loadArticleItems(any(), any(), any()) } throws IOException()

        viewModel.loadArticlesCoroutine()
        advanceUntilIdle()

        verifyOrder {
            isLoadingObserver.onChanged(true)
            locationObserver.onChanged(latlon)
            mapErrorEventObserver.onChanged(MapErrorItem.ARTICLES_ERROR)
        }
        verify(exactly = 0) {
            mapActionEventObserver.onChanged(CenterOnLocationAction(latlon))
            mapViewDataObserver.onChanged(mapViewData)
            isLoadingObserver.onChanged(false)
        }
        coVerifyOrder {
            locationSource.checkLocationServicesAvailability()
            locationSource.getCurrentLocation()
            preferencesSource.getPreferencesFlow()
            wikiSource.loadArticleItems(preferences.languageCode, latlon, preferences.radius)
        }

        Dispatchers.resetMain()
    }

    @Test
    fun loadArticlesCoroutine_LoadArticles_PreferencesUpdated() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        viewModel.loadArticlesCoroutine()
        advanceUntilIdle()

        val newPreferences = PreferencesBundle(500, "ru")
        preferencesFlow.value = newPreferences
        advanceUntilIdle()

        coVerifyOrder {
            wikiSource.loadArticleItems(preferences.languageCode, latlon, preferences.radius)
            wikiSource.loadArticleItems(newPreferences.languageCode, latlon, newPreferences.radius)
        }

        Dispatchers.resetMain()
    }

    @Test
    fun onPermissionsResult() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val permissionsGranted = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).associate { permission -> permission to true }

        viewModel.onPermissionsResult(permissionsGranted)
        advanceUntilIdle()

        verifyOrder {
            isLoadingObserver.onChanged(true)
            locationObserver.onChanged(latlon)
            mapActionEventObserver.onChanged(CenterOnLocationAction(latlon))
            mapViewDataObserver.onChanged(mapViewData)
            isLoadingObserver.onChanged(false)
        }
        verify(exactly = 0) {
            mapErrorEventObserver.onChanged(any())
        }
        coVerifyOrder {
            locationSource.checkLocationServicesAvailability()
            locationSource.getCurrentLocation()
            preferencesSource.getPreferencesFlow()
            wikiSource.loadArticleItems(preferences.languageCode, latlon, preferences.radius)
        }

        Dispatchers.resetMain()
    }

    @Test
    fun onPermissionsResult_LocationNotGranted() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val permissionsGranted = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).associate { permission -> permission to false }

        viewModel.onPermissionsResult(permissionsGranted)

        verify {
            mapErrorEventObserver.onChanged(MapErrorItem.LOCATION_PERMISSION_ERROR)
        }

        verify(exactly = 0) {
            locationObserver.onChanged(latlon)
            mapActionEventObserver.onChanged(CenterOnLocationAction(latlon))
            mapViewDataObserver.onChanged(mapViewData)
            isLoadingObserver.onChanged(false)
        }
        coVerify(exactly = 0) {
            locationSource.checkLocationServicesAvailability()
            locationSource.getCurrentLocation()
            preferencesSource.getPreferencesFlow()
            wikiSource.loadArticleItems(preferences.languageCode, latlon, preferences.radius)
        }

        Dispatchers.resetMain()
    }

    @Test
    fun onPermissionsResult_OnlyCoarseLocation() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val permissionsGranted = mapOf(
            Manifest.permission.ACCESS_COARSE_LOCATION to true,
            Manifest.permission.ACCESS_FINE_LOCATION to false,
            Manifest.permission.WRITE_EXTERNAL_STORAGE to true
        )

        viewModel.onPermissionsResult(permissionsGranted)
        advanceUntilIdle()

        verifyOrder {
            isLoadingObserver.onChanged(true)
            locationObserver.onChanged(latlon)
            mapActionEventObserver.onChanged(CenterOnLocationAction(latlon))
            mapViewDataObserver.onChanged(mapViewData)
            isLoadingObserver.onChanged(false)
        }
        verify(exactly = 0) {
            mapErrorEventObserver.onChanged(any())
        }
        coVerifyOrder {
            locationSource.checkLocationServicesAvailability()
            locationSource.getCurrentLocation()
            preferencesSource.getPreferencesFlow()
            wikiSource.loadArticleItems(preferences.languageCode, latlon, preferences.radius)
        }

        Dispatchers.resetMain()
    }

    @Test
    fun onMarkerSelected() {
        viewModel.onMarkerSelected(mapViewData.articlesOverlayData.first().toOverlayItem())

        verify {
            mapActionEventObserver.onChanged(
                ShowArticleSelectorAction(mapViewData.articlesOverlayData.first().items)
            )
        }
    }

    @Test
    fun onItemSelected() {
        viewModel.onItemSelected(ArticleItemCreator.createNormalList().first())

        verify {
            mapActionEventObserver.onChanged(
                NavigateToDetailsAction(ArticleItemCreator.createNormalList().first())
            )
        }
    }

    @Test
    fun onCenterOnUserLocationClicked() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        viewModel.loadArticlesCoroutine()
        advanceUntilIdle()

        viewModel.onCenterOnUserLocationClicked()

        verify {
            mapActionEventObserver.onChanged(
                CenterOnLocationAction(latlon)
            )
        }

        Dispatchers.resetMain()
    }

    @Test
    fun onZoomLevelChanged() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        viewModel.loadArticlesCoroutine()
        advanceUntilIdle()

        val newZoomLevel = 10.0
        viewModel.onZoomLevelChanged(newZoomLevel)

        val newMapViewData = MapViewData(
            clusterFactory.groupItemsIntoMarkers(
                newZoomLevel, mapViewData.articlesOverlayData.toViewData()
            )
        )
        verify {
            mapViewDataObserver.onChanged(newMapViewData)
        }

        Dispatchers.resetMain()
    }
}