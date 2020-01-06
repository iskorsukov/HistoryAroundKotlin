package my.projects.historyaroundkotlin.presentation.viewmodel.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import my.projects.historyaroundkotlin.mock.Mockable
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewaction.ViewAction
import my.projects.historyaroundkotlin.presentation.view.favorites.adapter.FavoritesListener
import my.projects.historyaroundkotlin.presentation.view.favorites.viewaction.NavigateToDetailsAction
import my.projects.historyaroundkotlin.presentation.view.favorites.viewstate.FavoritesErrorItem
import my.projects.historyaroundkotlin.presentation.view.favorites.viewstate.FavoritesLoadingItem
import my.projects.historyaroundkotlin.presentation.view.favorites.viewstate.FavoritesViewState
import my.projects.historyaroundkotlin.presentation.view.favorites.viewstate.viewdata.FavoritesViewData
import my.projects.historyaroundkotlin.service.favorites.FavoritesSource
import javax.inject.Inject

@Mockable
class FavouritesViewModel @Inject constructor(private val favoritesSource: FavoritesSource) : ViewModel(),
    FavoritesListener {

    private var disposable: Disposable? = null

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

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}