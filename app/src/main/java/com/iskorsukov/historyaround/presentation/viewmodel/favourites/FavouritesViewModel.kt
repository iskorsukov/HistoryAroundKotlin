package com.iskorsukov.historyaround.presentation.viewmodel.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import com.iskorsukov.historyaround.mock.Mockable
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.presentation.view.common.viewstate.LCEState
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.favorites.adapter.FavoritesListener
import com.iskorsukov.historyaround.presentation.view.favorites.viewaction.NavigateToDetailsAction
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.FavoritesErrorItem
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.FavoritesLoadingItem
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.FavoritesViewState
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.viewdata.FavoritesViewData
import com.iskorsukov.historyaround.service.favorites.FavoritesSource
import javax.inject.Inject

@Mockable
class FavouritesViewModel @Inject constructor(private val favoritesSource: FavoritesSource) : ViewModel(),
    FavoritesListener {

    private var disposable: Disposable? = null
    private var removeDisposable: Disposable? = null

    val viewStateLiveData: LiveData<FavoritesViewState> by lazy {
        MutableLiveData<FavoritesViewState>().also {
            it.value = FavoritesViewState(LCEState.LOADING, FavoritesLoadingItem.LOADING_FAVORITES, null, null)
            loadFavoriteItems()
        }
    }

    val viewActionLiveData: LiveEvent<ViewAction<*>> = LiveEvent()

    fun loadFavoriteItems() {
        disposable?.dispose()
        disposable = favoritesSource.getFavoriteArticles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { favoriteItems ->
                    (viewStateLiveData as MutableLiveData).value =
                        FavoritesViewState(
                            LCEState.CONTENT,
                            null,
                            FavoritesViewData(
                                favoriteItems
                            ),
                            null
                        )
                },
                { throwable ->
                    throwable.printStackTrace()
                    (viewStateLiveData as MutableLiveData).value =
                        FavoritesViewState(
                            LCEState.ERROR,
                            null,
                            null,
                            FavoritesErrorItem.ERROR
                        )
                }
            )
    }

    override fun onItemSelected(item: ArticleItem) {
        viewActionLiveData.value =
            NavigateToDetailsAction(item)
    }

    fun onRetry() {
        (viewStateLiveData as MutableLiveData).value = FavoritesViewState(LCEState.LOADING, FavoritesLoadingItem.LOADING_FAVORITES, null, null)
        loadFavoriteItems()
    }

    override fun onCleared() {
        removeDisposable?.dispose()
        disposable?.dispose()
        super.onCleared()
    }
}