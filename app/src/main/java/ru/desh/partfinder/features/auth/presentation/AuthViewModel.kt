package ru.desh.partfinder.features.auth.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import ru.desh.partfinder.core.Screens.BottomNavigation
import ru.desh.partfinder.core.Screens.PasswordReset
import ru.desh.partfinder.core.Screens.PhoneAuth
import ru.desh.partfinder.core.Screens.Registration
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.core.domain.repository.AuthRepository
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @AppNavigation private val router: Router
) : ViewModel() {

    data class AuthState(
        val isLoading: Boolean = false,
        val signedIn: Boolean = false,
        val signInFailed: Boolean = false,
        val error: Exception? = null
    )

    private val _authState = MutableLiveData(AuthState())
    val authState: LiveData<AuthState> = _authState

    fun toBottomNavigation() = router.navigateTo(BottomNavigation())
    fun toPasswordReset() = router.navigateTo(PasswordReset())
    fun toPhoneAuth() = router.navigateTo(PhoneAuth())
    fun toRegistration() = router.navigateTo(Registration())

    fun authWithEmailAndPassword(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _authState.value = _authState.value?.copy(isLoading = true)
            // TODO check if current user has confirmed email
            val result = authRepository.signInWithEmailAndPassword(email, password)
            if (!result.isException) {
                _authState.value = _authState.value?.copy(
                    signedIn = result.data!!,
                    isLoading = false
                )
            } else {
                _authState.value = _authState.value?.copy(
                    signInFailed = true,
                    error = result.exception!!,
                    isLoading = false
                )
            }
        }
        return
    }
}