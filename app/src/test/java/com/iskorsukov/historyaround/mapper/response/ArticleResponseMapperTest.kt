package com.iskorsukov.historyaround.mapper.response

import com.google.common.truth.Truth.assertThat
import com.iskorsukov.historyaround.data.response.ArticleQueryResponseCreator
import com.iskorsukov.historyaround.data.response.DetailQueryResponseCreator
import com.iskorsukov.historyaround.data.response.GeoQueryResponseCreator
import com.iskorsukov.historyaround.model.ArticleItemCreator
import com.iskorsukov.historyaround.model.GeoItemCreator
import com.iskorsukov.historyaround.model.ModelDataProvider
import org.junit.Test

class ArticleResponseMapperTest {

    private val mapper = ArticleResponsesMapper()

    @Test
    fun mapGeoResponseList() {
        val geoItems = mapper.mapGeoResponseList(GeoQueryResponseCreator.createNormal().query.data)

        assertThat(geoItems).containsExactlyElementsIn(GeoItemCreator.createNormalList())
    }

    @Test
    fun mapGeoResponseList_Empty() {
        val geoItems = mapper.mapGeoResponseList(emptyList())

        assertThat(geoItems).isEmpty()
    }

    @Test
    fun mapArticleResponseList() {
        val articlesAll = mapper.mapArticleResponseList(ArticleQueryResponseCreator.createNormalUnfiltered().query!!.pages.values.toList(), "en")
        val articlesValid = mapper.mapArticleResponseList(ArticleQueryResponseCreator.createNormalValid().query!!.pages.values.toList(), "en")

        assertThat(articlesAll).containsExactlyElementsIn(ArticleItemCreator.createNormalList())
        assertThat(articlesValid).containsExactlyElementsIn(ArticleItemCreator.createNormalList())
    }

    @Test
    fun mapArticleResponseList_Empty() {
        val articlesAll = mapper.mapArticleResponseList(ArticleQueryResponseCreator.createEmpty().query!!.pages.values.toList(), "en")

        assertThat(articlesAll).isEmpty()
    }

    @Test
    fun mapArticleDetailsResponseList() {
        val details = mapper.mapArticleDetailsResponseList(DetailQueryResponseCreator.createNormal().query.pages.values.toList(), "en")

        assertThat(details).containsExactlyElementsIn(ModelDataProvider.detailItemsBase)
    }

    @Test
    fun mapArticleDetailsResponseList_Empty() {
        val details = mapper.mapArticleDetailsResponseList(emptyList(), "en")

        assertThat(details).isEmpty()
    }
}