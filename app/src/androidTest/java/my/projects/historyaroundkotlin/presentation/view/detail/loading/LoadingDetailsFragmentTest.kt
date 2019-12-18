package my.projects.historyaroundkotlin.presentation.view.detail.loading

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.model.detail.ArticleDetails
import my.projects.historyaroundkotlin.presentation.view.base.BaseViewModelFragmentTest
import my.projects.historyaroundkotlin.presentation.view.detail.DetailsMockViewModelBindings
import my.projects.historyaroundkotlin.presentation.viewmodel.detail.DetailFlowViewModel
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultState
import my.projects.historyaroundkotlin.presentation.view.detail.viewstate.DetailErrorItem
import my.projects.historyaroundkotlin.presentation.view.detail.viewstate.DetailViewState
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoadingDetailsFragmentTest: BaseViewModelFragmentTest<LoadingDetailsFragment, DetailFlowViewModel>() {

    override fun getViewModelClass(): Class<out DetailFlowViewModel> = DetailFlowViewModel::class.java

    override fun getViewModelBindings(): TestViewModelBindings = DetailsMockViewModelBindings(mockViewModel)

    private fun prepareDetailsLiveData() {
        val detailsViewState =
            DetailViewState(
                LoadingResultState.content(),
                Mockito.mock(ArticleDetails::class.java)
            )

        val liveData = MutableLiveData<DetailViewState>()
        liveData.postValue(detailsViewState)

        Mockito.`when`(mockViewModel.loadArticleDetails(ArgumentMatchers.anyString())).thenReturn(liveData)
    }

    private fun prepareErrorLiveData(error: DetailErrorItem) {
        val liveData = MutableLiveData<DetailViewState>()
        liveData.postValue(
            DetailViewState(
                LoadingResultState.error(listOf(error)),
                null
            )
        )

        Mockito.`when`(mockViewModel.loadArticleDetails(ArgumentMatchers.anyString())).thenReturn(liveData)
    }

    private fun getPageidArguments(): Bundle {
        val args = Bundle()
        args.putString("pageid", "pageid")
        return args
    }

    @Test
    fun loadsDetails() {
        prepareDetailsLiveData()

        launchFragment(LoadingDetailsFragment::class.java, getPageidArguments())

        Mockito.verify(mockViewModel).loadArticleDetails(ArgumentMatchers.anyString())
    }

    @Test
    fun navigatesToDetailsFragmentWhenDetailsLoaded() {
        prepareDetailsLiveData()

        launchFragment(LoadingDetailsFragment::class.java, getPageidArguments())

        Mockito.verify(mockNavController).navigate(ArgumentMatchers.argThat { directions: NavDirections ->
            directions.actionId == R.id.action_loadingDetailsFragment_to_detail_fragment
        })
    }

    @Test
    fun navigatesToErrorScreenOnError() {
        prepareErrorLiveData(DetailErrorItem.CONNECTION_ERROR)

        launchFragment(LoadingDetailsFragment::class.java, getPageidArguments())

        Mockito.verify(mockNavController).navigate(ArgumentMatchers.argThat { directions: NavDirections ->
            directions.actionId == R.id.action_loadingDetailsFragment_to_errorDetailsFragment
        })

    }
}