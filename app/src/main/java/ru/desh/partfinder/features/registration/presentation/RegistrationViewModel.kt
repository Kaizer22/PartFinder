package ru.desh.partfinder.features.registration.presentation

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.desh.partfinder.core.Screens.Post_Registration
import ru.desh.partfinder.core.Screens.Registration_Confirmation
import ru.desh.partfinder.core.Screens.Registration_Data
import ru.desh.partfinder.core.Screens.Registration_Method
import ru.desh.partfinder.core.Screens.Registration_Name
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.core.di.module.RegistrationNavigation
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    @AppNavigation private val router: Router,
    @RegistrationNavigation private val innerRouter: Router,
    @RegistrationNavigation private val innerNavigatorHolder: NavigatorHolder,
    private val _registrationState: MutableStateFlow<RegistrationState>,
) : ViewModel() {

    fun observeRegistrationState(): StateFlow<RegistrationState> = _registrationState

    fun back() = router.exit()
    fun toRegistrationMethod() = innerRouter.navigateTo(Registration_Method())
    fun toRegistrationData(registrationType: RegistrationFragment.RegistrationType) =
        innerRouter.navigateTo(
            Registration_Data(registrationType)
        )

    fun toPhoneConfirmation() = innerRouter.navigateTo(
        Registration_Confirmation(
            RegistrationFragment.RegistrationType.PHONE
        )
    )

    fun toEmailConfirmation() = innerRouter.navigateTo(
        Registration_Confirmation(
            RegistrationFragment.RegistrationType.EMAIL
        )
    )

    fun toRegistrationNameForm() = innerRouter.navigateTo(
        Registration_Name()
    )

    fun toPostRegistration() = innerRouter.navigateTo(
        Post_Registration()
    )

    fun clearInnerNavigation() = innerNavigatorHolder.removeNavigator()

    fun initInnerNavigation(navigator: Navigator) = innerNavigatorHolder.setNavigator(navigator)
}