package com.iskorsukov.historyaround.model.article

import com.iskorsukov.historyaround.model.common.ArticleThumbnail
import java.io.Serializable

data class ArticleItem(val pageid: String,
                       val title: String,
                       val description: String,
                       val latlng: Pair<Double, Double>,
                       val thumbnail: ArticleThumbnail?,
                       val languageCode: String
): Serializable