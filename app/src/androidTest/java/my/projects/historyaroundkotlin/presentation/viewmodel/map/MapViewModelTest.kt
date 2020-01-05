package my.projects.historyaroundkotlin.presentation.viewmodel.map

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.map.viewaction.CenterOnLocationAction
import my.projects.historyaroundkotlin.presentation.view.map.viewaction.NavigateToDetailsAction
import my.projects.historyaroundkotlin.presentation.view.map.viewaction.ShowArticleSelectorAction
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.MapErrorItem
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata.ArticleItemViewData
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata.ArticlesClusterItem
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata.toOverlayItem
import my.projects.historyaroundkotlin.service.api.WikiSource
import my.projects.historyaroundkotlin.service.location.LocationSource
import my.projects.historyaroundkotlin.service.preferences.PreferencesSource
import my.projects.historyaroundkotlin.utils.MockitoUtil
import my.projects.historyaroundkotlin.utils.waitForValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.io.IOException
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class MapViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MapViewModel

    private val locationSource = Mockito.mock(LocationSource::class.java)
    private val wikiSource = Mockito.mock(WikiSource::class.java)
    private val preferencesSource = Mockito.mock(PreferencesSource::class.java)

    @Before
    fun setupViewModel() {
        viewModel = Mockito.spy(MapViewModel(locationSource, wikiSource, preferencesSource))
    }

    private fun getSampleLocation(): Location {
        return Location("test").also {
            it.latitude = 1.0
            it.longitude = 1.0
        }
    }

    private fun getSampleRadius(): Int {
        return 500
    }

    private fun getSampleArticles(): List<ArticleItem> {
        val first = ArticleItem(
            "1",
            "First",
            "First description",
            1.0 to 1.0,
            null
        )
        val second = ArticleItem(
            "1",
            "Second",
            "Second description",
            100.0 to 100.0,
            null
        )
        return listOf(first, second)
    }

    private fun getSampleClusters(): List<ArticlesClusterItem> {
        return getSampleArticles().map { ArticlesClusterItem(it.latlng, listOf(ArticleItemViewData(it, false))) }
    }

    @Before
    fun setupMocks() {
        Mockito.`when`(preferencesSource.getRadiusPreference()).thenReturn(Observable.just(getSampleRadius()))
        Mockito.`when`(locationSource.checkLocationServicesAvailability()).thenReturn(Completable.complete())
        Mockito.`when`(locationSource.getLocationUpdatesObservable()).thenReturn(Observable.never())
        Mockito.`when`(locationSource.getLastKnownLocation()).thenReturn(Maybe.just(getSampleLocation()))
        Mockito.`when`(wikiSource.loadArticleItems(MockitoUtil.any(), ArgumentMatchers.anyInt())).thenReturn(Single.just(getSampleArticles()))
    }

    private fun pushDelayData() {
        Mockito.`when`(locationSource.getLastKnownLocation()).thenReturn(Maybe.never())
    }

    private fun pushLocationServicesErrorData() {
        Mockito.`when`(locationSource.checkLocationServicesAvailability()).thenReturn(Completable.error(IllegalStateException()))
    }

    private fun pushLocationErrorData() {
        Mockito.`when`(locationSource.getLastKnownLocation()).thenReturn(Maybe.error(IllegalStateException()))
    }

    private fun pushArticlesErrorData() {
        Mockito.`when`(wikiSource.loadArticleItems(MockitoUtil.any(), ArgumentMatchers.anyInt())).thenReturn(Single.error(IOException()))
    }

    @Test
    fun pushesLoadingStateOnObserve() {
        pushDelayData()

        val liveData = viewModel.mapDataLiveData

        assertEquals(LCEState.LOADING, waitForValue(liveData).lceState)
    }

    @Test
    fun loadsArticlesOnObserve() {
        val liveData = viewModel.mapDataLiveData
        TimeUnit.SECONDS.sleep(2)

        Mockito.verify(wikiSource).loadArticleItems(MockitoUtil.any(), ArgumentMatchers.anyInt())
    }

    @Test
    fun pushesContentStateOnArticlesLoaded() {
        val liveData = viewModel.mapDataLiveData
        TimeUnit.SECONDS.sleep(2)

        val viewState = waitForValue(liveData)
        assertEquals(LCEState.CONTENT, viewState.lceState)
        assertEquals(getSampleClusters().size, viewState.content!!.articlesOverlayData.size)
    }

    @Test
    fun pushesErrorStateOnLocationServicesError() {
        pushLocationServicesErrorData()

        val liveData = viewModel.mapDataLiveData
        TimeUnit.SECONDS.sleep(2)

        val viewState = waitForValue(liveData)
        assertEquals(LCEState.ERROR, viewState.lceState)
        assertEquals(MapErrorItem.LOCATION_SERVICES_ERROR, viewState.error)
    }

    @Test
    fun pushesErrorStateOnLocationError() {
        pushLocationErrorData()

        val liveData = viewModel.mapDataLiveData
        TimeUnit.SECONDS.sleep(2)

        val viewState = waitForValue(liveData)
        assertEquals(LCEState.ERROR, viewState.lceState)
        assertEquals(MapErrorItem.LOCATION_ERROR, viewState.error)
    }

    @Test
    fun pushesErrorStateOnArticlesError() {
        pushArticlesErrorData()

        val liveData = viewModel.mapDataLiveData
        TimeUnit.SECONDS.sleep(2)

        val viewState = waitForValue(liveData)
        assertEquals(LCEState.ERROR, viewState.lceState)
        assertEquals(MapErrorItem.ARTICLES_ERROR, viewState.error)
    }

    @Test
    fun callsSourceClassesOnLoadingArticles() {
        val liveData = viewModel.mapDataLiveData
        TimeUnit.SECONDS.sleep(2)

        Mockito.verify(locationSource).checkLocationServicesAvailability()
        Mockito.verify(locationSource).getLastKnownLocation()
        Mockito.verify(preferencesSource).getRadiusPreference()
        Mockito.verify(wikiSource).loadArticleItems(MockitoUtil.any(), ArgumentMatchers.anyInt())
    }

    @Test
    fun pushesCenterOnLocationActionOnEvent() {
        viewModel.mapDataLiveData
        TimeUnit.SECONDS.sleep(2)

        val liveData = viewModel.mapActionLiveData
        viewModel.onCenterOnUserLocationClicked()

        assertTrue(waitForValue(liveData) is CenterOnLocationAction)
    }

    @Test
    fun pushesNavigateToDetailsActionOnArticleSelected() {
        viewModel.mapDataLiveData
        TimeUnit.SECONDS.sleep(2)

        val liveData = viewModel.mapActionLiveData
        viewModel.onItemSelected(getSampleArticles()[0])

        assertTrue(waitForValue(liveData) is NavigateToDetailsAction)
    }

    @Test
    fun pushesShowArticleSelectorActionOnMarkerSelected() {
        viewModel.mapDataLiveData
        TimeUnit.SECONDS.sleep(2)

        val liveData = viewModel.mapActionLiveData
        viewModel.onMarkerSelected(getSampleClusters()[0].toOverlayItem())

        assertTrue(waitForValue(liveData) is ShowArticleSelectorAction)
    }
}