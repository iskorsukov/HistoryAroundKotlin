package com.iskorsukov.historyaround.presentation.view.map.utils

import android.location.Location
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticleItemViewData
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticlesClusterItem
import javax.inject.Inject

class ArticlesClusterFactory @Inject constructor() {

    fun groupItemsIntoMarkers(zoomLevel: Double, articleItems: List<ArticleItemViewData>): List<ArticlesClusterItem> {
        val threshold = 15.0
        val clusters: MutableList<ArticlesCluster> = mutableListOf()
        for (item in articleItems) {
            if (clusters.isEmpty()) {
                val cluster = ArticlesCluster()
                cluster.add(item)
                clusters.add(cluster)
            } else {
                var foundCluster = false
                for (cluster in clusters) {
                    val markerDistance = markerDistance(
                        item.item.latlng,
                        cluster.center,
                        zoomLevel
                    )
                    if (markerDistance < threshold) {
                        cluster.add(item)
                        foundCluster = true
                        break
                    }
                }
                if (!foundCluster) {
                    val cluster = ArticlesCluster()
                    cluster.add(item)
                    clusters.add(cluster)
                }
            }
        }

        return clusters.map { ArticlesClusterItem(it.center, it.items) }
    }


    // Linear distance
    private fun locationDistance(first: Pair<Double, Double>, second: Pair<Double, Double>): Double {
        val firstLocation = Location("none")
        firstLocation.latitude = first.first
        firstLocation.longitude = first.second
        val secondLocation = Location("none")
        secondLocation.latitude = second.first
        secondLocation.longitude = second.second
        return firstLocation.distanceTo(secondLocation).toDouble()
    }

    private fun markerDistance(first: Pair<Double, Double>, second: Pair<Double, Double>, zoomLevel: Double): Double {
        return 0.1 * locationDistance(first, second) * Math.pow(2.0, zoomLevel - 14)
    }
}