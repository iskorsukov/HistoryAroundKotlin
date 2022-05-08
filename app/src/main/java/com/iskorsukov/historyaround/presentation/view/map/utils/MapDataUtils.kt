package com.iskorsukov.historyaround.presentation.view.map.utils

import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticleItemViewData
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticlesClusterItem
import org.osmdroid.util.GeoPoint

fun Pair<Double, Double>.toGeoPoint(): GeoPoint {
    return GeoPoint(first, second)
}

fun List<ArticlesClusterItem>.toViewData(): List<ArticleItemViewData> {
    return this.foldRight(HashSet()) { overlayItem: ArticlesClusterItem, set: HashSet<ArticleItemViewData> ->
        set.addAll(overlayItem.items)
        set
    }.toList()
}