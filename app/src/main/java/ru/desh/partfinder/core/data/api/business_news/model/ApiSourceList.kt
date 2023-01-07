package ru.desh.partfinder.core.data.api.business_news.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiSourceList(
    @field:Json(name = "status") val status: String?,
    @field:Json(name = "sources") val sources: List<ApiSource>?
)