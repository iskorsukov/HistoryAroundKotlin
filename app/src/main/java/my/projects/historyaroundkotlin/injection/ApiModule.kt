package my.projects.historyaroundkotlin.injection

import dagger.Module
import dagger.Provides
import my.projects.historyaroundkotlin.service.api.WikiApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {

    @Provides
    @BaseWikiUrlFormatString
    fun providesBaseWikiUrlFormatString(): String = "https://%s.wikipedia.org/w/"

    @Provides
    @LanguageString
    fun providesLanguageString(): String = "en"

    @Provides
    fun providesWikiApi(@BaseWikiUrlFormatString baseWikiUrlFormatString: String, @LanguageString language: String): WikiApi {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseWikiUrlFormatString.format(language))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(WikiApi::class.java)
    }

}