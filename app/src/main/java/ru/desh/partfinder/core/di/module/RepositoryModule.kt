package ru.desh.partfinder.core.di.module

import dagger.Binds
import dagger.Module
import ru.desh.partfinder.core.data.api.business_news.BusinessNewsRepositoryImpl
import ru.desh.partfinder.core.data.firebase.FirebaseAdRepositoryImpl
import ru.desh.partfinder.core.data.properties.PropertiesRepository
import ru.desh.partfinder.core.data.properties.PropertiesRepositoryDataStoreImpl
import ru.desh.partfinder.core.domain.repository.AuthRepository
import ru.desh.partfinder.core.data.firebase.FirebaseAuthRepositoryImpl
import ru.desh.partfinder.core.data.firebase.FirebaseUserRepositoryImpl
import ru.desh.partfinder.core.domain.repository.AdRepository
import ru.desh.partfinder.core.domain.repository.BusinessNewsRepository
import ru.desh.partfinder.core.domain.repository.UserRepository
import javax.inject.Singleton

@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun propertiesRepository(repo: PropertiesRepositoryDataStoreImpl): PropertiesRepository
    @Binds
    @Singleton
    abstract fun authRepository(repo: FirebaseAuthRepositoryImpl): AuthRepository
    @Binds
    @Singleton
    abstract fun userRepository(repo: FirebaseUserRepositoryImpl): UserRepository
    @Binds
    @Singleton
    abstract fun adRepository(repo: FirebaseAdRepositoryImpl): AdRepository
    @Binds
    @Singleton
    abstract fun businessNewsRepository(repo: BusinessNewsRepositoryImpl): BusinessNewsRepository
}