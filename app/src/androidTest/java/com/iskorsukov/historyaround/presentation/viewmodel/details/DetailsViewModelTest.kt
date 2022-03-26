package com.iskorsukov.historyaround.presentation.viewmodel.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import com.iskorsukov.historyaround.model.detail.ArticleDetails
import com.iskorsukov.historyaround.model.detail.toArticleItem
import com.iskorsukov.historyaround.model.preferences.PreferencesBundle
import com.iskorsukov.historyaround.presentation.view.common.viewstate.LCEState
import com.iskorsukov.historyaround.presentation.view.detail.viewaction.OpenInMapAction
import com.iskorsukov.historyaround.presentation.view.detail.viewaction.ViewInBrowserAction
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.viewdata.DetailViewData
import com.iskorsukov.historyaround.presentation.viewmodel.detail.DetailViewModel
import com.iskorsukov.historyaround.service.api.WikiSource
import com.iskorsukov.historyaround.service.favorites.FavoritesSource
import com.iskorsukov.historyaround.service.preferences.PreferencesSource
import com.iskorsukov.historyaround.utils.MockitoUtil
import com.iskorsukov.historyaround.utils.waitForValue
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
class DetailsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DetailViewModel
    private val wikiSource = Mockito.mock(WikiSource::class.java)
    private val favoritesSource = Mockito.mock(FavoritesSource::class.java)
    private val preferencesSource = Mockito.mock(PreferencesSource::class.java).also {
        Mockito.`when`(it.getPreferences()).thenReturn(Observable.just(getSamplePrefs()))
    }

    @Before
    fun initViewModel() {
        viewModel = DetailViewModel(wikiSource, favoritesSource, preferencesSource)
    }

    private fun getSamplePrefs(): PreferencesBundle {
        return PreferencesBundle(500, "en")
    }

    private fun getSampleData(): DetailViewData {
        val item = ArticleDetails(
            1L,
            "Sample",
            "Sample extract",
            null,
            1.0 to 1.0,
            "https://url.com",
            "en"
        )
        return DetailViewData(item, false)
    }

    private fun pushSampleData() {
        Mockito.`when`(wikiSource.loadArticleDetails(MockitoUtil.any(), ArgumentMatchers.anyString())).thenReturn(Single.just(getSampleData().item))
        Mockito.`when`(favoritesSource.isFavorite(ArgumentMatchers.anyString())).thenReturn(Single.just(getSampleData().isFavorite))
    }

    @Test
    fun callsSourceWhenLoadingDetails() {
        pushSampleData()

        viewModel.loadArticleDetails("1", "en")

        Mockito.verify(wikiSource).loadArticleDetails("en", "1")
        Mockito.verify(favoritesSource).isFavorite("1")
    }

    private fun pushDelayData() {
        Mockito.`when`(wikiSource.loadArticleDetails(MockitoUtil.any(), ArgumentMatchers.anyString())).thenReturn(Single.never())
        Mockito.`when`(favoritesSource.isFavorite(ArgumentMatchers.anyString())).thenReturn(Single.just(false))
    }

    @Test
    fun pushesLoadingStateWhenLoadingDetails() {
        pushDelayData()

        val liveData = viewModel.detailDataLiveData
        viewModel.loadArticleDetails("1", "en")

        assertEquals(LCEState.LOADING, waitForValue(liveData).lceState)
    }

    @Test
    fun pushesContentStateWhenLoadedDetails() {
        pushSampleData()

        val liveData = viewModel.detailDataLiveData
        viewModel.loadArticleDetails("1", "en")
        TimeUnit.SECONDS.sleep(2)

        val viewState = liveData.value!!
        assertEquals(LCEState.CONTENT, viewState.lceState)
        assertEquals(getSampleData(), viewState.content)
    }

    private fun pushErrorData() {
        Mockito.`when`(wikiSource.loadArticleDetails(MockitoUtil.any(), ArgumentMatchers.anyString())).thenReturn(Single.error(IOException()))
        Mockito.`when`(favoritesSource.isFavorite(ArgumentMatchers.anyString())).thenReturn(Single.just(false))
    }

    @Test
    fun pushesErrorStateOnError() {
        pushErrorData()

        val liveData = viewModel.detailDataLiveData
        viewModel.loadArticleDetails("1", "en")
        TimeUnit.SECONDS.sleep(2)

        val viewState = liveData.value!!
        assertEquals(LCEState.ERROR, viewState.lceState)
    }

    @Test
    fun callsSourceOnAddingToFavorites() {
        Mockito.`when`(favoritesSource.addToFavorites(MockitoUtil.any())).thenReturn(Completable.complete())

        viewModel.addToFavorites(getSampleData().item)

        Mockito.verify(favoritesSource).addToFavorites(getSampleData().item.toArticleItem())
    }

    @Test
    fun callsSourceOnRemovingFromFavorites() {
        Mockito.`when`(favoritesSource.removeFromFavorites(MockitoUtil.any())).thenReturn(Completable.complete())

        viewModel.removeFromFavorites(getSampleData().item)

        Mockito.verify(favoritesSource).removeFromFavorites(getSampleData().item.toArticleItem())
    }

    @Test
    fun changesFavoriteStateAfterFavoritesAction() {
        Mockito.`when`(favoritesSource.addToFavorites(MockitoUtil.any())).thenReturn(Completable.complete())

        val liveData = viewModel.detailDataLiveData
        viewModel.addToFavorites(getSampleData().item)

        val viewState = waitForValue(liveData)
        assertEquals(LCEState.CONTENT, viewState.lceState)
        assertEquals(true, viewState.content!!.isFavorite)
    }

    @Test
    fun pushesActionOnMapButtonClicked() {
        val liveData = viewModel.detailActionLiveEvent

        viewModel.onOpenInMapButtonClicked(1.0 to 1.0)

        assertTrue(liveData.value!! is OpenInMapAction)
    }

    @Test
    fun pushesActionOnOpenInBrowserClicked() {
        val liveData = viewModel.detailActionLiveEvent

        viewModel.onViewInBrowserButtonClicked("")

        assertTrue(liveData.value!! is ViewInBrowserAction)
    }
}