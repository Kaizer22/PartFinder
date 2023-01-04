package ru.desh.partfinder.core.domain.model

data class User(
    val uid: String,
    val accountUid: String,
    val name: String,
    val surname: String,
    val lastName: String,
    val avatar: String
)