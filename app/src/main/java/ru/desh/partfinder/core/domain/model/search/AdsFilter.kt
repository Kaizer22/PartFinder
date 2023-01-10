package ru.desh.partfinder.core.domain.model.search

data class AdsFilter(
    val region: String,
    val publicationDate: Long,
    val adType: String,

    val searchString: String,
    val sortOption: String
)