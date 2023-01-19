package ru.desh.partfinder.core.data.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.desh.partfinder.core.data.api.business_news.ApiParams.API_KEY

import ru.desh.partfinder.core.data.api.business_news.ApiUtils.QHZV_DSL_NHB
import javax.inject.Inject

class NewsApiAuthenticationInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authorizedUrl = request.url.newBuilder()
            .addQueryParameter(API_KEY, QHZV_DSL_NHB)
            .build()

        return chain.proceed(
            request.newBuilder()
                .url(authorizedUrl).build()
        )
    }
}