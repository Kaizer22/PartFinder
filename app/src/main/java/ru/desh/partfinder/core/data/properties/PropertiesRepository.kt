package ru.desh.partfinder.core.data.properties

interface PropertiesRepository {
    fun getOnBoardingFlag(): Boolean
    fun setOnboardingFinished()

    fun getLanguage(): PropertiesLanguage
    fun setLanguage(lang: PropertiesLanguage)

    fun getUiMode(): PropertiesUiMode
    fun setUiMode(mode: PropertiesUiMode)

    fun getIsNotificationsEnabled(): Boolean
    fun setIsNotificationsEnabled(isEnabled: Boolean)
}

enum class PropertiesUiMode { DARK, LIGHT, SYSTEM }
enum class PropertiesLanguage { RU, ENG }
