package ru.desh.partfinder.features.ads.di

import dagger.BindsInstance
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow
import ru.desh.partfinder.features.ads.presentation.CreateAdFragment
import ru.desh.partfinder.features.ads.presentation.CreateAdState
import ru.desh.partfinder.features.ads.presentation.child_fragments.*

@Subcomponent
interface CreateAdComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance createAdState: MutableStateFlow<CreateAdState>
        ): CreateAdComponent
    }

    fun inject(createAdFragment: CreateAdFragment)

    fun inject(createAdContactsFragment: CreateAdContactsFragment)
    fun inject(createAdDescriptionFragment: CreateAdDescriptionFragment)
    fun inject(createAdFilesFragment: CreateAdFilesFragment)
    fun inject(createAdTargetFragment: CreateAdTargetFragment)
    fun inject(postCreateAdFragment: PostCreateAdFragment)
}