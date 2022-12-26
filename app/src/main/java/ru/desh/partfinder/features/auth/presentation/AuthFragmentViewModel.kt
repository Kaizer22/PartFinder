package ru.desh.partfinder.features.auth.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import ru.desh.partfinder.core.domain.model.Account
import ru.desh.partfinder.core.utils.DataOrErrorResult
import ru.desh.partfinder.features.auth.data.AuthRepository
import javax.inject.Inject

class AuthFragmentViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    fun authWithEmailAndPassword(email: String, password: String): StateFlow<DataOrErrorResult<Account?, Exception?>> {
        Log.d("TAG", "$email $password")
        return authRepository.signInWithEmailAndPassword(email, password)
    }

    fun testSignUnWithEmailAndPassword(email: String, password: String): StateFlow<DataOrErrorResult<Account?, Exception?>> {
        Log.d("TAG", "sign in $email $password")
        return authRepository.createUserWithEmailAndPassword(email, password)
    }

}