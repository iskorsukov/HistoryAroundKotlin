package com.iskorsukov.historyaround.presentation.viewmodel.detail

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@Mockable
class DetailViewModel @Inject constructor(
    private val wikiSource: WikiSource,
    private val favoritesSource: FavoritesSource
    ) : ViewModel() {

    private val _detailDataLiveData = MutableLiveData<DetailViewData>()
    val detailDataLiveData: LiveData<DetailViewData>
        get() = _detailDataLiveData

    private val _detailIsLoadingLiveData = MutableLiveData(true)
    val detailIsLoadingLiveData: LiveData<Boolean>
        get() = _detailIsLoadingLiveData

    val detailErrorLiveEvent: LiveEvent<DetailErrorItem> = LiveEvent()

    val detailActionLiveEvent: LiveEvent<ViewAction<*>> = LiveEvent()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _detailIsLoadingLiveData.value = false
        detailErrorLiveEvent.value = DetailErrorItem()
    }

    fun loadArticleDetails(id: String, languageCode: String) {
        _detailIsLoadingLiveData.value = true
        viewModelScope.launch(exceptionHandler) {
            val articleDetails = wikiSource.loadArticleDetails(languageCode, id)
            val isFavorite = favoritesSource.isFavorite(id)
            _detailIsLoadingLiveData.value = false
            _detailDataLiveData.value = DetailViewData(
                articleDetails,
                isFavorite
            )
        }
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
        viewModelScope.launch(exceptionHandler) {
            favoritesSource.addToFavorites(details.toArticleItem())
            _detailDataLiveData.value = DetailViewData(details, true)
        }
    }

    private fun removeFromFavorites(details: ArticleDetails) {
        viewModelScope.launch(exceptionHandler) {
            favoritesSource.removeFromFavorites(details.toArticleItem())
            _detailDataLiveData.value = DetailViewData(details, false)
        }
    }
}