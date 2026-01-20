package com.practicum.cookbookapp

import android.app.Application
import com.practicum.cookbookapp.di.AppContainer

class RecipesApplication : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)
    }
}