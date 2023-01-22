package ru.desh.partfinder.core.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.desh.partfinder.core.di.ViewModelKey
import ru.desh.partfinder.core.ui.ViewModelFactory
import ru.desh.partfinder.features.ads.presentation.CreateAdViewModel
import ru.desh.partfinder.features.ads.presentation.HomePageViewModel
import ru.desh.partfinder.features.ads.presentation.child_fragments.CreateAdContactsViewModel
import ru.desh.partfinder.features.ads.presentation.child_fragments.CreateAdDescriptionViewModel
import ru.desh.partfinder.features.ads.presentation.child_fragments.CreateAdFilesViewModel
import ru.desh.partfinder.features.ads.presentation.child_fragments.CreateAdTargetViewModel
import ru.desh.partfinder.features.auth.presentation.AuthViewModel
import ru.desh.partfinder.features.auth.presentation.CodeEnterViewModel
import ru.desh.partfinder.features.auth.presentation.PasswordResetViewModel
import ru.desh.partfinder.features.auth.presentation.PhoneAuthViewModel
import ru.desh.partfinder.features.registration.presentation.RegistrationViewModel
import ru.desh.partfinder.features.registration.presentation.child_fragments.*
import ru.desh.partfinder.features.settings.presentation.AppSettingsViewModel
import ru.desh.partfinder.features.start.presentation.PrivacyPolicyViewModel
import ru.desh.partfinder.features.start.presentation.WelcomeViewModel

@Module
abstract class AppViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PhoneAuthViewModel::class)
    abstract fun bindPhoneAuthViewModel(viewModel: PhoneAuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomePageViewModel::class)
    abstract fun bindHomePageViewModel(viewModel: HomePageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CodeEnterViewModel::class)
    abstract fun bindCodeEnterViewModel(viewModel: CodeEnterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PasswordResetViewModel::class)
    abstract fun bindPasswordResetViewModel(viewModel: PasswordResetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PrivacyPolicyViewModel::class)
    abstract fun bindPrivacyPolicyViewModel(viewModel: PrivacyPolicyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    abstract fun bindWelcomeViewModel(viewModel: WelcomeViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(AppSettingsViewModel::class)
    abstract fun bindAppSettingsViewModel(viewModel: AppSettingsViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    abstract fun bindRegistrationViewModel(viewModel: RegistrationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostRegistrationViewModel::class)
    abstract fun bindPostRegistrationViewModel(viewModel: PostRegistrationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationConfirmationViewModel::class)
    abstract fun bindRegistrationConfirmationViewModel(viewModel: RegistrationConfirmationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationDataViewModel::class)
    abstract fun bindRegistrationDataViewModel(viewModel: RegistrationDataViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationMethodViewModel::class)
    abstract fun bindRegistrationMethodViewModel(viewModel: RegistrationMethodViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationNameViewModel::class)
    abstract fun bindRegistrationNameViewModel(viewModel: RegistrationNameViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateAdViewModel::class)
    abstract fun bindCreateAdViewModel(viewModel: CreateAdViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateAdContactsViewModel::class)
    abstract fun bindCreateAdContactsViewModel(viewModel: CreateAdContactsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateAdDescriptionViewModel::class)
    abstract fun bindCreateAdDescriptionViewModel(viewModel: CreateAdDescriptionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateAdFilesViewModel::class)
    abstract fun bindCreateAdFilesViewModel(viewModel: CreateAdFilesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateAdTargetViewModel::class)
    abstract fun bindCreateAdTargetViewModel(viewModel: CreateAdTargetViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

