package com.iskorsukov.historyaround.data.response

import com.iskorsukov.historyaround.CommonValues
import com.iskorsukov.historyaround.data.response.geo.GeoDataResponse
import com.iskorsukov.historyaround.data.response.geo.GeoItemResponse
import com.iskorsukov.historyaround.data.response.geo.GeoQueryResponse

object GeoQueryResponseCreator {

    fun createNormal(): GeoQueryResponse {
        return GeoQueryResponse(
            GeoDataResponse(
                CommonValues.pageids.map { id -> GeoItemResponse(id) }
            )
        )
    }

    fun createEmpty(): GeoQueryResponse {
        return GeoQueryResponse(
            GeoDataResponse(
                emptyList()
            )
        )
    }
}