package com.coral

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CoralApp : Application() {
    companion object {
        private const val TAG = "CoralApp"
        lateinit var instance: CoralApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initializeFirebase()
    }

    private fun initializeFirebase() {
        try {
            // Initialize Firebase if not already initialized
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
                Log.d(TAG, "Firebase initialized successfully")
            } else {
                Log.d(TAG, "Firebase was already initialized")
            }

            // Configure Firebase Database
            FirebaseDatabase.getInstance().apply {
                // Enable disk persistence for offline capability
                setPersistenceEnabled(true)
                // Set database logging level
                setLogLevel(com.google.firebase.database.Logger.Level.INFO)
            }

            // Initialize Authentication
            FirebaseAuth.getInstance()

            Log.d(TAG, "Firebase services initialized and accessible")

        } catch (e: Exception) {
            Log.e(TAG, "Firebase initialization failed", e)
            // You might want to handle this error more gracefully in production
            // For example, showing a user-friendly error message or fallback behavior
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        try {
            // Clean up Firebase resources if needed
            FirebaseDatabase.getInstance().purgeOutstandingWrites()
        } catch (e: Exception) {
            Log.e(TAG, "Error during app termination", e)
        }
    }

    // Helper method to check if Firebase is properly initialized
    fun isFirebaseInitialized(): Boolean {
        return try {
            FirebaseApp.getInstance() != null &&
                    FirebaseAuth.getInstance() != null &&
                    FirebaseDatabase.getInstance() != null
        } catch (e: Exception) {
            Log.e(TAG, "Firebase initialization check failed", e)
            false
        }
    }
}