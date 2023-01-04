package ru.desh.partfinder.core.utils.exception.properties

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PropertiesUtils {
    val KEY_FLAG_ONBOARDING = booleanPreferencesKey("onboarding_flag")
    val KEY_SELECTED_LANGUAGE = stringPreferencesKey("language")
    val KEY_UI_MODE = stringPreferencesKey("ui_mode")
    val KEY_NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
}