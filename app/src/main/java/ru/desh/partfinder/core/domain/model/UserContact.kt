package ru.desh.partfinder.core.domain.model

data class UserContact(
    val uid: String,
    val type: String,
    val value: String
) {
    enum class UserContactType {
        EMAIL, VK, TELEGRAM, FACEBOOK, INSTAGRAM, TWITTER, LINKEDIN
    }
    constructor(): this("", "", "")
}
