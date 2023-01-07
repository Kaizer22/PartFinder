package ru.desh.partfinder.core.data.api.business_news

import ru.desh.partfinder.BuildConfig

object ApiEndpoints {
    const val EVERYTHING_ENDPOINT_V2 = "/v2/everything"
    const val TOP_HEADLINES_ENDPOINT_V2 = "/v2/top-headlines"
    const val SOURCES_ENDPOINT_V2 = "/v2/top-headlines/sources"
}

object ApiUtils {
    const val BASE_URL = "https://newsapi.org"
    //const val API_V2 = "/v2/"
    const val DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val QHZV_DSL_NHB = BuildConfig.QHZV_DSL_NHB
}

object ApiParams {
    const val API_KEY = "apiKey"
    const val CATEGORY = "category"
    const val LANGUAGE = "language"
    const val COUNTRY = "country"
    const val SOURCES = "sources"
    const val DOMAINS = "domains"
    const val EXCLUDE_DOMAINS = "excludeDomains"
    const val QUERY = "q"
    const val SEARCH_IN = "searchIn"
    const val FROM = "from"
    const val TO = "to"
    const val SORT_BY = "sortBy"
    const val PAGE_SIZE = "pageSize"
    const val PAGE = "page"
}