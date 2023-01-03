package ru.desh.partfinder.core.domain.repository

import androidx.lifecycle.LiveData
import ru.desh.partfinder.core.domain.model.Account
import ru.desh.partfinder.core.utils.DataOrErrorResult

interface AuthRepository {
    // Using LiveData because there is no need to have initial state,
    // just needs a simple observable result, which is independent from specific libs and frameworks
    fun createAccountWithEmailAndPassword(email:String, password:String): LiveData<DataOrErrorResult<Account?, Exception?>>
    fun sendVerificationEmail(): LiveData<DataOrErrorResult<Boolean, Exception?>>
    fun sendPasswordResetEmail(email: String): LiveData<DataOrErrorResult<Boolean, Exception?>>

    fun createAccountWithGoogleAccount(): LiveData<DataOrErrorResult<Account?, Exception?>>

    fun createAccountWithPhone(phoneNumber: String): LiveData<DataOrErrorResult<Account?, Exception?>>
    fun sendVerificationCode(phoneNumber: String): LiveData<DataOrErrorResult<Account?, Exception?>>
    fun verifyCode(code: String): LiveData<DataOrErrorResult<Account?, Exception?>>

    fun signInWithGoogle(): LiveData<DataOrErrorResult<Account?, Exception?>>

    fun signInWithEmailAndPassword(email: String, password: String): LiveData<DataOrErrorResult<Account?, Exception?>>

    fun createAccountWithFacebook()
    fun signInWithFacebook()
}