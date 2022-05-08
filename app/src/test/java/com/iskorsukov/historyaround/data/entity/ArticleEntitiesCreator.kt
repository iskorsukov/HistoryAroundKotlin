package com.iskorsukov.historyaround.data.entity

object ArticleEntitiesCreator {

    fun createNormal(): List<ArticleEntity> {
        return EntityDataProvider.articleEntitiesBase
    }

    fun createEmpty(): List<ArticleEntity> {
        return emptyList()
    }
}