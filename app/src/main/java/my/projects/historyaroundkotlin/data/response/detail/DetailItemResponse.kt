package my.projects.historyaroundkotlin.data.response.detail

import my.projects.historyaroundkotlin.data.response.common.ThumbnailResponse

data class DetailItemResponse(val pageid: Long,
                              val title: String,
                              val extract: String,
                              val thumbnail: ThumbnailResponse?,
                              val fullurl: String)