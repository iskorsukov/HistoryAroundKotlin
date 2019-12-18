package my.projects.historyaroundkotlin.presentation.viewmodel.map

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import my.projects.historyaroundkotlin.mock.Mockable
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewaction.ViewAction
import my.projects.historyaroundkotlin.presentation.view.map.adapter.ArticleListItemListener
import my.projects.historyaroundkotlin.presentation.view.map.utils.groupItemsIntoMarkers
import my.projects.historyaroundkotlin.presentation.view.map.viewaction.CenterOnLocationAction
import my.projects.historyaroundkotlin.presentation.view.map.viewaction.NavigateToDetailsAction
import my.projects.historyaroundkotlin.presentation.view.map.viewaction.ShowArticleSelectorAction
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.MapErrorItem
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.MapViewState
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata.ArticleItemViewData
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata.ArticlesClusterItem
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata.ArticlesOverlayItem
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata.MapViewData
import my.projects.historyaroundkotlin.service.api.WikiSource
import my.projects.historyaroundkotlin.service.location.LocationSource
import my.projects.historyaroundkotlin.service.preferences.PreferencesSource
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Mockable
class MapFlowViewModel @Inject constructor(
    private val locationSource: LocationSource,
    private val wikiSource: WikiSource,
    private val preferencesSource: PreferencesSource
) : ViewModel(), ArticleListItemListener {

    companion object {
        const val DEFAULT_ZOOM_VALUE = 16.0
    }

    private var lastZoomValue = DEFAULT_ZOOM_VALUE

    private var loadArticlesDisposable: Disposable? = null

    val mapDataLiveData: LiveData<MapViewState> by lazy {
        MutableLiveData<MapViewState>().also {
            it.value = MapViewState(LCEState.LOADING, null, null)
            loadArticles()
        }
    }

    val mapActionLiveData: LiveEvent<ViewAction<*>> = LiveEvent()

    fun loadArticles() {
        loadArticlesDisposable?.dispose()

        var lastLoadedLocation: Location? = null

        startLocationUpdates()
        loadArticlesDisposable = checkLocationServicesAvailable()
            .andThen(loadLocation())
            .flatMapObservable {
                lastLoadedLocation = it
                loadArticles(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ articleItems ->
                if (lastLoadedLocation == null) {
                    handleError(IllegalArgumentException("No location found"))
                } else {
                    handleResult(lastLoadedLocation!!, articleItems)
                }
            }, { throwable ->
                handleError(throwable)
            })
    }

    private fun checkLocationServicesAvailable(): Completable {
        return locationSource.checkLocationServicesAvailability().timeout(5, TimeUnit.SECONDS).onErrorResumeNext {
            Completable.error(IllegalStateException("Location Services unavailable"))
        }
    }

    private fun loadLocation(): Single<Location> {
        return locationSource.getLastKnownLocation()
            .switchIfEmpty(locationSource.getLocationUpdatesObservable().firstOrError())
            .timeout(15, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEvent { _, _ -> stopLocationUpdates() }
            .doOnDispose(this::stopLocationUpdates)
    }

    private fun loadArticles(location: Location): Observable<List<ArticleItem>> {
        return preferencesSource.getRadiusPreference().flatMapSingle {
            loadArticleItems(location, it)
        }
    }

    private fun loadArticleItems(location: Location, radius: Int): Single<List<ArticleItem>> {
        return wikiSource.loadArticleItems(Pair(location.latitude, location.longitude), radius)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    // TODO define descriptive exception classes
    private fun handleError(throwable: Throwable) {
        throwable.printStackTrace()
        when (throwable) {
            is IllegalStateException -> // location services check error
                (mapDataLiveData as MutableLiveData).value =
                    MapViewState(
                        LCEState.ERROR,
                        null,
                        MapErrorItem.LOCATION_SERVICES_ERROR
                    )
            is IOException -> // IO error
                (mapDataLiveData as MutableLiveData).value =
                    MapViewState(
                        LCEState.ERROR,
                        null,
                        MapErrorItem.ARTICLES_ERROR
                    )
            else -> // location error
                (mapDataLiveData as MutableLiveData).value =
                    MapViewState(
                        LCEState.ERROR,
                        null,
                        MapErrorItem.LOCATION_ERROR
                    )
        }
    }

    private fun handleResult(location: Location, articleItems: List<ArticleItem>) {
        mapActionLiveData.value = CenterOnLocationAction(location.latitude to location.longitude)
        (mapDataLiveData as MutableLiveData).value =
            MapViewState(
                LCEState.CONTENT,
                MapViewData(
                    location.latitude to location.longitude,
                    groupItemsIntoMarkers(
                        lastZoomValue,
                        articleItems.map { ArticleItemViewData(it, false) }
                    )
                ),
                null
            )
    }

    private fun startLocationUpdates() {
        locationSource.startLocationUpdates()
    }

    private fun stopLocationUpdates() {
        locationSource.stopLocationUpdates()
    }

    fun onMarkerSelected(marker: ArticlesOverlayItem) {
        mapActionLiveData.value =
            ShowArticleSelectorAction(
                marker.articleItems
            )
    }

    override fun onItemSelected(articleItem: ArticleItem) {
        mapActionLiveData.value =
            NavigateToDetailsAction(
                articleItem
            )
    }

    fun onCenterOnUserLocationClicked() {
        mapDataLiveData.value?.content?.apply {
            mapActionLiveData.value =
                CenterOnLocationAction(
                    this.location
                )
        }
    }

    fun onZoomLevelChanged(zoomLevel: Double) {
        lastZoomValue = zoomLevel
        mapDataLiveData.value?.content?.apply {
            (mapDataLiveData as MutableLiveData).value =
                MapViewState(
                    LCEState.CONTENT,
                    MapViewData(
                        location,
                        groupItemsIntoMarkers(
                            lastZoomValue,
                            articlesOverlayData.toViewData()
                        )

                    ),
                    null
                )
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