package ru.desh.partfinder.core.domain.repository

import ru.desh.partfinder.core.domain.model.User
import ru.desh.partfinder.core.utils.DataOrErrorResult

interface UserRepository {
    suspend fun createUser(user: User): DataOrErrorResult<Boolean, Exception?>
    suspend fun editUser(user: User): DataOrErrorResult<Boolean, Exception?>
    suspend fun deleteUser(user: User): DataOrErrorResult<Boolean, Exception?>
    suspend fun getUserByUid(uid: String): DataOrErrorResult<User?, Exception?>
}