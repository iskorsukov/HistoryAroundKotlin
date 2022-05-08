package com.iskorsukov.historyaround.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.iskorsukov.historyaround.model.ArticleItemCreator
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.favorites.viewaction.NavigateToDetailsAction
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.FavoritesErrorItem
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.viewdata.FavoritesViewData
import com.iskorsukov.historyaround.presentation.viewmodel.favourites.FavouritesViewModel
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
class FavoritesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val favoritesSource: FavoritesSource = mockk()

    private val viewModel = FavouritesViewModel(favoritesSource)

    private val isLoadingObserver: Observer<Boolean> = mockk(relaxed = true)
    private val viewDataObserver: Observer<FavoritesViewData> = mockk(relaxed = true)
    private val actionEventObserver: Observer<ViewAction<*>> = mockk(relaxed = true)
    private val errorEventObserver: Observer<FavoritesErrorItem> = mockk(relaxed = true)

    private val favoritesFlow: MutableStateFlow<List<ArticleItem>> =
        MutableStateFlow(ArticleItemCreator.createNormalList())

    @Before
    fun observeData() {
        viewModel.favouritesIsLoadingLiveData.observeForever(isLoadingObserver)
        viewModel.favouritesDataLiveData.observeForever(viewDataObserver)
        viewModel.favouritesActionLiveEvent.observeForever(actionEventObserver)
        viewModel.favouritesErrorLiveEvent.observeForever(errorEventObserver)
    }

    @Before
    fun mockData() {
        coEvery {
            favoritesSource.getFavoriteArticles()
        } returns favoritesFlow
    }

    @After
    fun removeObserver() {
        viewModel.favouritesIsLoadingLiveData.removeObserver(isLoadingObserver)
        viewModel.favouritesDataLiveData.removeObserver(viewDataObserver)
        viewModel.favouritesActionLiveEvent.removeObserver(actionEventObserver)
        viewModel.favouritesErrorLiveEvent.removeObserver(errorEventObserver)
    }

    @Test
    fun loadFavoriteItems() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        viewModel.loadFavoriteItems()
        advanceUntilIdle()

        verifyOrder {
            isLoadingObserver.onChanged(true)
            viewDataObserver.onChanged(FavoritesViewData(ArticleItemCreator.createNormalList()))
            isLoadingObserver.onChanged(false)
        }
        verify(exactly = 0) {
            errorEventObserver.onChanged(any())
        }
        coVerify {
            favoritesSource.getFavoriteArticles()
        }

        Dispatchers.resetMain()
    }

    @Test
    fun loadFavoriteItems_Exception() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        coEvery {
            favoritesSource.getFavoriteArticles()
        } throws IOException()

        viewModel.loadFavoriteItems()
        advanceUntilIdle()

        verifyOrder {
            isLoadingObserver.onChanged(true)
            errorEventObserver.onChanged(FavoritesErrorItem)
        }
        verify(exactly = 0) {
            viewDataObserver.onChanged(FavoritesViewData(ArticleItemCreator.createNormalList()))
            isLoadingObserver.onChanged(false)
        }
        coVerify {
            favoritesSource.getFavoriteArticles()
        }

        Dispatchers.resetMain()
    }

    @Test
    fun loadFavoriteItems_FlowChanged() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        viewModel.loadFavoriteItems()
        advanceUntilIdle()

        favoritesFlow.value = emptyList()
        advanceUntilIdle()

        verifyOrder {
            isLoadingObserver.onChanged(true)
            viewDataObserver.onChanged(FavoritesViewData(ArticleItemCreator.createNormalList()))
            isLoadingObserver.onChanged(false)
            viewDataObserver.onChanged(FavoritesViewData(emptyList()))
        }
        verify(exactly = 0) {
            errorEventObserver.onChanged(any())
        }
        coVerify {
            favoritesSource.getFavoriteArticles()
        }

        Dispatchers.resetMain()
    }

    @Test
    fun onItemSelected() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        viewModel.loadFavoriteItems()
        advanceUntilIdle()

        viewModel.onItemSelected(ArticleItemCreator.createNormalList().first())

        verify {
            actionEventObserver.onChanged(
                NavigateToDetailsAction(ArticleItemCreator.createNormalList().first())
            )
        }

        Dispatchers.resetMain()
    }
}