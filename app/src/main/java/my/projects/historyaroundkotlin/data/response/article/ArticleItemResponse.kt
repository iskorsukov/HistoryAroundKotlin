package my.projects.historyaroundkotlin.data.response.article

import my.projects.historyaroundkotlin.data.response.common.CoordinatesResponse
import my.projects.historyaroundkotlin.data.response.common.ThumbnailResponse

data class ArticleItemResponse(val pageid: Long,
                               val title: String,
                               val description: String?,
                               val coordinates: List<CoordinatesResponse>?,
                               val thumbnail: ThumbnailResponse?)