package com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata

import com.iskorsukov.historyaround.presentation.view.map.utils.toGeoPoint

data class ArticlesClusterItem(val location: Pair<Double, Double>, val items: List<ArticleItemViewData>)

fun ArticlesClusterItem.toOverlayItem(): ArticlesOverlayItem {
    return ArticlesOverlayItem(items.joinToString { it.item.title }, "", location.toGeoPoint(), items)
}