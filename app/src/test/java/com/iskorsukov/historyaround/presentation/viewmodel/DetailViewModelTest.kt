package com.iskorsukov.historyaround.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import com.iskorsukov.historyaround.model.ArticleDetailsCreator
import com.iskorsukov.historyaround.model.ArticleItemCreator
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.model.detail.toArticleItem
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.detail.viewaction.OpenInMapAction
import com.iskorsukov.historyaround.presentation.view.detail.viewaction.ViewInBrowserAction
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.DetailErrorItem
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.viewdata.DetailViewData
import com.iskorsukov.historyaround.presentation.viewmodel.detail.DetailViewModel
import com.iskorsukov.historyaround.service.api.WikiSource
import com.iskorsukov.historyaround.service.favorites.FavoritesSource
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val wikiSource: WikiSource = mockk()
    private val favoritesSource: FavoritesSource = mockk(relaxed = true)

    private val viewModel = DetailViewModel(wikiSource, favoritesSource)

    private val isLoadingObserver: Observer<Boolean> = mockk(relaxed = true)
    private val viewDataObserver: Observer<DetailViewData> = mockk(relaxed = true)
    private val actionEventObserver: Observer<ViewAction<*>> = mockk(relaxed = true)
    private val errorEventObserver: Observer<DetailErrorItem> = mockk(relaxed = true)

    private val favoritesFlow: MutableStateFlow<List<ArticleItem>> =
        MutableStateFlow(ArticleItemCreator.createNormalList())

    private val pageId = "1"
    private val languageCode = "en"

    @Before
    fun observeData() {
        viewModel.detailIsLoadingLiveData.observeForever(isLoadingObserver)
        viewModel.detailDataLiveData.observeForever(viewDataObserver)
        viewModel.detailActionLiveEvent.observeForever(actionEventObserver)
        viewModel.detailErrorLiveEvent.observeForever(errorEventObserver)
    }

    @Before
    fun mockData() {
        coEvery {
            wikiSource.loadArticleDetails(any(), any())
        } returns ArticleDetailsCreator.createNormal()
        coEvery {
            favoritesSource.isFavorite(any())
        } returns true
        coEvery {
            favoritesSource.getFavoriteArticles()
        } returns favoritesFlow
    }

    @After
    fun removeObserver() {
        viewModel.detailIsLoadingLiveData.removeObserver(isLoadingObserver)
        viewModel.detailDataLiveData.removeObserver(viewDataObserver)
        viewModel.detailActionLiveEvent.removeObserver(actionEventObserver)
        viewModel.detailErrorLiveEvent.removeObserver(errorEventObserver)
    }

    @Test
    fun loadArticleDetails() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        viewModel.loadArticleDetails(pageId, languageCode)
        advanceUntilIdle()

        verifyOrder {
            isLoadingObserver.onChanged(true)
            viewDataObserver.onChanged(
                DetailViewData(ArticleDetailsCreator.createNormal(), true)
            )
            isLoadingObserver.onChanged(false)
        }
        verify(exactly = 0) {
            errorEventObserver.onChanged(any())
        }
        coVerifyOrder {
            wikiSource.loadArticleDetails(languageCode, pageId)
            favoritesSource.isFavorite(pageId)
        }

        Dispatchers.resetMain()
    }

    @Test
    fun loadArticleDetails_Exception() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        coEvery {
            wikiSource.loadArticleDetails(any(), any())
        } throws IOException()

        viewModel.loadArticleDetails(pageId, languageCode)
        advanceUntilIdle()

        verifyOrder {
            isLoadingObserver.onChanged(true)
            errorEventObserver.onChanged(DetailErrorItem)
        }
        verify(exactly = 0) {
            viewDataObserver.onChanged(
                DetailViewData(ArticleDetailsCreator.createNormal(), true)
            )
            isLoadingObserver.onChanged(false)
        }
        coVerify {
            wikiSource.loadArticleDetails(languageCode, pageId)
        }
        coVerify(exactly = 0) {
            favoritesSource.isFavorite(pageId)
        }

        Dispatchers.resetMain()
    }

    @Test
    fun onFavoriteButtonClicked_RemoveFromFavorites() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        viewModel.loadArticleDetails(pageId, languageCode)
        advanceUntilIdle()

        viewModel.onFavoriteButtonClicked()
        advanceUntilIdle()

        verifyOrder {
            viewDataObserver.onChanged(
                DetailViewData(ArticleDetailsCreator.createNormal(), true)
            )
            viewDataObserver.onChanged(
                DetailViewData(ArticleDetailsCreator.createNormal(), false)
            )
        }
        verify(exactly = 0) {
            errorEventObserver.onChanged(any())
        }
        coVerifyOrder {
            favoritesSource.removeFromFavorites(
                ArticleDetailsCreator.createNormal().toArticleItem()
            )
        }

        Dispatchers.resetMain()
    }

    @Test
    fun onFavoriteButtonClicked_AddToFavorites() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        coEvery {
            favoritesSource.isFavorite(any())
        } returns false

        viewModel.loadArticleDetails(pageId, languageCode)
        advanceUntilIdle()

        viewModel.onFavoriteButtonClicked()
        advanceUntilIdle()

        verifyOrder {
            viewDataObserver.onChanged(
                DetailViewData(ArticleDetailsCreator.createNormal(), false)
            )
            viewDataObserver.onChanged(
                DetailViewData(ArticleDetailsCreator.createNormal(), true)
            )
        }
        verify(exactly = 0) {
            errorEventObserver.onChanged(any())
        }
        coVerifyOrder {
            favoritesSource.addToFavorites(
                ArticleDetailsCreator.createNormal().toArticleItem()
            )
        }

        Dispatchers.resetMain()
    }

    @Test
    fun onViewInBrowserButtonClicked() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        viewModel.loadArticleDetails(pageId, languageCode)
        advanceUntilIdle()

        viewModel.onViewInBrowserButtonClicked()

        verify {
            actionEventObserver.onChanged(
                ViewInBrowserAction(ArticleDetailsCreator.createNormal().url)
            )
        }

        Dispatchers.resetMain()
    }

    @Test
    fun onOpenInMapButtonClicked() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        viewModel.loadArticleDetails(pageId, languageCode)
        advanceUntilIdle()

        viewModel.onOpenInMapButtonClicked()

        verify {
            actionEventObserver.onChanged(
                OpenInMapAction(ArticleDetailsCreator.createNormal().coordinates)
            )
        }

        Dispatchers.resetMain()
    }
}