package my.projects.historyaroundkotlin.presentation.view.map

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.hadilq.liveevent.LiveEvent
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.view.base.BaseViewModelFragmentTest
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewaction.ViewAction
import my.projects.historyaroundkotlin.presentation.view.map.viewaction.ShowArticleSelectorAction
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.MapLoadingItem
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.MapViewState
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata.ArticleItemViewData
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata.ArticlesClusterItem
import my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata.MapViewData
import my.projects.historyaroundkotlin.presentation.viewmodel.map.MapViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito


@RunWith(AndroidJUnit4::class)
@LargeTest
class MapFragmentTest: BaseViewModelFragmentTest<MapFragment, MapViewModel>() {
    override fun getViewModelClass(): Class<out MapViewModel> {
        return MapViewModel::class.java
    }

    override fun getViewModelBindings(): TestViewModelBindings = MapMockViewModelBindings(mockViewModel)

    private val viewStateLiveData = MutableLiveData<MapViewState>()
    private val viewActionLiveData = LiveEvent<ViewAction<*>>()

    @Before
    fun setupMockViewModel() {
        Mockito.`when`(mockViewModel.mapDataLiveData).thenReturn(viewStateLiveData)
        Mockito.`when`(mockViewModel.mapActionLiveData).thenReturn(viewActionLiveData)
    }

    private fun pushLoadingState() {
        viewStateLiveData.postValue(MapViewState(LCEState.LOADING, MapLoadingItem.LOADING_ARTICLES, null, null))
    }

    private fun getSampleLocation(): Pair<Double, Double> {
        return 55.790589 to 37.681430
    }

    private fun getSampleArticles(): List<ArticleItemViewData> {
        val firstArticle = ArticleItem(
            "1",
            "First",
            "First description",
            55.790897 to 37.681685,
            null
        )
        val firstViewData = ArticleItemViewData(firstArticle, false)

        val secondArticle = ArticleItem(
            "2",
            "Second",
            "Second description",
            55.788912 to 37.681103,
            null
        )
        val secondViewData = ArticleItemViewData(secondArticle, false)

        return listOf(firstViewData, secondViewData)
    }

    private fun getSampleClusters(): List<ArticlesClusterItem> {
        return getSampleArticles().map { ArticlesClusterItem(it.item.latlng, listOf(it)) }
    }

    private fun pushSampleData() {
        viewStateLiveData.postValue(MapViewState(LCEState.CONTENT, null, MapViewData(getSampleLocation(), getSampleClusters()), null))
    }

    @Test
    fun showsLoadingState() {
        pushLoadingState()
        launchFragment(MapFragment::class.java)

        checkShowsLoadingState()
    }

    private fun checkShowsLoadingState() {
        onView(withId(R.id.loading)).check(matches(isDisplayed()))
    }

    private fun pushArticlesSelectorAction() {
        viewActionLiveData.postValue(ShowArticleSelectorAction(getSampleArticles()))
    }

    @Test
    fun showsArticlesSelector() {
        pushSampleData()
        launchFragment(MapFragment::class.java)
        pushArticlesSelectorAction()

        checkShowsArticleSelector()
    }

    private fun checkShowsArticleSelector() {
        onView(withId(R.id.articlesRecycler)).check(matches(isDisplayed()))
        for (article in getSampleArticles()) {
            onView(withText(article.item.title)).check(matches(isDisplayed()))
        }
    }

    private fun clickOnArticle(item: ArticleItemViewData) {
        onView(withText(item.item.title)).perform(click())
    }

    @Test
    fun callsViewModelOnArticleSelected() {
        pushSampleData()
        launchFragment(MapFragment::class.java)
        pushArticlesSelectorAction()

        clickOnArticle(getSampleArticles()[0])

        Mockito.verify(mockViewModel).onItemSelected(getSampleArticles()[0].item)
    }

    private fun clickOnFAB() {
        onView(withId(R.id.myLocationFAB)).perform(click())
    }

    @Test
    fun callsViewModelOnFABClicked() {
        pushSampleData()
        launchFragment(MapFragment::class.java)

        clickOnFAB()

        Mockito.verify(mockViewModel, Mockito.atLeast(2)).onCenterOnUserLocationClicked()
    }
}