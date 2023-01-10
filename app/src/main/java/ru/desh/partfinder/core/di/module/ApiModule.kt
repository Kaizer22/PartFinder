package ru.desh.partfinder.core.di.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.desh.partfinder.core.data.api.business_news.ApiUtils
import ru.desh.partfinder.core.data.api.business_news.NewsApi
import ru.desh.partfinder.core.data.api.interceptors.LoggingInterceptor
import ru.desh.partfinder.core.data.api.interceptors.NewsApiAuthenticationInterceptor
import javax.inject.Singleton

@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideNewsApi(
        builder: Retrofit.Builder
    ): NewsApi {
        return builder.build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun providesNewsApiRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(ApiUtils.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
    }

    @Provides
    fun provideHttpLoggingInterceptor(loggingInterceptor: LoggingInterceptor): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(loggingInterceptor)
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    fun provideOkHttpClient(
        newsApiAuthenticationInterceptor: NewsApiAuthenticationInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(newsApiAuthenticationInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }
}