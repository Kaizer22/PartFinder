package ru.desh.partfinder.core.utils.exception.properties

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PropertiesRepositoryDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): PropertiesRepository {


    override fun getOnBoardingFlag(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            prefs[PropertiesUtils.KEY_FLAG_ONBOARDING] ?: false
        }
    }

    override suspend fun setOnboardingFinished() {
        dataStore.edit { prefs ->
            prefs[PropertiesUtils.KEY_FLAG_ONBOARDING] = true
        }
    }

    override fun getLanguage(): Flow<PropertyLanguage> {
        // TODO system language by default
        return dataStore.data.map { prefs ->
            PropertyLanguage.valueOf(prefs[PropertiesUtils.KEY_SELECTED_LANGUAGE]
                ?: PropertyLanguage.ENG.name)
        }
    }

    override suspend fun setLanguage(lang: PropertyLanguage) {
        dataStore.edit { prefs ->
            prefs[PropertiesUtils.KEY_SELECTED_LANGUAGE] = lang.name
        }
    }

    override fun getUiMode(): Flow<PropertyUiMode> {
        return dataStore.data.map { prefs ->
            PropertyUiMode.valueOf(prefs[PropertiesUtils.KEY_UI_MODE]
                ?: PropertyUiMode.SYSTEM.name)
        }
    }

    override suspend fun setUiMode(mode: PropertyUiMode) {
        dataStore.edit { prefs ->
            prefs[PropertiesUtils.KEY_UI_MODE] = mode.name
        }
    }

    override fun getIsNotificationsEnabled(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            prefs[PropertiesUtils.KEY_NOTIFICATIONS_ENABLED] ?: true
        }
    }

    override suspend fun setIsNotificationsEnabled(isEnabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[PropertiesUtils.KEY_NOTIFICATIONS_ENABLED]  = isEnabled
        }
    }
}