package ru.desh.partfinder.features.registration.di

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow
import ru.desh.partfinder.features.registration.presentation.RegistrationFragment
import ru.desh.partfinder.features.registration.presentation.RegistrationState
import ru.desh.partfinder.features.registration.presentation.child_fragments.*

@Subcomponent
interface RegistrationComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): RegistrationComponent
    }

    fun inject(registrationFragment: RegistrationFragment)

    fun inject(registrationMethodFragment: RegistrationMethodFragment)
    fun inject(registrationDataFragment: RegistrationDataFragment)
    fun inject(registrationConfirmationFragment: RegistrationConfirmationFragment)
    fun inject(registrationNameFragment: RegistrationNameFragment)
    fun inject(postRegistrationFragment: PostRegistrationFragment)
}

@Module
class RegistrationModule {
    private val registrationState by lazy {
        MutableStateFlow<RegistrationState>(RegistrationState.InitState)
    }

    @Provides
    fun provideRegistrationState(): MutableStateFlow<RegistrationState> {
        return registrationState
    }
}