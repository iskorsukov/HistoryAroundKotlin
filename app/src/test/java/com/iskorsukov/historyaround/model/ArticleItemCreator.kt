package com.iskorsukov.historyaround.model

import com.iskorsukov.historyaround.model.article.ArticleItem

object ArticleItemCreator {

    fun createNormalList(): List<ArticleItem> {
        return ModelDataProvider.articleItemsBase
    }

    fun createEmptyList(): List<ArticleItem> {
        return emptyList()
    }
}