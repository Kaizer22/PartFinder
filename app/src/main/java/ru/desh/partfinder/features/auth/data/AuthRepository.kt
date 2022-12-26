package ru.desh.partfinder.features.auth.data

import kotlinx.coroutines.flow.StateFlow
import ru.desh.partfinder.core.domain.model.Account
import ru.desh.partfinder.core.utils.DataOrErrorResult

interface AuthRepository {
    fun createUserWithEmailAndPassword(email:String, password:String): StateFlow<DataOrErrorResult<Account?, Exception?>>
    fun createUserWithGoogleAccount(): StateFlow<DataOrErrorResult<Account?, Exception?>>
    fun createUserWithPhone(): StateFlow<DataOrErrorResult<Account?, Exception?>>

    fun signInWithGoogle(): StateFlow<DataOrErrorResult<Account?, Exception?>>
    fun signInWithPhone(): StateFlow<DataOrErrorResult<Account?, Exception?>>
    fun signInWithEmailAndPassword(email: String, password: String): StateFlow<DataOrErrorResult<Account?, Exception?>>

    fun createUserWithFacebook()
    fun signInWithFacebook()
}