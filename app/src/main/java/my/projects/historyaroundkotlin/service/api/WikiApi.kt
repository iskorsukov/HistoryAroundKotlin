package my.projects.historyaroundkotlin.service.api

import io.reactivex.Single
import my.projects.historyaroundkotlin.data.response.article.ArticleQueryResponse
import my.projects.historyaroundkotlin.data.response.detail.DetailQueryResponse
import my.projects.historyaroundkotlin.data.response.geo.GeoQueryResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WikiApi {

    companion object {
        const val LANGUAGE_HEADER = "language"
    }

    @GET("api.php?action=query&list=geosearch&gslimit=500&format=json")
    fun loadGeoData(@Header(LANGUAGE_HEADER) languageCode: String?, @Query("gsradius") radius: Int, @Query("gscoord") coordString: String): Single<GeoQueryResponse>

    @GET("api.php?action=query&prop=coordinates|description|pageimages&colimit=500&pithumbsize=400&format=json")
    fun loadArticlesData(@Header(LANGUAGE_HEADER) languageCode: String?, @Query("pageids") pageidsString: String): Single<ArticleQueryResponse>

    @GET("api.php?action=query&prop=extracts|pageimages|info|coordinates&explaintext=true&exlimit=1&exchars=1200&inprop=url&pithumbsize=600&format=json")
    fun loadDetailsData(@Header(LANGUAGE_HEADER) languageCode: String?, @Query("pageids") pageid: String): Single<DetailQueryResponse>
}