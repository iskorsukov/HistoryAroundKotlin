package com.iskorsukov.historyaround.presentation.view.map.utils

import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticleItemViewData

class ArticlesCluster {
    val items: MutableList<ArticleItemViewData> = mutableListOf()
    lateinit var center: Pair<Double, Double>

    fun add(item: ArticleItemViewData) {
        center = if (items.isEmpty()) {
            item.item.latlng
        } else {
            calculateCenter()
        }
        items.add(item)
    }

    private fun calculateCenter(): Pair<Double, Double> {
        val avgLat = items.sumOf { it.item.latlng.first } / items.size
        val avgLon = items.sumOf { it.item.latlng.second } / items.size
        return avgLat to avgLon
    }
}