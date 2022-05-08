package com.iskorsukov.historyaround.data.response

import com.iskorsukov.historyaround.data.response.article.ArticlePagesResponse
import com.iskorsukov.historyaround.data.response.article.ArticleQueryResponse

object ArticleQueryResponseCreator {

    fun createNormalUnfiltered(): ArticleQueryResponse {
        return ArticleQueryResponse(
            ArticlePagesResponse(
                ResponseDataProvider.articlePagesMap
            )
        )
    }

    fun createNormalValid(): ArticleQueryResponse {
        return ArticleQueryResponse(
            ArticlePagesResponse(
                ResponseDataProvider.articlePagesMap.filterValues { response ->
                    response.coordinates != null && response.coordinates!!.isNotEmpty()
                }
            )
        )
    }

    fun createEmpty(): ArticleQueryResponse {
        return ArticleQueryResponse(
            ArticlePagesResponse(
                emptyMap()
            )
        )
    }
}