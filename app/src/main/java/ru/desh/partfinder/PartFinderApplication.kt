package ru.desh.partfinder

import android.app.Application
import ru.desh.partfinder.core.di.SingleApplicationComponent

// To do list
// TODO Compound views for a survey questions
// TODO Navigation animations
// TODO Rename views ids
// TODO Extract string resources
// TODO Startups and investments news using API and Retrofit

class PartFinderApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        SingleApplicationComponent
            .initApplicationComponent(this)
    }

    companion object {
        internal lateinit var INSTANCE: PartFinderApplication
            private set
    }
}