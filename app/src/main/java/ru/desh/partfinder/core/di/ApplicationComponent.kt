package ru.desh.partfinder.core.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import ru.desh.partfinder.core.MainActivity
import ru.desh.partfinder.core.di.module.DataModule
import ru.desh.partfinder.core.di.module.FirebaseModule
import ru.desh.partfinder.core.di.module.NavigationModule
import ru.desh.partfinder.core.di.module.RepositoryModule
import ru.desh.partfinder.features.auth.presentation.AuthFragment
import ru.desh.partfinder.features.auth.presentation.PhoneAuthFragment
import ru.desh.partfinder.features.registration.di.RegistrationComponent
import ru.desh.partfinder.features.registration.presentation.NameFormFragment
import ru.desh.partfinder.features.registration.presentation.RegistrationFragment
import ru.desh.partfinder.features.registration.presentation.child_fragments.RegistrationMethodFragment
import ru.desh.partfinder.features.start.presentation.SplashScreenActivity
import ru.desh.partfinder.features.start.presentation.WelcomeFragment
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class AppNavigation
@Qualifier
annotation class RegistrationNavigation
@Qualifier
annotation class UserFormNavigation


@Component(modules = [AppSubcomponents::class,
    NavigationModule::class, DataModule::class, RepositoryModule::class,
FirebaseModule::class])
@Singleton
interface ApplicationComponent {
    @Component.Factory
    interface Factory{
        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }

    fun registrationComponentFactory(): RegistrationComponent.Factory

    fun inject(splashScreenActivity: SplashScreenActivity)
    fun inject(mainActivity: MainActivity)

    fun inject(welcomeFragment: WelcomeFragment)
    fun inject(authFragment: AuthFragment)
    fun inject(phoneAuthFragment: PhoneAuthFragment)
    fun inject(nameFormFragment: NameFormFragment)
}

@Module(subcomponents = [RegistrationComponent::class])
class AppSubcomponents

