package my.projects.historyaroundkotlin.presentation.view.detail

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.hadilq.liveevent.LiveEvent
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.model.detail.ArticleDetails
import my.projects.historyaroundkotlin.presentation.view.base.BaseViewModelFragmentTest
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewaction.ViewAction
import my.projects.historyaroundkotlin.presentation.view.detail.viewstate.DetailLoadingItem
import my.projects.historyaroundkotlin.presentation.view.detail.viewstate.DetailViewState
import my.projects.historyaroundkotlin.presentation.view.detail.viewstate.viewdata.DetailViewData
import my.projects.historyaroundkotlin.presentation.viewmodel.detail.DetailViewModel
import my.projects.historyaroundkotlin.utils.DrawableMatcherUtils
import my.projects.historyaroundkotlin.utils.MockitoUtil
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@LargeTest
class DetailFragmentTest: BaseViewModelFragmentTest<DetailFragment, DetailViewModel>() {

    private val viewStateLiveData = MutableLiveData<DetailViewState>()
    private val viewActionLiveData = LiveEvent<ViewAction<*>>()

    private val args: Bundle = Bundle().also {
        it.putString("pageid", "1")
        it.putString("languageCode", "en")
    }

    override fun getViewModelClass(): Class<out DetailViewModel> {
        return DetailViewModel::class.java
    }

    override fun getViewModelBindings(): TestViewModelBindings {
        return DetailsMockViewModelBindings(mockViewModel)
    }

    @Before
    fun setupMockViewModel() {
        Mockito.`when`(mockViewModel.viewStateLiveData).thenReturn(viewStateLiveData)
        Mockito.`when`(mockViewModel.viewActionLiveData).thenReturn(viewActionLiveData)
        Mockito.doNothing().`when`(mockViewModel).loadArticleDetails(MockitoUtil.any(), MockitoUtil.any())
    }

    @Test
    fun observesViewState() {
        launchFragment(DetailFragment::class.java, args)

        Mockito.verify(mockViewModel).viewStateLiveData
        Mockito.verify(mockViewModel).viewActionLiveData
    }

    @Test
    fun callsViewModelToLoadDetails() {
        launchFragment(DetailFragment::class.java, args)

        Mockito.verify(mockViewModel).loadArticleDetails(args.getString("pageid")!!, args.getString("languageCode")!!)
    }

    @Test
    fun showsLoadingState() {
        pushLoadingState()
        launchFragment(DetailFragment::class.java, args)

        checkShowsLoadingState()
    }

    private fun pushLoadingState() {
        viewStateLiveData.postValue(DetailViewState(LCEState.LOADING, DetailLoadingItem.LOADING_DETAILS, null, null))
    }

    private fun checkShowsLoadingState() {
        onView(withId(R.id.loading)).check(matches(isDisplayed()))
    }

    @Test
    fun showsDetails() {
        val item = ArticleDetails(1L, "Sample", "Sample extract", null, 1.0 to 1.0, "https://url.com", "en")
        pushSampleData(item, false)
        launchFragment(DetailFragment::class.java, args)

        checkShowsDetails(item, false)
    }

    private fun pushSampleData(item: ArticleDetails, isFavorite: Boolean) {
        val articleDetailsViewData = DetailViewData(item, isFavorite)
        viewStateLiveData.postValue(DetailViewState(LCEState.CONTENT, null, articleDetailsViewData, null))
    }

    private fun checkShowsDetails(item: ArticleDetails, isFavorite: Boolean) {
        onView(withId(R.id.articleDetailsTitle)).check(matches(withText(item.title)))
        onView(withId(R.id.articleDetailsExtract)).check(matches(withText(item.extract)))

        val expectedDrawable = InstrumentationRegistry.getInstrumentation().targetContext.resources.getDrawable(
            if (isFavorite) {
                R.drawable.ic_star_black
            } else {
                R.drawable.ic_star_border_black
            }
        )
        onView(withId(R.id.favoriteButton)).check(matches(DrawableMatcherUtils.withDrawable(expectedDrawable)))
    }

    private fun clickOnFavoritesButton() {
        onView(withId(R.id.favoriteButton)).perform(click())
    }

    @Test
    fun clickOnFavoritesButtonCallsViewModel_favorite() {
        val item = ArticleDetails(1L, "Sample", "Sample extract", null, 1.0 to 1.0, "https://url.com", "en")
        pushSampleData(item, true)
        launchFragment(DetailFragment::class.java, args)

        clickOnFavoritesButton()

        Mockito.verify(mockViewModel).removeFromFavorites(item)
    }

    @Test
    fun clickOnFavoritesButtonCallsViewModel_notFavorite() {
        val item = ArticleDetails(1L, "Sample", "Sample extract", null, 1.0 to 1.0, "https://url.com", "en")
        pushSampleData(item, false)
        launchFragment(DetailFragment::class.java, args)

        clickOnFavoritesButton()

        Mockito.verify(mockViewModel).addToFavorites(item)
    }


}