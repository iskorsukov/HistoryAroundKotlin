package my.projects.historyaroundkotlin.presentation.viewmodel.main.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import my.projects.historyaroundkotlin.mock.Mockable
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultState
import my.projects.historyaroundkotlin.presentation.viewstate.main.detail.DetailErrorStatus
import my.projects.historyaroundkotlin.presentation.viewstate.main.detail.DetailViewState
import my.projects.historyaroundkotlin.service.api.WikiSource
import javax.inject.Inject

@Mockable
class DetailFlowViewModel @Inject constructor(private val wikiSource: WikiSource) : ViewModel() {

    private var detailsDisposable: Disposable? = null

    fun loadArticleDetails(id: String): LiveData<DetailViewState> {
        detailsDisposable?.dispose()

        val liveData = MutableLiveData<DetailViewState>()
        detailsDisposable = wikiSource.loadArticleDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ details ->
                liveData.value = DetailViewState(LoadingResultState.content(), details)
            }, { throwable ->
                throwable.printStackTrace()
                liveData.value = DetailViewState(LoadingResultState.error(listOf(DetailErrorStatus.CONNECTION_ERROR)), null)
            })

        return liveData
    }

    override fun onCleared() {
        detailsDisposable?.dispose()
        super.onCleared()
    }
}