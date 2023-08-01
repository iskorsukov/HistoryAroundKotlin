package com.iskorsukov.historyaround.injection.service

import com.iskorsukov.historyaround.injection.BaseWikiUrlFormatString
import dagger.Module
import dagger.Provides
import com.iskorsukov.historyaround.service.api.WikiApi
import com.iskorsukov.historyaround.service.api.interceptors.LanguageCodeInterceptor
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @BaseWikiUrlFormatString
    fun providesBaseWikiUrlFormatString(): String = "https://wikipedia.org/w/"

    @Provides
    @Singleton
    fun providesWikiApi(@BaseWikiUrlFormatString baseWikiUrlFormatString: String, client: OkHttpClient): WikiApi {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseWikiUrlFormatString)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(WikiApi::class.java)
    }

    @Provides
    fun providesLoggingHttpClient(languageCodeInterceptor: LanguageCodeInterceptor): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(languageCodeInterceptor)
            .addInterceptor(logger)
            .build()
    }

}