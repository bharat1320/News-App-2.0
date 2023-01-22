package com.project.news.baseApplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {

    companion object{
        var context : BaseApplication? = null
    }

    override fun onCreate() {
        super.onCreate()

        context = this

    }
}