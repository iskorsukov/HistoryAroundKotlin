package com.iskorsukov.historyaround.presentation.view.favorites

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.hadilq.liveevent.LiveEvent
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.injection.test.TestViewModelBindings
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.presentation.view.base.BaseViewModelFragmentTest
import com.iskorsukov.historyaround.presentation.view.common.viewstate.LCEState
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.favorites.viewaction.NavigateToDetailsAction
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.FavoritesLoadingItem
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.FavoritesViewState
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.viewdata.FavoritesViewData
import com.iskorsukov.historyaround.presentation.viewmodel.favourites.FavouritesViewModel
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.util.concurrent.TimeUnit

class FavoritesFragmentTest: BaseViewModelFragmentTest<FavoritesFragment, FavouritesViewModel>() {

    private val viewStateLiveData = MutableLiveData<FavoritesViewState>()
    private val viewActionLiveData = LiveEvent<ViewAction<*>>()

    override fun getViewModelClass(): Class<out FavouritesViewModel> {
        return FavouritesViewModel::class.java
    }

    override fun getViewModelBindings(): TestViewModelBindings {
        return FavoritesMockViewModelBindings(mockViewModel)
    }

    private fun getSampleData(): List<ArticleItem> {
        val firstItem = ArticleItem(
            "1",
            "First",
            "First description",
            1.0 to 1.0,
            null,
            "en"
        )
        val secondItem = ArticleItem(
            "2",
            "Second",
            "Second description",
            1.0 to 1.0,
            null,
            "en"
        )
        return listOf(firstItem, secondItem)
    }

    @Before
    fun setupViewModel() {
        Mockito.`when`(mockViewModel.favouritesDataLiveData).thenReturn(viewStateLiveData)
        Mockito.`when`(mockViewModel.favouritesActionLiveEvent).thenReturn(viewActionLiveData)
    }

    @Test
    fun observesViewState() {
        launchFragment(FavoritesFragment::class.java)

        Mockito.verify(mockViewModel).favouritesActionLiveEvent
        Mockito.verify(mockViewModel).favouritesDataLiveData
    }

    @Test
    fun showsLoadingState() {
        pushLoadingState()
        launchFragment(FavoritesFragment::class.java)

        checkShowsLoadingState()
    }

    private fun pushLoadingState() {
        viewStateLiveData.postValue(FavoritesViewState(LCEState.LOADING, FavoritesLoadingItem.LOADING_FAVORITES, null, null))
    }

    private fun checkShowsLoadingState() {
        onView(withId(R.id.loading)).check(matches(isDisplayed()))
    }

    @Test
    fun showsFavoriteArticles() {
        pushSampleData()
        launchFragment(FavoritesFragment::class.java)

        checkShowsItems()
    }

    private fun pushSampleData() {
        viewStateLiveData.postValue(FavoritesViewState(LCEState.CONTENT, null, FavoritesViewData(getSampleData()), null))
    }

    private fun checkShowsItems() {
        for (article in getSampleData()) {
            onView(allOf(withId(R.id.favoriteItemTitle), withText(article.title))).check(matches(isDisplayed()))
            onView(allOf(withId(R.id.favoriteItemDescription), withText(article.description))).check(matches(isDisplayed()))
        }
    }

    @Test
    fun callsViewModelOnItemSelected() {
        pushSampleData()
        launchFragment(FavoritesFragment::class.java)

        clickOnItem(getSampleData()[0])

        Mockito.verify(mockViewModel).onItemSelected(getSampleData()[0])
    }

    private fun clickOnItem(item: ArticleItem) {
        onView(withText(item.title)).perform(click())
    }

    @Test
    fun navigatesToDetailsOnAction() {
        launchFragment(FavoritesFragment::class.java)
        pushNavigateToDetailsAction()
        TimeUnit.SECONDS.sleep(2)

        Mockito.verify(mockNavController).navigate(ArgumentMatchers.argThat { directions: NavDirections ->
            directions.actionId == R.id.action_favoritesFragment_to_detailFragment
        })
    }

    private fun pushNavigateToDetailsAction() {
        viewActionLiveData.postValue(NavigateToDetailsAction(getSampleData()[0]))
    }
}