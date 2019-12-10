package my.projects.historyaroundkotlin.service.favorites

import io.reactivex.Completable
import io.reactivex.Single
import my.projects.historyaroundkotlin.model.article.ArticleItem

interface FavoritesSource {
    fun getFavoriteArticles(): Single<List<ArticleItem>>
    fun addToFavorites(articleItem: ArticleItem): Completable
    fun removeFromFavorites(articleItem: ArticleItem): Completable
    fun isFavorite(pageid: String): Single<Boolean>
}