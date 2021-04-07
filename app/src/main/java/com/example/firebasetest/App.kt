package com.example.firebasetest

import android.app.Application
import com.google.firebase.FirebaseApp

class App: Application() {
    companion object {
        lateinit var instance: App
            private set

//        val component: AppComponent by lazy { DaggerAppComponent.builder().build() }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        FirebaseApp.initializeApp(this)
    }
}