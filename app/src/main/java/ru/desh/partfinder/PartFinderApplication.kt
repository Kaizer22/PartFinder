package ru.desh.partfinder

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ru.desh.partfinder.core.di.SingleApplicationComponent

// To do ideas list
// TODO Compound views for a different types of survey questions
// TODO Navigation animations
// TODO Master/Detail for chats in landscape orientation
// TODO Unify input validation process
// TODO Human readable errors in case of exceptions

class PartFinderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Because app doesn't fully ready to dark/light theming
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        INSTANCE = this
        SingleApplicationComponent
            .initApplicationComponent(this)
    }

    companion object {
        internal lateinit var INSTANCE: PartFinderApplication
            private set
    }
}