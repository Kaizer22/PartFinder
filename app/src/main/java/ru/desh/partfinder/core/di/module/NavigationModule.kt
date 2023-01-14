package ru.desh.partfinder.core.di.module

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Qualifier
annotation class AppNavigation

@Qualifier
annotation class RegistrationNavigation

@Qualifier
annotation class UserFormNavigation

@Qualifier
annotation class CreateAdNavigation

@Module
class NavigationModule {
    private val appCicerone = Cicerone.create()
    private lateinit var registrationCicerone: Cicerone<Router>
    private lateinit var userFormCicerone: Cicerone<Router>
    private lateinit var createAdCicerone: Cicerone<Router>

    @Provides
    @AppNavigation
    fun providesAppRouter(): Router {
        return appCicerone.router
    }

    @Provides
    @AppNavigation
    fun providesNavigatorHolder(): NavigatorHolder {
        return appCicerone.getNavigatorHolder()
    }

    @Provides
    @RegistrationNavigation
    fun providesRegistrationRouter(): Router {
        if (!this::registrationCicerone.isInitialized) {
            registrationCicerone = Cicerone.create()
        }
        return registrationCicerone.router
    }

    @Provides
    @RegistrationNavigation
    fun providesRegistrationNavigatorHolder(): NavigatorHolder {
        if (!this::registrationCicerone.isInitialized) {
            registrationCicerone = Cicerone.create()
        }
        return registrationCicerone.getNavigatorHolder()
    }

    @Provides
    @UserFormNavigation
    fun providesUserFormRouter(): Router {
        if (!this::userFormCicerone.isInitialized) {
            userFormCicerone = Cicerone.create()
        }
        return userFormCicerone.router
    }

    @Provides
    @UserFormNavigation
    fun providesUserFormNavigatorHolder(): NavigatorHolder {
        if (!this::userFormCicerone.isInitialized) {
            userFormCicerone = Cicerone.create()
        }
        return userFormCicerone.getNavigatorHolder()
    }

    @Provides
    @CreateAdNavigation
    fun providesCreateAdRouter(): Router {
        if (!this::createAdCicerone.isInitialized) {
            createAdCicerone = Cicerone.create()
        }
        return createAdCicerone.router
    }

    @Provides
    @CreateAdNavigation
    fun providesCreateAdNavigatorHolder(): NavigatorHolder {
        if (!this::createAdCicerone.isInitialized) {
            createAdCicerone = Cicerone.create()
        }
        return createAdCicerone.getNavigatorHolder()
    }
}