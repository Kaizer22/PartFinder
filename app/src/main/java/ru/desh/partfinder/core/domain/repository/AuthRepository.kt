package ru.desh.partfinder.core.domain.repository

import androidx.lifecycle.LiveData
import ru.desh.partfinder.core.domain.model.Account
import ru.desh.partfinder.core.utils.DataOrErrorResult

interface AuthRepository {
    fun getCurrentAccount(): Account?

    fun createAccountWithEmailAndPassword(
        email: String,
        password: String
    ): LiveData<DataOrErrorResult<Boolean, Exception?>>

    fun sendVerificationEmail(): LiveData<DataOrErrorResult<Boolean, Exception?>>
    fun sendPasswordResetEmail(email: String): LiveData<DataOrErrorResult<Boolean, Exception?>>

    fun createAccountWithGoogleAccount(): LiveData<DataOrErrorResult<Boolean, Exception?>>

    fun createAccountWithPhone(phoneNumber: String): LiveData<DataOrErrorResult<Boolean, Exception?>>
    fun sendVerificationCode(phoneNumber: String): LiveData<DataOrErrorResult<String, Exception?>>
    fun verifyCode(code: String): LiveData<DataOrErrorResult<Boolean, Exception?>>

    fun signInWithGoogle(): LiveData<DataOrErrorResult<Account?, Exception?>>

    fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): LiveData<DataOrErrorResult<Boolean, Exception?>>

    fun createAccountWithFacebook()
    fun signInWithFacebook()

    fun signOut()
}