package com.iskorsukov.historyaround.model

import com.iskorsukov.historyaround.CommonValues
import com.iskorsukov.historyaround.model.geo.GeoItem

object GeoItemCreator {

    fun createNormalList(): List<GeoItem> {
        return CommonValues.pageids.map { GeoItem(it) }
    }
}