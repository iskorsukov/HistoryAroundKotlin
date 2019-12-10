package my.projects.historyaroundkotlin.presentation.argument

import my.projects.historyaroundkotlin.model.article.ArticleItem
import java.io.Serializable

class FavoritesArgument(val favoriteItems: ArrayList<ArticleItem>): Serializable