package my.projects.historyaroundkotlin.presentation.argument

import my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles.ArticleItemViewData
import java.io.Serializable

class MapDataArgument(val location: Pair<Double, Double>, val articles: ArrayList<ArticleItemViewData>): Serializable