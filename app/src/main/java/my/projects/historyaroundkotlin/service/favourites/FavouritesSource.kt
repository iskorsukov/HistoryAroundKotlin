package my.projects.historyaroundkotlin.service.favourites

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.model.detail.ArticleDetails

interface FavouritesSource {
    fun getFavouriteArticles(): Single<List<ArticleItem>>
    fun getFavouriteArticleDetails(pageid: Long): Maybe<ArticleDetails>

    fun addToFavourites(articleItem: ArticleItem, articleDetails: ArticleDetails): Completable
}