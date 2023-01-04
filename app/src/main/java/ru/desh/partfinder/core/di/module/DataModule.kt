package ru.desh.partfinder.core.di.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module
class DataModule {
    companion object {
        const val PREFERENCES_DATA_STORE = "preferences"
    }

    @Provides
    @Singleton
    fun provideDataStore(context: Context): DataStore<Preferences> {
        //TODO
        val datastore = PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(PREFERENCES_DATA_STORE)
        }
        return datastore
    }
}


