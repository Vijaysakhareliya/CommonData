package com.basecontent

import android.app.Application
import android.content.SharedPreferences

open class MyApp : Application() {

    companion object {
        private lateinit var instance: MyApp

        fun getInstance(): MyApp = instance

        lateinit var preferences: SharedPreferences
        lateinit var prefEditor: SharedPreferences.Editor

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}