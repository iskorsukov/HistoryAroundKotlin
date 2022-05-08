package com.iskorsukov.historyaround.data.response

import com.iskorsukov.historyaround.data.response.detail.DetailDataResponse
import com.iskorsukov.historyaround.data.response.detail.DetailQueryResponse

object DetailQueryResponseCreator {

    fun createNormal(): DetailQueryResponse {
        return DetailQueryResponse(
            DetailDataResponse(
                ResponseDataProvider.articleDetailPagesMap
            )
        )
    }

    fun createEmpty(): DetailQueryResponse {
        return DetailQueryResponse(
            DetailDataResponse(
                emptyMap()
            )
        )
    }
}