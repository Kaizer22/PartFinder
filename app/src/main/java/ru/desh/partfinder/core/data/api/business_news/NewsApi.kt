package ru.desh.partfinder.core.data.api.business_news

import retrofit2.http.GET
import retrofit2.http.Query
import ru.desh.partfinder.core.data.api.business_news.model.ApiPaginatedNews
import ru.desh.partfinder.core.data.api.business_news.model.ApiSourceList

// Api for providing latest business news to a "News" section of HomePageFragment
interface NewsApi {
    @GET(ApiEndpoints.EVERYTHING_ENDPOINT_V2)
    suspend fun getEverything(
        // Required
        //@Query(ApiParams.API_KEY) apiKey: String,
        // Optional
        @Query(ApiParams.QUERY) query: String? = null,
        @Query(ApiParams.SEARCH_IN) searchIn: String? = null,
        @Query(ApiParams.SOURCES) sources: String? = null,
        @Query(ApiParams.DOMAINS) domains: String? = null,
        @Query(ApiParams.EXCLUDE_DOMAINS) excludeDomains: String? = null,
        @Query(ApiParams.FROM) from: String? = null,
        @Query(ApiParams.TO) to: String? = null,
        @Query(ApiParams.LANGUAGE) language: String? = null,
        @Query(ApiParams.SORT_BY) sortBy: String? = null,
        @Query(ApiParams.PAGE_SIZE) pageSize: Int? = null,
        @Query(ApiParams.PAGE) page: Int? = null
    ): ApiPaginatedNews

    @GET(ApiEndpoints.TOP_HEADLINES_ENDPOINT_V2)
    suspend fun getTopHeadlines(
        // Required
        //@Query(ApiParams.API_KEY) apiKey: String,
        // Optional
        @Query(ApiParams.COUNTRY) country: String? = null,
        @Query(ApiParams.CATEGORY) category: String? = null,
        @Query(ApiParams.SOURCES) sources: String? = null,
        @Query(ApiParams.QUERY) query: String? = null,
        @Query(ApiParams.PAGE_SIZE) pageSize: Int? = null,
        @Query(ApiParams.PAGE) page: Int? = null
    ): ApiPaginatedNews

    @GET(ApiEndpoints.SOURCES_ENDPOINT_V2)
    fun getSources(
        // Required
        //@Query(ApiParams.API_KEY) apiKey: String,
        // Optional
        @Query(ApiParams.COUNTRY) country: String? = null,
        @Query(ApiParams.CATEGORY) category: String? = null,
        @Query(ApiParams.LANGUAGE) language: String? = null,
    ): ApiSourceList
}