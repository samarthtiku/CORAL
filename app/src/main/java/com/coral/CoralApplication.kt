package com.coral

import android.app.Application
import com.google.firebase.FirebaseApp
import android.util.Log

class CoralApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeFirebase()
    }

    private fun initializeFirebase() {
        try {
            FirebaseApp.initializeApp(this)
            Log.d("CoralApplication", "Firebase initialized successfully in Application class")
        } catch (e: Exception) {
            Log.e("CoralApplication", "Failed to initialize Firebase", e)
        }
    }
}