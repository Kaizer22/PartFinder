package ru.desh.partfinder.features.ads.di

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.features.ads.presentation.CreateAdFragment
import ru.desh.partfinder.features.ads.presentation.CreateAdState
import ru.desh.partfinder.features.ads.presentation.child_fragments.*
import javax.inject.Qualifier

@Subcomponent//(modules = [CreateAdModule::class])
interface CreateAdComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): CreateAdComponent
    }

    fun inject(createAdFragment: CreateAdFragment)

    fun inject(createAdContactsFragment: CreateAdContactsFragment)
    fun inject(createAdDescriptionFragment: CreateAdDescriptionFragment)
    fun inject(createAdFilesFragment: CreateAdFilesFragment)
    fun inject(createAdTargetFragment: CreateAdTargetFragment)
    fun inject(postCreateAdFragment: PostCreateAdFragment)
}

@Qualifier
annotation class BufferAd

@Module
class CreateAdModule {
    private val _createAdState = MutableStateFlow<CreateAdState>(CreateAdState.InitState)
    private val bufferAd = Ad()

    @BufferAd
    @Provides
    fun providesBufferAd(): Ad = bufferAd

    @Provides
    fun provideCreateAdState(): MutableStateFlow<CreateAdState> = _createAdState
}