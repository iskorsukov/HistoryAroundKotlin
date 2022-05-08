package com.iskorsukov.historyaround.mapper.entity

import com.google.common.truth.Truth.assertThat
import com.iskorsukov.historyaround.data.entity.ArticleEntitiesCreator
import com.iskorsukov.historyaround.model.ArticleItemCreator
import org.junit.Test

class ArticleEntityMapperTest {

    private val mapper = ArticleEntitiesMapper()

    @Test
    fun mapArticleEntity() {
        val article = mapper.mapArticleEntity(ArticleEntitiesCreator.createNormal().first())

        assertThat(article).isEqualTo(ArticleItemCreator.createNormalList().first())
    }

    @Test
    fun mapArticleEntityList() {
        val articles = mapper.mapArticleEntityList(ArticleEntitiesCreator.createNormal())

        assertThat(articles).containsExactlyElementsIn(ArticleItemCreator.createNormalList())
    }

    @Test
    fun mapArticleToEntity() {
        val entity = mapper.mapArticleToEntity(ArticleItemCreator.createNormalList().first())

        assertThat(entity).isEqualTo(ArticleEntitiesCreator.createNormal().first())
    }
}