package ru.desh.partfinder.core.data.properties

import kotlinx.coroutines.flow.Flow

interface PropertiesRepository {
    fun getOnBoardingFlag(): Flow<Boolean>
    suspend fun setOnboardingFinished()

    fun getLanguage(): Flow<PropertyLanguage>
    suspend fun setLanguage(lang: PropertyLanguage)

    fun getUiMode(): Flow<PropertyUiMode>
    suspend fun setUiMode(mode: PropertyUiMode)

    fun getIsNotificationsEnabled(): Flow<Boolean>
    suspend fun setIsNotificationsEnabled(isEnabled: Boolean)
}

enum class PropertyUiMode { DARK, LIGHT, SYSTEM }
enum class PropertyLanguage { RU, ENG }
