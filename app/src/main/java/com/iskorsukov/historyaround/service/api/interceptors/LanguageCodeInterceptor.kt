package com.iskorsukov.historyaround.service.api.interceptors

import com.iskorsukov.historyaround.service.api.WikiApi
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.regex.Pattern
import javax.inject.Inject

class LanguageCodeInterceptor @Inject constructor(): Interceptor {
    private val pattern = Pattern.compile("^(https?:\\/\\/)?(.+?)\$")

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(specifyLanguage(chain.request()))
    }

    private fun specifyLanguage(request: Request): Request {
        val languageCode = request.header(WikiApi.LANGUAGE_HEADER)
        return if (languageCode != null) {
            val url = request.url()
            val urlWithLanguage = addLanguage(url, languageCode)
            request.newBuilder().url(urlWithLanguage).removeHeader(WikiApi.LANGUAGE_HEADER).build()
        } else {
            request
        }
    }

    private fun addLanguage(url: HttpUrl, languageCode: String): HttpUrl {
        val matcher = pattern.matcher(url.toString())
        matcher.find()
        return if (matcher.groupCount() == 1) {
            // no schema group
            HttpUrl.parse("$languageCode.$url")!!
        } else {
            HttpUrl.parse("${matcher.group(1)}$languageCode.${matcher.group(2)}")!!
        }
    }
}