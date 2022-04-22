package com.iskorsukov.historyaround.presentation.viewmodel.map

import android.Manifest
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.Task
import com.hadilq.liveevent.LiveEvent
import com.iskorsukov.historyaround.mock.Mockable
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.model.preferences.PreferencesBundle
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.map.adapter.ArticleListItemListener
import com.iskorsukov.historyaround.presentation.view.map.utils.ArticlesOverlayListener
import com.iskorsukov.historyaround.presentation.view.map.utils.ZoomLevelListener
import com.iskorsukov.historyaround.presentation.view.map.utils.groupItemsIntoMarkers
import com.iskorsukov.historyaround.presentation.view.map.viewaction.CenterOnLocationAction
import com.iskorsukov.historyaround.presentation.view.map.viewaction.NavigateToDetailsAction
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
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Mockable
class MapViewModel @Inject constructor(
    private val locationSource: LocationSource,
    private val wikiSource: WikiSource,
    private val preferencesSource: PreferencesSource
) : ViewModel(), ArticleListItemListener, ArticlesOverlayListener.ArticleMarkerListener, ZoomLevelListener.OnZoomLevelChangedListener {

    companion object {
        const val DEFAULT_ZOOM_VALUE = 16.0
    }

    private var lastZoomValue = DEFAULT_ZOOM_VALUE

    private var loadArticlesDisposable: Disposable? = null

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

    fun checkLocationServicesAvailability(): Task<LocationSettingsResponse> {
        return locationSource.checkLocationServicesAvailability()
    }

    fun loadArticles() {
        _mapIsLoadingLiveData.value = true
        
        loadArticlesDisposable?.dispose()

        loadArticlesDisposable = loadLocation()
            .flatMapObservable {
                _mapLocationLiveData.value = it.latitude to it.longitude
                loadArticles(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ articleItems ->
                if (mapLocationLiveData.value == null) {
                    handleError(LocationErrorThrowable())
                } else {
                    handleResult(articleItems)
                }
                _mapIsLoadingLiveData.value = false
            }, { throwable ->
                handleError(throwable)
                _mapIsLoadingLiveData.value = false
            })
    }

    private fun loadLocation(): Single<Location> {
        return locationSource.getCurrentLocation()
            .toSingle()
            .timeout(15, TimeUnit.SECONDS)
            .onErrorResumeNext {
                Single.error(LocationErrorThrowable())
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun loadArticles(location: Location): Observable<List<ArticleItem>> {
        return preferencesSource.getPreferences().flatMapSingle {
            loadArticleItems(location, it)
        }.onErrorResumeNext { throwable: Throwable ->
            throwable.printStackTrace()
            Observable.error(ArticlesErrorThrowable())
        }
    }

    private fun loadArticleItems(location: Location, preferences: PreferencesBundle): Single<List<ArticleItem>> {
        return wikiSource.loadArticleItems(preferences.languageCode, Pair(location.latitude, location.longitude), preferences.radius)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun handleError(throwable: Throwable) {
        throwable.printStackTrace()
        when (throwable) {
            is LocationServicesErrorThrowable ->
                mapErrorLiveEvent.value = MapErrorItem.LOCATION_SERVICES_ERROR
            is LocationErrorThrowable ->
                mapErrorLiveEvent.value = MapErrorItem.LOCATION_ERROR
            else ->
                mapErrorLiveEvent.value = MapErrorItem.ARTICLES_ERROR
        }
    }

    private fun handleResult(articleItems: List<ArticleItem>) {
        if (mapLocationLiveData.value == null) return
        mapActionLiveEvent.value = CenterOnLocationAction(mapLocationLiveData.value!!)
        _mapDataLiveData.value =
            MapViewData(
                groupItemsIntoMarkers(
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
                    groupItemsIntoMarkers(
                        lastZoomValue,
                        articlesOverlayData.toViewData()
                    )
                )
        }
    }

    fun onRefresh() {
        loadArticles()
    }

    fun onPermissionsResult(permissions: Map<String, Boolean>) {
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (locationPermissions.any { permissions[it] == true }) {
            // at least one location permission granted
            loadArticles()
        } else {
            mapErrorLiveEvent.value = MapErrorItem.LOCATION_PERMISSION_ERROR
        }
    }

    private fun List<ArticlesClusterItem>.toViewData(): List<ArticleItemViewData> {
        return this.foldRight(HashSet(), { overlayItem: ArticlesClusterItem, set: HashSet<ArticleItemViewData> ->
            set.addAll(overlayItem.items)
            set
        }).toList()
    }

    override fun onCleared() {
        loadArticlesDisposable?.dispose()
        super.onCleared()
    }

}