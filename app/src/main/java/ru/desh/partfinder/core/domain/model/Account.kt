package ru.desh.partfinder.core.domain.model

import java.util.UUID

data class Account(
    val uid: String,
    val registrationType: RegistrationType,
    val isActive: Boolean,
    val phone: String,
    val email: String,
    val displayName: String
) {
    enum class RegistrationType{
        PHONE, EMAIL, GOOGLE
    }

    companion object {
        fun getEmpty(): Account {
            return Account(
                UUID.randomUUID().toString(),
                RegistrationType.EMAIL,
                isActive = false,
                "+78001234567",
                "q@q.com",
                "Ivan"
            )
        }
    }
}