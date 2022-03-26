package com.iskorsukov.historyaround.presentation.viewmodel.detail

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.iskorsukov.historyaround.mock.Mockable
import com.iskorsukov.historyaround.model.detail.ArticleDetails
import com.iskorsukov.historyaround.model.detail.toArticleItem
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.detail.viewaction.OpenInMapAction
import com.iskorsukov.historyaround.presentation.view.detail.viewaction.ViewInBrowserAction
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.DetailErrorItem
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.viewdata.DetailViewData
import com.iskorsukov.historyaround.service.api.WikiSource
import com.iskorsukov.historyaround.service.favorites.FavoritesSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@Mockable
class DetailViewModel @Inject constructor(
    private val wikiSource: WikiSource,
    private val favoritesSource: FavoritesSource
    ) : ViewModel() {

    private var detailsDisposable: Disposable? = null

    private val _detailDataLiveData = MutableLiveData<DetailViewData>()
    val detailDataLiveData: LiveData<DetailViewData>
        get() = _detailDataLiveData

    private val _detailIsLoadingLiveData = MutableLiveData(true)
    val detailIsLoadingLiveData: LiveData<Boolean>
        get() = _detailIsLoadingLiveData

    val detailErrorLiveEvent: LiveEvent<DetailErrorItem> = LiveEvent()

    val detailActionLiveEvent: LiveEvent<ViewAction<*>> = LiveEvent()

    fun loadArticleDetails(id: String, languageCode: String) {
        detailsDisposable?.dispose()

        _detailIsLoadingLiveData.value = true
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
                    _detailIsLoadingLiveData.value = false
                    _detailDataLiveData.value = viewData
                }, { throwable ->
                    throwable.printStackTrace()
                    _detailIsLoadingLiveData.value = false
                    detailErrorLiveEvent.value = DetailErrorItem()
                })
    }

    fun onOpenInMapButtonClicked() {
        _detailDataLiveData.value?.let {
            detailActionLiveEvent.value = OpenInMapAction(it.item.coordinates)
        }
    }

    fun onViewInBrowserButtonClicked() {
        _detailDataLiveData.value?.let {
            detailActionLiveEvent.value = ViewInBrowserAction(Uri.parse(it.item.url))
        }
    }

    fun onFavoriteButtonClicked() {
        _detailDataLiveData.value?.let {
            if (it.isFavorite)
                removeFromFavorites(it.item)
            else
                addToFavorites(it.item)
        }
    }

    private fun addToFavorites(details: ArticleDetails) {
        detailsDisposable?.dispose()

        detailsDisposable = favoritesSource.addToFavorites(details.toArticleItem())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _detailDataLiveData.value = DetailViewData(details, true)
            }, {
                it.printStackTrace()
            })
    }

    private fun removeFromFavorites(details: ArticleDetails) {
        detailsDisposable?.dispose()

        detailsDisposable = favoritesSource.removeFromFavorites(details.toArticleItem())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _detailDataLiveData.value = DetailViewData(details, false)
            }, {
                it.printStackTrace()
            })
    }

    override fun onCleared() {
        detailsDisposable?.dispose()
        super.onCleared()
    }
}