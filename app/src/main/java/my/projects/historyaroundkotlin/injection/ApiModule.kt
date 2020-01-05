package my.projects.historyaroundkotlin.injection

import dagger.Module
import dagger.Provides
import my.projects.historyaroundkotlin.service.api.WikiApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

@Module
class ApiModule {

    @Provides
    @BaseWikiUrlFormatString
    fun providesBaseWikiUrlFormatString(): String = "https://wikipedia.org/w/"

    @Provides
    fun providesWikiApi(@BaseWikiUrlFormatString baseWikiUrlFormatString: String, client: OkHttpClient): WikiApi {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseWikiUrlFormatString)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

        return retrofit.create(WikiApi::class.java)
    }

    @Provides
    fun providesLoggingHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(logger).build()
    }

}