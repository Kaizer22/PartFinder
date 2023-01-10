package ru.desh.partfinder.core.domain.model

import java.util.Date

data class BusinessArticle(
    val source: String,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: Date,
)