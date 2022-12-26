package ru.desh.partfinder.core.di.module

import dagger.Binds
import dagger.Module
import ru.desh.partfinder.core.data.properties.PropertiesRepository
import ru.desh.partfinder.core.data.properties.PropertiesRepositoryDataStoreImpl
import ru.desh.partfinder.features.auth.data.AuthRepository
import ru.desh.partfinder.features.auth.data.FirebaseAuthRepositoryImpl
import javax.inject.Singleton

@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun propertiesRepository(repo: PropertiesRepositoryDataStoreImpl): PropertiesRepository

    @Binds
    @Singleton
    abstract fun authRepository(repo: FirebaseAuthRepositoryImpl): AuthRepository
}