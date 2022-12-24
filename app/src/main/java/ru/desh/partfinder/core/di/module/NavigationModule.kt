package ru.desh.partfinder.core.di.module

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides

@Module
class NavigationModule {
    private val cicerone = Cicerone.create()

    @Provides
    fun providesRouter(): Router {
        return cicerone.router
    }

    @Provides
    fun providesNavigatorHolder(): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }
}