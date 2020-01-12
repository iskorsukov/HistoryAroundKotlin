package com.iskorsukov.historyaround.presentation.viewmodel.detail

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import com.iskorsukov.historyaround.mock.Mockable
import com.iskorsukov.historyaround.model.detail.ArticleDetails
import com.iskorsukov.historyaround.model.detail.toArticleItem
import com.iskorsukov.historyaround.presentation.view.common.viewstate.LCEState
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.detail.viewaction.OpenInMapAction
import com.iskorsukov.historyaround.presentation.view.detail.viewaction.ViewInBrowserAction
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.DetailErrorItem
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.DetailLoadingItem
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.DetailViewState
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.viewdata.DetailViewData
import com.iskorsukov.historyaround.service.api.WikiSource
import com.iskorsukov.historyaround.service.favorites.FavoritesSource
import com.iskorsukov.historyaround.service.preferences.PreferencesSource
import javax.inject.Inject

@Mockable
class DetailViewModel @Inject constructor(private val wikiSource: WikiSource, private val favoritesSource: FavoritesSource, private val preferencesSource: PreferencesSource) : ViewModel() {

    private var detailsDisposable: Disposable? = null

    val viewStateLiveData: LiveData<DetailViewState> = MutableLiveData<DetailViewState>()
    val viewActionLiveData: LiveEvent<ViewAction<*>> = LiveEvent()

    fun loadArticleDetails(id: String, languageCode: String) {
        detailsDisposable?.dispose()

        (viewStateLiveData as MutableLiveData).value = DetailViewState(LCEState.LOADING, DetailLoadingItem.LOADING_DETAILS, null, null)
        detailsDisposable =
            wikiSource.loadArticleDetails(languageCode, id)
                .zipWith(favoritesSource.isFavorite(id)) { details, isFavorite ->
                    DetailViewData(
                        details,
                        isFavorite
                    )
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewData ->
                    (viewStateLiveData as MutableLiveData).value =
                        DetailViewState(
                            LCEState.CONTENT,
                            null,
                            viewData,
                            null
                        )
                }, { throwable ->
                    throwable.printStackTrace()
                    (viewStateLiveData as MutableLiveData).value =
                        DetailViewState(
                            LCEState.ERROR,
                            null,
                            null,
                            DetailErrorItem.ERROR
                        )
                })
    }

    fun addToFavorites(details: ArticleDetails) {
        detailsDisposable?.dispose()

        detailsDisposable = favoritesSource.addToFavorites(details.toArticleItem())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                (viewStateLiveData as MutableLiveData).value =
                    DetailViewState(
                        LCEState.CONTENT,
                        null,
                        DetailViewData(
                            details,
                            true
                        ),
                        null
                    )
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
                (viewStateLiveData as MutableLiveData).value =
                    DetailViewState(
                        LCEState.CONTENT,
                        null,
                        DetailViewData(
                            details,
                            false
                        ),
                        null
                    )
            }, {
                it.printStackTrace()
            })
    }

    fun onOpenInMapButtonClicked(latlon: Pair<Double, Double>) {
        viewActionLiveData.value = OpenInMapAction(latlon)
    }

    fun onViewInBrowserButtonClicked(url: String) {
        viewActionLiveData.value = ViewInBrowserAction(Uri.parse(url))
    }

    override fun onCleared() {
        detailsDisposable?.dispose()
        super.onCleared()
    }
}