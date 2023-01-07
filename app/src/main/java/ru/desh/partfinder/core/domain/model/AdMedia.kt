package ru.desh.partfinder.core.domain.model

data class AdMedia(
    val uid: String,
    val mediaType: String,
    val link: String,
    val displayName: String,
    val description: String
) {
    enum class AdMediaType {
        IMAGE, VIDEO
    }
    constructor(): this("", "", "", "", "")
}