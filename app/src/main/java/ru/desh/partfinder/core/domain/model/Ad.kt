package ru.desh.partfinder.core.domain.model

import java.util.UUID

data class Ad(
    var uid: String,
    var type: String,
    var category: String,
    var authorId: String,
    var creationTimestamp: Long,
    var location: String,
    var title: String,
    var tags: List<Tag>,
    var target: String,
    var description: String,
    var adFiles: List<AdFile>,
    var media: List<AdMedia>,
    var userContacts: List<UserContact>,
    var favouritesCount: Int,
    var commentsCount: Int,
    var reputation: Int
) {
    // TODO get rid of empty constructors for Firebase
    constructor() : this(UUID.randomUUID().toString(),
        "", "", "", 0L, "", "",
    emptyList(), "", "", emptyList(), emptyList(), emptyList(),
        0, 0, 0)
}

enum class AdType(val value: String) {
    SUGGEST_INVESTMENTS("suggest_investments"), LOOK_INVESTMENTS("look_investments"),
    LOOK_BUSINESS_PARTNER("look_business_partner"), LOOK_SUPPLIER("look_supplier"),
    SUGGEST_SERVICES("suggest_services")
}