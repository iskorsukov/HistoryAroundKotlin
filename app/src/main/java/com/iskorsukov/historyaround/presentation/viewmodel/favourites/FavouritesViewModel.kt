package com.iskorsukov.historyaround.presentation.viewmodel.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.iskorsukov.historyaround.mock.Mockable
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.favorites.adapter.FavoritesListener
import com.iskorsukov.historyaround.presentation.view.favorites.viewaction.NavigateToDetailsAction
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.FavoritesErrorItem
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.viewdata.FavoritesViewData
import com.iskorsukov.historyaround.service.favorites.FavoritesSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@Mockable
class FavouritesViewModel @Inject constructor(private val favoritesSource: FavoritesSource) : ViewModel(),
    FavoritesListener {

    private var disposable: Disposable? = null
    private var removeDisposable: Disposable? = null

    private val _favouritesDataLiveData = MutableLiveData<FavoritesViewData>()
    val favouritesDataLiveData: LiveData<FavoritesViewData>
        get() = _favouritesDataLiveData

    private val _favouritesIsLoadingLiveData = MutableLiveData(true)
    val favouritesIsLoadingLiveData: LiveData<Boolean>
        get() = _favouritesIsLoadingLiveData

    val favouritesErrorLiveEvent: LiveEvent<FavoritesErrorItem> = LiveEvent()

    val favouritesActionLiveEvent: LiveEvent<ViewAction<*>> = LiveEvent()

    fun loadFavoriteItems() {
        _favouritesIsLoadingLiveData.value = true

        disposable?.dispose()
        disposable = favoritesSource.getFavoriteArticles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { favoriteItems ->
                    _favouritesDataLiveData.value = FavoritesViewData(favoriteItems)
                    _favouritesIsLoadingLiveData.value = false
                },
                { throwable ->
                    throwable.printStackTrace()
                    favouritesErrorLiveEvent.value = FavoritesErrorItem()
                    _favouritesIsLoadingLiveData.value = false
                }
            )
    }

    override fun onItemSelected(item: ArticleItem) {
        favouritesActionLiveEvent.value = NavigateToDetailsAction(item)
    }

    override fun onCleared() {
        removeDisposable?.dispose()
        disposable?.dispose()
        super.onCleared()
    }
}