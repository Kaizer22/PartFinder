package ru.desh.partfinder.core.domain.model

data class Ad(
    val uid: String,
    val authorId: String,
    val creationTimestamp: Long,
    val location: String,
    val title: String,
    val tags: List<Tag>,
    val target: String,
    val description: String,
    val adFiles: List<AdFile>,
    val media: List<AdMedia>,
    val userContacts: List<UserContact>,
    val favouritesCount: Int,
    val commentsCount: Int,
    val reputation: Int
)