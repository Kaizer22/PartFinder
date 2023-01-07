package ru.desh.partfinder.core.domain.model

data class Tag(
    val uid: String,
    val text: String
) {
    constructor(): this("", "")
}