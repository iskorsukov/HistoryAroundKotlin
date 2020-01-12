package com.iskorsukov.historyaround.data.response.detail

import com.iskorsukov.historyaround.data.response.common.CoordinatesResponse
import com.iskorsukov.historyaround.data.response.common.ThumbnailResponse

data class DetailItemResponse(val pageid: Long,
                              val title: String,
                              val extract: String,
                              val thumbnail: ThumbnailResponse?,
                              val coordinates: List<CoordinatesResponse>,
                              val fullurl: String)