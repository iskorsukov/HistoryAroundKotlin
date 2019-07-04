package my.projects.historyaroundkotlin.service.api

import io.reactivex.Single
import my.projects.historyaroundkotlin.data.response.article.ArticleQueryResponse
import my.projects.historyaroundkotlin.data.response.detail.DetailQueryResponse
import my.projects.historyaroundkotlin.data.response.geo.GeoQueryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WikiApi {
    @GET("api.php?action=query&list=geosearch")
    fun loadGeoData(@Query("gsradius") radius: Int, @Query("gscoord") coordString: String): Single<GeoQueryResponse>

    @GET("api.php?action=query&prop=coordinates|pageimages")
    fun loadArticlesData(@Query("pageids") pageidsString: String): Single<ArticleQueryResponse>

    @GET("api.php?action=query&prop=extracts|pageimages|info&explaintext=true&exlimit=1&exchars=1200&inprop=url")
    fun loadDetailsData(@Query("pageids") pageid: String): Single<DetailQueryResponse>
}