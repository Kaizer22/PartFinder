package ru.desh.partfinder.features.ads.presentation


sealed class CreateAdState {
    object InitState : CreateAdState()
    object TargetSubmitted : CreateAdState()
    object DescriptionSubmitted : CreateAdState()
    object FilesSubmitted : CreateAdState()
    object AdPublished : CreateAdState()
}