package my.projects.historyaroundkotlin.presentation.viewmodel.main.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import my.projects.historyaroundkotlin.mock.Mockable
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.model.detail.ArticleDetails
import my.projects.historyaroundkotlin.model.detail.toArticleItem
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultState
import my.projects.historyaroundkotlin.presentation.viewstate.main.detail.ArticleDetailsViewData
import my.projects.historyaroundkotlin.presentation.viewstate.main.detail.DetailErrorStatus
import my.projects.historyaroundkotlin.presentation.viewstate.main.detail.DetailViewState
import my.projects.historyaroundkotlin.service.api.WikiSource
import my.projects.historyaroundkotlin.service.favorites.FavoritesSource
import javax.inject.Inject

@Mockable
class DetailFlowViewModel @Inject constructor(private val wikiSource: WikiSource, private val favoritesSource: FavoritesSource) : ViewModel() {

    private var detailsDisposable: Disposable? = null

    val detailsLiveData: LiveData<DetailViewState> = MutableLiveData<DetailViewState>()

    fun loadArticleDetails(id: String) {
        detailsDisposable?.dispose()

        detailsDisposable = wikiSource.loadArticleDetails(id)
            .zipWith(favoritesSource.isFavorite(id)) { details, isFavorite ->
                ArticleDetailsViewData(details, isFavorite)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ viewData ->
                (detailsLiveData as MutableLiveData).value = DetailViewState(LoadingResultState.content(), viewData)
            }, { throwable ->
                throwable.printStackTrace()
                (detailsLiveData as MutableLiveData).value = DetailViewState(LoadingResultState.error(listOf(DetailErrorStatus.CONNECTION_ERROR)), null)
            })
    }

    fun addToFavorites(details: ArticleDetails) {
        detailsDisposable?.dispose()

        detailsDisposable = favoritesSource.addToFavorites(details.toArticleItem())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                (detailsLiveData as MutableLiveData).value = DetailViewState(LoadingResultState.content(), ArticleDetailsViewData(details, true))
            }, {
                it.printStackTrace()
            })
    }

    fun removeFromFavorites(details: ArticleDetails) {
        detailsDisposable?.dispose()

        detailsDisposable = favoritesSource.removeFromFavorites(details.toArticleItem())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                (detailsLiveData as MutableLiveData).value = DetailViewState(LoadingResultState.content(), ArticleDetailsViewData(details, false))
            }, {
                it.printStackTrace()
            })
    }

    override fun onCleared() {
        detailsDisposable?.dispose()
        super.onCleared()
    }
}