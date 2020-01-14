package com.iskorsukov.historyaround.service.favorites

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import com.iskorsukov.historyaround.model.article.ArticleItem

interface FavoritesSource {
    fun getFavoriteArticles(): Observable<List<ArticleItem>>
    fun addToFavorites(articleItem: ArticleItem): Completable
    fun removeFromFavorites(articleItem: ArticleItem): Completable
    fun isFavorite(pageid: String): Single<Boolean>
}