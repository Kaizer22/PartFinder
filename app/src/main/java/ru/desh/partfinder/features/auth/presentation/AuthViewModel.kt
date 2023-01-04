package ru.desh.partfinder.features.auth.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.desh.partfinder.core.domain.model.Account
import ru.desh.partfinder.core.utils.DataOrErrorResult
import ru.desh.partfinder.core.domain.repository.AuthRepository
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    fun authWithEmailAndPassword(email: String, password: String): LiveData<DataOrErrorResult<Account?, Exception?>> {
        Log.d("TAG", "$email $password")
        return authRepository.signInWithEmailAndPassword(email, password)
    }

    fun testSignUnWithEmailAndPassword(email: String, password: String): LiveData<DataOrErrorResult<Account?, Exception?>> {
        Log.d("TAG", "sign in $email $password")
        return authRepository.createAccountWithEmailAndPassword(email, password)
    }

}