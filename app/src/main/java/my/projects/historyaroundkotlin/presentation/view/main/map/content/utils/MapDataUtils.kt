package my.projects.historyaroundkotlin.presentation.view.main.map.content.utils

import android.location.Location
import my.projects.historyaroundkotlin.presentation.view.main.map.content.ArticlesOverlayItem
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles.ArticleItemViewData
import org.osmdroid.util.GeoPoint

class ArticlesCluster {
    val items: MutableList<ArticleItemViewData> = mutableListOf()
    lateinit var center: Pair<Double, Double>

    fun add(item: ArticleItemViewData) {
        center = if (items.isEmpty()) {
            item.item.latlng
        } else {
            (center.first + item.item.latlng.first) / 2 to (center.second + item.item.latlng.second) / 2
        }
        items.add(item)
    }
}

fun groupItemsIntoMarkers(zoomLevel: Double, articleItems: List<ArticleItemViewData>): List<ArticlesOverlayItem> {
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
                val markerDistance = markerDistance(item.item.latlng, cluster.center, zoomLevel)
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

    return clusters.map { articlesCluster: ArticlesCluster ->
        ArticlesOverlayItem("", "", GeoPoint(articlesCluster.center.first, articlesCluster.center.second), articlesCluster.items)
    }
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

fun Location.toGeoPoint(): GeoPoint {
    return GeoPoint(latitude, longitude)
}