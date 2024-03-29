package com.iskorsukov.historyaround.presentation.viewmodel.map

import android.Manifest
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.hadilq.liveevent.LiveEvent
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.model.preferences.PreferencesBundle
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.map.adapter.ArticleListItemListener
import com.iskorsukov.historyaround.presentation.view.map.utils.ArticlesClusterFactory
import com.iskorsukov.historyaround.presentation.view.map.utils.ArticlesOverlayListener
import com.iskorsukov.historyaround.presentation.view.map.utils.ZoomLevelListener
import com.iskorsukov.historyaround.presentation.view.map.utils.toViewData
import com.iskorsukov.historyaround.presentation.view.map.viewaction.CenterOnLocationAction
import com.iskorsukov.historyaround.presentation.view.map.viewaction.NavigateToDetailsAction
import com.iskorsukov.historyaround.presentation.view.map.viewaction.ResolveLocationServicesAction
import com.iskorsukov.historyaround.presentation.view.map.viewaction.ShowArticleSelectorAction
import com.iskorsukov.historyaround.presentation.view.map.viewstate.MapErrorItem
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticleItemViewData
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticlesClusterItem
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticlesOverlayItem
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.MapViewData
import com.iskorsukov.historyaround.presentation.viewmodel.map.throwable.ArticlesErrorThrowable
import com.iskorsukov.historyaround.presentation.viewmodel.map.throwable.LocationErrorThrowable
import com.iskorsukov.historyaround.presentation.viewmodel.map.throwable.LocationServicesErrorThrowable
import com.iskorsukov.historyaround.service.api.WikiSource
import com.iskorsukov.historyaround.service.location.LocationSource
import com.iskorsukov.historyaround.service.preferences.PreferencesSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationSource: LocationSource,
    private val wikiSource: WikiSource,
    private val preferencesSource: PreferencesSource,
    private val clusterFactory: ArticlesClusterFactory
) : ViewModel(),
    ArticleListItemListener,
    ArticlesOverlayListener.ArticleMarkerListener,
    ZoomLevelListener.OnZoomLevelChangedListener {

    companion object {
        const val DEFAULT_ZOOM_VALUE = 16.0
    }

    private var lastZoomValue = DEFAULT_ZOOM_VALUE

    private val _mapLocationLiveData = MutableLiveData<Pair<Double, Double>>()
    val mapLocationLiveData: LiveData<Pair<Double, Double>>
        get() = _mapLocationLiveData

    private val _mapDataLiveData = MutableLiveData<MapViewData>()
    val mapDataLiveData: LiveData<MapViewData>
        get() = _mapDataLiveData

    private val _mapIsLoadingLiveData = MutableLiveData(true)
    val mapIsLoadingLiveData: LiveData<Boolean>
        get() = _mapIsLoadingLiveData

    val mapErrorLiveEvent: LiveEvent<MapErrorItem> = LiveEvent()

    val mapActionLiveEvent: LiveEvent<ViewAction<*>> = LiveEvent()

    fun loadArticlesCoroutine() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable -> handleError(throwable) }
        viewModelScope.launch(exceptionHandler) {
            _mapIsLoadingLiveData.value = true

            checkGoogleLocationServices()
            val location = loadLocation()
            _mapLocationLiveData.value = location.latitude to location.longitude

            preferencesSource.getPreferencesFlow().collect { preferences ->
                handleResult(loadArticles(location, preferences))
                _mapIsLoadingLiveData.value = false
            }
        }
    }

    private suspend fun checkGoogleLocationServices() {
        try {
            locationSource.checkLocationServicesAvailability()
        } catch (apiException: ApiException) {
            throw apiException
        } catch (otherException: Exception) {
            otherException.printStackTrace()
            throw ApiException(Status(LocationSettingsStatusCodes.INTERRUPTED))
        }
    }

    private suspend fun loadLocation(): Location {
        return withTimeout(TimeUnit.SECONDS.toMillis(15)) {
            try {
                locationSource.getCurrentLocation()
            } catch (e: Exception) {
                throw LocationErrorThrowable(e)
            }
        }
    }

    private suspend fun loadArticles(location: Location, preferences: PreferencesBundle): List<ArticleItem> {
        return try {
            wikiSource.loadArticleItems(
                preferences.languageCode,
                Pair(location.latitude, location.longitude),
                preferences.radius
            )
        } catch (e: Exception) {
            throw ArticlesErrorThrowable(e)
        }
    }

    private fun handleError(throwable: Throwable) {
        throwable.printStackTrace()
        when (throwable) {
            is LocationServicesErrorThrowable ->
                mapErrorLiveEvent.value = MapErrorItem.LOCATION_SERVICES_ERROR
            is LocationErrorThrowable ->
                mapErrorLiveEvent.value = MapErrorItem.LOCATION_ERROR
            is ApiException -> {
                if (throwable.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    mapActionLiveEvent.value = ResolveLocationServicesAction(throwable as ResolvableApiException)
                } else {
                    mapErrorLiveEvent.value = MapErrorItem.GOOGLE_SERVICES_ERROR
                }
            }
            else ->
                mapErrorLiveEvent.value = MapErrorItem.ARTICLES_ERROR
        }
    }

    private fun handleResult(articleItems: List<ArticleItem>) {
        if (mapLocationLiveData.value == null) return
        mapActionLiveEvent.value = CenterOnLocationAction(mapLocationLiveData.value!!)
        _mapDataLiveData.value =
            MapViewData(
                clusterFactory.groupItemsIntoMarkers(
                    lastZoomValue,
                    articleItems.map { ArticleItemViewData(it, false) }
                )
            )
    }

    override fun onMarkerSelected(item: ArticlesOverlayItem) {
        mapActionLiveEvent.value = ShowArticleSelectorAction(item.articleItems)
    }

    override fun onItemSelected(articleItem: ArticleItem) {
        mapActionLiveEvent.value = NavigateToDetailsAction(articleItem)
    }

    fun onCenterOnUserLocationClicked() {
        mapLocationLiveData.value?.apply {
            mapActionLiveEvent.value = CenterOnLocationAction(this)
        }
    }

    override fun onZoomLevelChanged(zoomLevel: Double) {
        lastZoomValue = zoomLevel
        mapDataLiveData.value?.apply {
            _mapDataLiveData.value =
                MapViewData(
                    clusterFactory.groupItemsIntoMarkers(
                        lastZoomValue,
                        articlesOverlayData.toViewData()
                    )
                )
        }
    }

    fun onPermissionsResult(permissions: Map<String, Boolean>) {
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (locationPermissions.any { permissions[it] == true }) {
            // at least one location permission granted
            loadArticlesCoroutine()
        } else {
            mapErrorLiveEvent.value = MapErrorItem.LOCATION_PERMISSION_ERROR
        }
    }
}