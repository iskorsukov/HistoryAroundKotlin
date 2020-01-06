package my.projects.historyaroundkotlin.presentation.viewmodel.favorites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.Observable
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.favorites.viewaction.NavigateToDetailsAction
import my.projects.historyaroundkotlin.presentation.viewmodel.favourites.FavouritesViewModel
import my.projects.historyaroundkotlin.service.favorites.FavoritesSource
import my.projects.historyaroundkotlin.utils.waitForValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.io.IOException
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class FavoritesViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: FavouritesViewModel
    private val favoritesSource = Mockito.mock(FavoritesSource::class.java)

    @Before
    fun setupViewModel() {
        viewModel = FavouritesViewModel(favoritesSource)
    }

    private fun getSampleData(): List<ArticleItem> {
        val first = ArticleItem(
            "1",
            "First",
            "First description",
            1.0 to 1.0,
            null,
            "en"
        )
        val second = ArticleItem(
            "1",
            "Second",
            "Second description",
            1.0 to 1.0,
            null,
            "en"
        )
        return listOf(first, second)
    }

    private fun pushSampleData() {
        Mockito.`when`(favoritesSource.getFavoriteArticles()).thenReturn(Observable.just(getSampleData()))
    }

    private fun pushDelayData() {
        Mockito.`when`(favoritesSource.getFavoriteArticles()).thenReturn(Observable.never())
    }

    private fun pushErrorData() {
        Mockito.`when`(favoritesSource.getFavoriteArticles()).thenReturn(Observable.error(IOException()))
    }

    @Test
    fun pushesLoadingStateOnObserve() {
        pushDelayData()

        val liveData = viewModel.viewStateLiveData

        assertEquals(LCEState.LOADING, waitForValue(liveData).lceState)
    }

    @Test
    fun callsSourceOnObserveLoad() {
        pushSampleData()

        val liveData = viewModel.viewStateLiveData
        TimeUnit.SECONDS.sleep(2) // wait for operations to execute

        Mockito.verify(favoritesSource).getFavoriteArticles()
    }

    @Test
    fun pushesContentWhenLoaded() {
        pushSampleData()

        val liveData = viewModel.viewStateLiveData
        TimeUnit.SECONDS.sleep(2) // wait for value to change to content

        val viewState = waitForValue(liveData)
        assertEquals(LCEState.CONTENT, viewState.lceState)
        assertEquals(getSampleData().size, viewState.content!!.items.size)
    }

    @Test
    fun pushesErrorOnError() {
        pushErrorData()

        val liveData = viewModel.viewStateLiveData
        TimeUnit.SECONDS.sleep(2) // wait for value to change to content

        val viewState = waitForValue(liveData)
        assertEquals(LCEState.ERROR, viewState.lceState)
    }

    @Test
    fun pushesNavigateActionOnItemSelected() {
        val liveData = viewModel.viewActionLiveData
        viewModel.onItemSelected(getSampleData()[0])

        val action = waitForValue(liveData)
        assertTrue(action is NavigateToDetailsAction)
    }

}