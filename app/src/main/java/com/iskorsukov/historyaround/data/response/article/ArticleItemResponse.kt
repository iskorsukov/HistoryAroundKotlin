package com.iskorsukov.historyaround.data.response.article

import com.iskorsukov.historyaround.data.response.common.CoordinatesResponse
import com.iskorsukov.historyaround.data.response.common.ThumbnailResponse

data class ArticleItemResponse(val pageid: Long,
                               val title: String?,
                               val description: String?,
                               val coordinates: List<CoordinatesResponse>?,
                               val thumbnail: ThumbnailResponse?)