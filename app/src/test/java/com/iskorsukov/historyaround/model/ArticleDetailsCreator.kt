package com.iskorsukov.historyaround.model

import com.iskorsukov.historyaround.model.detail.ArticleDetails

object ArticleDetailsCreator {

    fun createNormal(): ArticleDetails {
        return ModelDataProvider.detailItemsBase.first()
    }
}