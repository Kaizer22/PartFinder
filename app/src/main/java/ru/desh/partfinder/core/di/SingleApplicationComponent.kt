package ru.desh.partfinder.core.di

import android.content.Context

object SingleApplicationComponent {
    private var INSTANCE: ApplicationComponent? = null
    private lateinit var appContext: Context
    fun initApplicationComponent(context: Context){
        this.appContext = context
    }
    fun getInstance(): ApplicationComponent {
        return INSTANCE ?: DaggerApplicationComponent
            .factory().create(appContext).also {
                INSTANCE = it
            }
    }
}