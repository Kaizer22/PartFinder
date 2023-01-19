package ru.desh.partfinder.core.domain.repository

import androidx.lifecycle.LiveData
import ru.desh.partfinder.core.domain.model.Account
import ru.desh.partfinder.core.utils.DataOrErrorResult

interface AuthRepository {
    fun getCurrentAccount(): Account?

    suspend fun createAccountWithEmailAndPassword(
        email: String,
        password: String
    ): DataOrErrorResult<Boolean, Exception?>

    suspend fun sendVerificationEmail(): DataOrErrorResult<Boolean, Exception?>
    suspend fun sendPasswordResetEmail(email: String): DataOrErrorResult<Boolean, Exception?>

    suspend fun createAccountWithGoogleAccount(): DataOrErrorResult<Boolean, Exception?>

    suspend fun createAccountWithPhone(phoneNumber: String): DataOrErrorResult<Boolean, Exception?>
    fun sendVerificationCode(phoneNumber: String): LiveData<DataOrErrorResult<String, Exception?>>
    suspend fun verifyCode(code: String): DataOrErrorResult<Boolean, Exception?>

    suspend fun signInWithGoogle(): DataOrErrorResult<Account?, Exception?>

    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): DataOrErrorResult<Boolean, Exception?>

    fun createAccountWithFacebook()
    fun signInWithFacebook()

    fun signOut()
}