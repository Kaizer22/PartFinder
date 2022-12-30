package ru.desh.partfinder.features.registration.di

import dagger.BindsInstance
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow
import ru.desh.partfinder.features.registration.presentation.RegistrationFragment
import ru.desh.partfinder.features.registration.presentation.RegistrationState
import ru.desh.partfinder.features.registration.presentation.child_fragments.RegistrationConfirmationFragment
import ru.desh.partfinder.features.registration.presentation.child_fragments.RegistrationDataFragment
import ru.desh.partfinder.features.registration.presentation.child_fragments.RegistrationMethodFragment

@Subcomponent
interface RegistrationComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance registrationState: MutableStateFlow<RegistrationState>
        ): RegistrationComponent
    }

    fun inject(registrationFragment: RegistrationFragment)
    fun inject(registrationMethodFragment: RegistrationMethodFragment)
    fun inject(registrationDataFragment: RegistrationDataFragment)
    fun inject(registrationConfirmationFragment: RegistrationConfirmationFragment)
}