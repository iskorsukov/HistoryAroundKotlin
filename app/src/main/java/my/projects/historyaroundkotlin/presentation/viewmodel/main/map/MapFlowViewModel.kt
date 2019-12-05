package my.projects.historyaroundkotlin.presentation.viewmodel.main.map

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.mock.Mockable
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultState
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles.ArticleItemViewData
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles.ArticlesErrorStatus
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles.ArticlesViewState
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.location.LocationErrorStatus
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.location.LocationViewState
import my.projects.historyaroundkotlin.service.api.WikiSource
import my.projects.historyaroundkotlin.service.location.LocationSource
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Mockable
class MapFlowViewModel @Inject constructor(private val locationSource: LocationSource, private val wikiSource: WikiSource, private val appContext: Context) : ViewModel() {

    private var locationUpdatesDisposable: Disposable? = null
    private var loadArticlesDisposable: Disposable? = null

    fun observeLocation(): LiveData<LocationViewState> {
        locationUpdatesDisposable?.dispose()

        val liveData = MutableLiveData<LocationViewState>()
        locationUpdatesDisposable = checkLocationServicesAvailable()
            .andThen(loadLocation())
            .subscribe({ location ->
                liveData.value = LocationViewState(LoadingResultState.content(), location)
            }, { throwable ->
                handleLocationError(throwable, liveData)
            })

        startLocationUpdates()

        return liveData
    }

    private fun checkLocationServicesAvailable(): Completable {
        return locationSource.checkLocationServicesAvailability().timeout(5, TimeUnit.SECONDS).onErrorResumeNext {
            Completable.error(IllegalStateException("Location Services unavailable"))
        }
    }

    private fun loadLocation(): Single<Location> {
        return locationSource.getLocationUpdatesObservable()
            .timeout(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate {
                stopLocationUpdates()
            }
            .doOnDispose(this::stopLocationUpdates)
            .firstOrError()
    }

    private fun handleLocationError(throwable: Throwable, liveData: MutableLiveData<LocationViewState>) {
        throwable.printStackTrace()
        if (throwable is IllegalStateException) {
            // location services check error
            liveData.value = LocationViewState(
                LoadingResultState.error(listOf(LocationErrorStatus.LOCATION_SERVICE_UNAVAILABLE)),
                null
            )
        } else {
            // location error
            liveData.value = LocationViewState(
                LoadingResultState.error(listOf(LocationErrorStatus.LOCATION_UNAVAILABLE)),
                null
            )
        }
    }

    fun loadArticles(location: Location): LiveData<ArticlesViewState> {
        loadArticlesDisposable?.dispose()

        val liveData = MutableLiveData<ArticlesViewState>()
        val radius = PreferenceManager.getDefaultSharedPreferences(appContext).getInt(appContext.getString(R.string.prefs_radius_key), 500)
        loadArticlesDisposable = loadArticleItems(location, radius)
            .subscribe ({ articleItems ->
                Log.i("TEST", "loaded ${articleItems.size} items")
                handleArticleItems(articleItems, liveData)
            }, { throwable ->
                handleArticlesError(throwable, liveData)
            })

        return liveData
    }

    private fun loadArticleItems(location: Location, radius: Int): Single<List<ArticleItem>> {
        return wikiSource.loadArticleItems(Pair(location.latitude, location.longitude), 500)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun handleArticleItems(articleItems: List<ArticleItem>, liveData: MutableLiveData<ArticlesViewState>) {
        val articleItemViewDataList = articleItems.map { item ->
            ArticleItemViewData(
                item,
                false
            )
        }
        liveData.value = ArticlesViewState(LoadingResultState.content(), articleItemViewDataList)
    }

    private fun handleArticlesError(throwable: Throwable, liveData: MutableLiveData<ArticlesViewState>) {
        throwable.printStackTrace()
        liveData.value = ArticlesViewState(LoadingResultState.error(listOf(ArticlesErrorStatus.CONNECTION_ERROR)), null)
    }

    private fun startLocationUpdates() {
        Log.i("TEST", "start location updates")
        locationSource.startLocationUpdates()
    }

    private fun stopLocationUpdates() {
        Log.i("TEST", "stop location updates")
        locationSource.stopLocationUpdates()
    }

    override fun onCleared() {
        locationUpdatesDisposable?.dispose()
        loadArticlesDisposable?.dispose()
        super.onCleared()
    }

}