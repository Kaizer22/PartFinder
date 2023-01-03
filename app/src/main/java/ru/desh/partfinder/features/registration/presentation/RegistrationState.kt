package ru.desh.partfinder.features.registration.presentation

sealed class RegistrationState {
    object InitState : RegistrationState()

    data class RegistrationMethodSelected(
        val registrationType: RegistrationFragment.RegistrationType
        ): RegistrationState()

    object PhoneInputFinished : RegistrationState()
    object EmailInputFinished : RegistrationState()
    object DataConfirmed: RegistrationState()
    object RegistrationFinished: RegistrationState()
}
