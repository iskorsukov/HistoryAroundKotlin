package my.projects.historyaroundkotlin.presentation.viewmodel.main.favourites

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import my.projects.historyaroundkotlin.mock.Mockable
import my.projects.historyaroundkotlin.presentation.viewstate.main.common.LoadingResultState
import my.projects.historyaroundkotlin.presentation.viewstate.main.favorites.FavoritesErrorStatus
import my.projects.historyaroundkotlin.presentation.viewstate.main.favorites.FavoritesViewState
import my.projects.historyaroundkotlin.service.favorites.FavoritesSource
import javax.inject.Inject

@Mockable
class FavouritesViewModel @Inject constructor(private val favoritesSource: FavoritesSource) : ViewModel() {

    private var disposable: Disposable? = null

    fun loadFavoriteItems(): LiveData<FavoritesViewState> {
        disposable?.dispose()
        Log.i("TEST", "loading favorite items")

        val liveData = MutableLiveData<FavoritesViewState>()
        disposable = favoritesSource.getFavoriteArticles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { favoriteItems ->
                    Log.i("TEST", "loaded " + favoriteItems.size + " favorite items")
                    liveData.postValue(FavoritesViewState(LoadingResultState.content(), favoriteItems))
                },
                { throwable ->
                    throwable.printStackTrace()
                    liveData.postValue(FavoritesViewState(LoadingResultState.error(listOf(FavoritesErrorStatus.LOADING_ERROR)), null))
                }
            )

        return liveData
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}