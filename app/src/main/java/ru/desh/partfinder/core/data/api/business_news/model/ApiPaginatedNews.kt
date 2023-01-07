package ru.desh.partfinder.core.data.api.business_news.model

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPaginatedNews(
    @field:Json(name = "status")
    @SerializedName("status") val status: String?,
    @field:Json(name = "totalResults")
    @SerializedName("totalResults") val totalResults: Int?,
    @field:Json(name = "articles")
    @SerializedName("articles") val articles: List<ApiArticle>?
)