package ru.desh.partfinder.core.di.module

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import ru.desh.partfinder.core.di.AppNavigation
import ru.desh.partfinder.core.di.RegistrationNavigation
import ru.desh.partfinder.core.di.UserFormNavigation

@Module
class NavigationModule {
    private val appCicerone = Cicerone.create()
    private lateinit var registrationCicerone: Cicerone<Router>
    private lateinit var userFormCicerone: Cicerone<Router>

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
    fun providesRegistrationNavigationHolder(): NavigatorHolder {
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
    fun providesUserFormNavigationHolder(): NavigatorHolder {
        if (!this::userFormCicerone.isInitialized) {
            userFormCicerone = Cicerone.create()
        }
        return userFormCicerone.getNavigatorHolder()
    }
}