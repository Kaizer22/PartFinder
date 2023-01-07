package ru.desh.partfinder.core.di.module

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

//@Qualifier
//annotation class ActivityNavigation

@Qualifier
annotation class AppNavigation
@Qualifier
annotation class RegistrationNavigation
@Qualifier
annotation class UserFormNavigation
@Qualifier
annotation class HomePageNavigation

@Module
class NavigationModule {
    //private val activityCicerone = Cicerone.create()
    private val appCicerone = Cicerone.create()
    private lateinit var registrationCicerone: Cicerone<Router>
    private lateinit var userFormCicerone: Cicerone<Router>
    private lateinit var homePageCicerone: Cicerone<Router>

//    @Provides
//    @ActivityNavigation
//    fun providesActivityNavigationRouter(): Router {
//        return activityCicerone.router
//    }
//
//    @Provides
//    @ActivityNavigation
//    fun providesActivityNavigatorHolder(): NavigatorHolder {
//        return activityCicerone.getNavigatorHolder()
//    }

    @Provides
    @AppNavigation
    fun providesAppRouter(): Router {
//        if (!this::appCicerone.isInitialized) {
//            appCicerone = Cicerone.create()
//        }
        return appCicerone.router
    }

    @Provides
    @AppNavigation
    fun providesNavigatorHolder(): NavigatorHolder {
//        if (!this::appCicerone.isInitialized) {
//            appCicerone = Cicerone.create()
//        }
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
    @HomePageNavigation
    fun providesHomePageRouter(): Router {
        if (!this::homePageCicerone.isInitialized) {
            homePageCicerone = Cicerone.create()
        }
        return homePageCicerone.router
    }

    @Provides
    @HomePageNavigation
    fun providesHomePageNavigatorHolder(): NavigatorHolder {
        if (!this::homePageCicerone.isInitialized) {
            homePageCicerone = Cicerone.create()
        }
        return homePageCicerone.getNavigatorHolder()
    }
}