package ru.desh.partfinder.core.domain.repository

import androidx.lifecycle.LiveData
import ru.desh.partfinder.core.domain.model.User
import ru.desh.partfinder.core.utils.DataOrErrorResult

interface UserRepository {
    fun createUser(user: User): LiveData<DataOrErrorResult<Boolean, Exception>>
    fun editUser(user: User): LiveData<DataOrErrorResult<Boolean, Exception>>
    fun deleteUser(user: User): LiveData<DataOrErrorResult<Boolean, Exception>>
    fun getUserByUid(uid: String): LiveData<DataOrErrorResult<User?, Exception>>
}