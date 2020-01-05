package my.projects.historyaroundkotlin.presentation.viewmodel.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.Completable
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import my.projects.historyaroundkotlin.model.detail.ArticleDetails
import my.projects.historyaroundkotlin.model.detail.toArticleItem
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.detail.viewaction.OpenInMapAction
import my.projects.historyaroundkotlin.presentation.view.detail.viewaction.ViewInBrowserAction
import my.projects.historyaroundkotlin.presentation.view.detail.viewstate.viewdata.DetailViewData
import my.projects.historyaroundkotlin.presentation.viewmodel.detail.DetailViewModel
import my.projects.historyaroundkotlin.service.api.WikiSource
import my.projects.historyaroundkotlin.service.favorites.FavoritesSource
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
class DetailsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DetailViewModel
    private val wikiSource = Mockito.mock(WikiSource::class.java)
    private val favoritesSource = Mockito.mock(FavoritesSource::class.java)

    @Before
    fun initViewModel() {
        viewModel = DetailViewModel(wikiSource, favoritesSource)
    }

    private fun getSampleData(): DetailViewData {
        val item = ArticleDetails(
            1L,
            "Sample",
            "Sample extract",
            null,
            1.0 to 1.0,
            "https://url.com"
        )
        return DetailViewData(item, false)
    }

    private fun pushSampleData() {
        Mockito.`when`(wikiSource.loadArticleDetails(ArgumentMatchers.anyString())).thenReturn(Single.just(getSampleData().item))
        Mockito.`when`(favoritesSource.isFavorite(ArgumentMatchers.anyString())).thenReturn(Single.just(getSampleData().isFavorite))
    }

    @Test
    fun callsSourceWhenLoadingDetails() {
        pushSampleData()

        viewModel.loadArticleDetails("1")

        Mockito.verify(wikiSource).loadArticleDetails("1")
        Mockito.verify(favoritesSource).isFavorite("1")
    }

    private fun pushDelayData() {
        Mockito.`when`(wikiSource.loadArticleDetails(ArgumentMatchers.anyString())).thenReturn(Single.never())
        Mockito.`when`(favoritesSource.isFavorite(ArgumentMatchers.anyString())).thenReturn(Single.just(false))
    }

    @Test
    fun pushesLoadingStateWhenLoadingDetails() {
        pushDelayData()

        val liveData = viewModel.viewStateLiveData
        viewModel.loadArticleDetails("1")

        assertEquals(LCEState.LOADING, waitForValue(liveData).lceState)
    }

    @Test
    fun pushesContentStateWhenLoadedDetails() {
        pushSampleData()

        val liveData = viewModel.viewStateLiveData
        viewModel.loadArticleDetails("1")
        TimeUnit.SECONDS.sleep(2)

        val viewState = liveData.value!!
        assertEquals(LCEState.CONTENT, viewState.lceState)
        assertEquals(getSampleData(), viewState.content)
    }

    private fun pushErrorData() {
        Mockito.`when`(wikiSource.loadArticleDetails(ArgumentMatchers.anyString())).thenReturn(Single.error(IOException()))
        Mockito.`when`(favoritesSource.isFavorite(ArgumentMatchers.anyString())).thenReturn(Single.just(false))
    }

    @Test
    fun pushesErrorStateOnError() {
        pushErrorData()

        val liveData = viewModel.viewStateLiveData
        viewModel.loadArticleDetails("1")
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

        val liveData = viewModel.viewStateLiveData
        viewModel.addToFavorites(getSampleData().item)

        val viewState = waitForValue(liveData)
        assertEquals(LCEState.CONTENT, viewState.lceState)
        assertEquals(true, viewState.content!!.isFavorite)
    }

    @Test
    fun pushesActionOnMapButtonClicked() {
        val liveData = viewModel.viewActionLiveData

        viewModel.onOpenInMapButtonClicked(1.0 to 1.0)

        assertTrue(liveData.value!! is OpenInMapAction)
    }

    @Test
    fun pushesActionOnOpenInBrowserClicked() {
        val liveData = viewModel.viewActionLiveData

        viewModel.onViewInBrowserButtonClicked("")

        assertTrue(liveData.value!! is ViewInBrowserAction)
    }
}