package com.iskorsukov.historyaround.service.api

import com.iskorsukov.historyaround.data.response.article.ArticleQueryResponse
import com.iskorsukov.historyaround.data.response.detail.DetailQueryResponse
import com.iskorsukov.historyaround.data.response.geo.GeoQueryResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WikiApi {

    companion object {
        const val LANGUAGE_HEADER = "language"
    }

    @GET("api.php?action=query&list=geosearch&gslimit=500&format=json")
    suspend fun loadGeoData(@Header(LANGUAGE_HEADER) languageCode: String?, @Query("gsradius") radius: Int, @Query("gscoord") coordString: String): GeoQueryResponse

    @GET("api.php?action=query&prop=coordinates|description|pageimages&colimit=500&pithumbsize=400&format=json")
    suspend fun loadArticlesData(@Header(LANGUAGE_HEADER) languageCode: String?, @Query("pageids") pageidsString: String): ArticleQueryResponse

    @GET("api.php?action=query&prop=extracts|pageimages|info|coordinates&exlimit=1&exchars=1200&inprop=url&pithumbsize=600&format=json")
    suspend fun loadDetailsData(@Header(LANGUAGE_HEADER) languageCode: String?, @Query("pageids") pageid: String): DetailQueryResponse
}