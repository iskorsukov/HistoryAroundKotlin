package my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata

import my.projects.historyaroundkotlin.presentation.view.map.utils.toGeoPoint

class ArticlesClusterItem(val location: Pair<Double, Double>, val items: List<ArticleItemViewData>)

fun ArticlesClusterItem.toOverlayItem(): ArticlesOverlayItem {
    return ArticlesOverlayItem("", "", location.toGeoPoint(), items)
}