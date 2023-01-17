package ru.desh.partfinder.features.auth.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import ru.desh.partfinder.core.Screens.BottomNavigation
import ru.desh.partfinder.core.Screens.PasswordReset
import ru.desh.partfinder.core.Screens.PhoneAuth
import ru.desh.partfinder.core.Screens.Registration
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.core.domain.repository.AuthRepository
import ru.desh.partfinder.core.utils.DataOrErrorResult
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @AppNavigation private val router: Router
) : ViewModel() {

    fun toBottomNavigation() = router.navigateTo(BottomNavigation())
    fun toPasswordReset() = router.navigateTo(PasswordReset())
    fun toPhoneAuth() = router.navigateTo(PhoneAuth())
    fun toRegistration() = router.navigateTo(Registration())

    fun authWithEmailAndPassword(
        email: String,
        password: String
    ): LiveData<DataOrErrorResult<Boolean, Exception?>> {
        Log.d("TAG", "$email $password")
        return authRepository.signInWithEmailAndPassword(email, password)
    }
}