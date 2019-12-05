package my.projects.historyaroundkotlin.presentation.argument

import android.location.Location
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles.ArticleItemViewData
import java.io.Serializable

class MapDataArgument(val location: Location, val articles: ArrayList<ArticleItemViewData>): Serializable {
}