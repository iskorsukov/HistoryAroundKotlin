package my.projects.historyaroundkotlin.presentation.view.map.loading

import android.location.Location
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import androidx.test.ext.junit.runners.AndroidJUnit4
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.presentation.argument.LocationArgument
import my.projects.historyaroundkotlin.presentation.view.base.BaseViewModelFragmentTest
import my.projects.historyaroundkotlin.presentation.view.map.MapMockViewModelBindings
import my.projects.historyaroundkotlin.presentation.viewmodel.map.MapFlowViewModel
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultState
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles.ArticleItemViewData
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles.ArticlesErrorItem
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles.ArticlesViewState
import my.projects.historyaroundkotlin.utils.MockitoUtil
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class LoadingArticlesFragmentTest: BaseViewModelFragmentTest<LoadingArticlesFragment, MapFlowViewModel>() {

    override fun getViewModelClass(): Class<out MapFlowViewModel> = MapFlowViewModel::class.java

    override fun getViewModelBindings(): TestViewModelBindings = MapMockViewModelBindings(mockViewModel)

    private fun getLocationArgs(): Bundle {
        val args = Bundle()
        args.putSerializable("locationArgument", LocationArgument(Location("GPS")))
        return args
    }

    private fun prepareArticlesLiveData() {
        val items = mutableListOf<ArticleItemViewData>()
        items.add(Mockito.mock(ArticleItemViewData::class.java))
        items.add(Mockito.mock(ArticleItemViewData::class.java))

        val liveData = MutableLiveData<ArticlesViewState>()
        liveData.postValue(ArticlesViewState(LoadingResultState.content(), items))

        Mockito.`when`(mockViewModel.loadArticles(MockitoUtil.any())).thenReturn(liveData)
    }

    private fun prepareErrorLiveData(error: ArticlesErrorItem) {
        val liveData = MutableLiveData<ArticlesViewState>()
        liveData.postValue(ArticlesViewState(LoadingResultState.error(listOf(error)), null))

        Mockito.`when`(mockViewModel.loadArticles(MockitoUtil.any())).thenReturn(liveData)
    }

    @Test
    fun loadsArticles() {
        prepareArticlesLiveData()

        launchFragment(LoadingArticlesFragment::class.java, getLocationArgs())

        Mockito.verify(mockViewModel).loadArticles(MockitoUtil.any())
    }

    @Test
    fun navigatesToMapFragmentAfterArticlesLoaded() {
        prepareArticlesLiveData()

        launchFragment(LoadingArticlesFragment::class.java, getLocationArgs())

        Mockito.verify(mockNavController).navigate(ArgumentMatchers.argThat { directions: NavDirections ->
            directions.actionId == R.id.action_loadingArticlesFragment_to_mapFragment
        })
    }

    @Test
    fun navigatesToErrorScreenOnLoadingArticlesError() {
        prepareErrorLiveData(ArticlesErrorItem.CONNECTION_ERROR)

        launchFragment(LoadingArticlesFragment::class.java, getLocationArgs())

        Mockito.verify(mockNavController).navigate(ArgumentMatchers.argThat { directions: NavDirections ->
            directions.actionId == R.id.action_loadingArticlesFragment_to_errorArticlesFragment
        })
    }
}