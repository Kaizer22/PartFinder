package ru.desh.partfinder.core.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import ru.desh.partfinder.core.MainActivity
import ru.desh.partfinder.core.di.module.*
import ru.desh.partfinder.features.BottomNavigationActivity
import ru.desh.partfinder.features.ads.di.CreateAdComponent
import ru.desh.partfinder.features.ads.presentation.HomePageFragment
import ru.desh.partfinder.features.auth.presentation.AuthFragment
import ru.desh.partfinder.features.auth.presentation.CodeEnterFragment
import ru.desh.partfinder.features.auth.presentation.PasswordResetFragment
import ru.desh.partfinder.features.auth.presentation.PhoneAuthFragment
import ru.desh.partfinder.features.registration.di.RegistrationComponent
import ru.desh.partfinder.features.start.presentation.PrivacyPolicyFragment
import ru.desh.partfinder.features.start.presentation.SplashScreenActivity
import ru.desh.partfinder.features.start.presentation.WelcomeFragment
import javax.inject.Singleton



@Component(modules = [AppSubcomponents::class,
    NavigationModule::class, DataModule::class, RepositoryModule::class,
FirebaseModule::class, ApiModule::class])
@Singleton
interface ApplicationComponent {
    @Component.Factory
    interface Factory{
        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }

    fun registrationComponentFactory(): RegistrationComponent.Factory
    fun createAdComponentFactory(): CreateAdComponent.Factory

    fun inject(splashScreenActivity: SplashScreenActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(bottomNavigationActivity: BottomNavigationActivity)

    fun inject(welcomeFragment: WelcomeFragment)
    fun inject(privacyPolicyFragment: PrivacyPolicyFragment)
    fun inject(authFragment: AuthFragment)
    fun inject(phoneAuthFragment: PhoneAuthFragment)
    fun inject(codeEnterFragment: CodeEnterFragment)
    fun inject(passwordResetFragment: PasswordResetFragment)
    fun inject(homePageFragment: HomePageFragment)

}

@Module(subcomponents = [RegistrationComponent::class, CreateAdComponent::class])
class AppSubcomponents

