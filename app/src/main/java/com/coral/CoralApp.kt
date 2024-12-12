package com.coral

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CoralApp : Application() {
    companion object {
        private const val TAG = "CoralApp"
        lateinit var instance: CoralApp
            private set

        // Track Firebase initialization state
        private var isFirebaseInitialized = false
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

            // Configure Firebase Database with enhanced error handling
            val database = FirebaseDatabase.getInstance()
            setupDatabaseConfig(database)
            setupConnectionMonitoring(database)

            // Initialize Authentication with monitoring
            setupAuthenticationMonitoring()

            isFirebaseInitialized = true
            Log.d(TAG, "Firebase services initialized and accessible")

        } catch (e: Exception) {
            Log.e(TAG, "Firebase initialization failed", e)
            handleFirebaseInitError(e)
        }
    }

    private fun setupDatabaseConfig(database: FirebaseDatabase) {
        try {
            database.apply {
                // Enable disk persistence for offline capability
                setPersistenceEnabled(true)
                // Set database logging level
                setLogLevel(com.google.firebase.database.Logger.Level.INFO)
                // Set cache size
                setPersistenceCacheSizeBytes(10 * 1024 * 1024) // 10MB cache
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error configuring Firebase Database", e)
        }
    }

    private fun setupConnectionMonitoring(database: FirebaseDatabase) {
        database.getReference(".info/connected")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val connected = snapshot.getValue(Boolean::class.java) ?: false
                    Log.d(TAG, "Firebase connection state changed: ${if (connected) "connected" else "disconnected"}")
                    handleConnectionStateChange(connected)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Firebase connection monitoring failed", error.toException())
                }
            })
    }

    private fun setupAuthenticationMonitoring() {
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            auth.currentUser?.let { user ->
                Log.d(TAG, "User authenticated: ${user.uid}")
                updateUserOnlineStatus(user.uid, true)
            } ?: run {
                Log.d(TAG, "User signed out")
                cleanupUserSession()
            }
        }
    }

    private fun updateUserOnlineStatus(userId: String, online: Boolean) {
        if (!isFirebaseInitialized) return

        try {
            FirebaseDatabase.getInstance()
                .getReference("users/$userId/online")
                .setValue(online)
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to update user online status", e)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user online status", e)
        }
    }

    private fun cleanupUserSession() {
        try {
            // Clean up any user-specific resources or listeners
            FirebaseDatabase.getInstance().purgeOutstandingWrites()
        } catch (e: Exception) {
            Log.e(TAG, "Error cleaning up user session", e)
        }
    }

    private fun handleConnectionStateChange(connected: Boolean) {
        if (!connected) {
            // Handle offline mode
            Log.d(TAG, "Entered offline mode")
        } else {
            // Handle online mode
            Log.d(TAG, "Restored online mode")
            syncPendingData()
        }
    }

    private fun syncPendingData() {
        // Implement any necessary data synchronization when coming back online
        try {
            FirebaseDatabase.getInstance()
                .getReference(".info/serverTimeOffset")
                .get()
                .addOnSuccessListener {
                    Log.d(TAG, "Time sync completed")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Time sync failed", e)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing data", e)
        }
    }

    private fun handleFirebaseInitError(error: Exception) {
        // Log the error details
        Log.e(TAG, "Firebase initialization error details: ${error.message}", error)

        // You could implement additional error handling here:
        // - Show a user notification
        // - Try to reinitialize
        // - Switch to offline mode
    }

    override fun onTerminate() {
        super.onTerminate()
        try {
            // Clean up Firebase resources
            cleanupUserSession()
            FirebaseAuth.getInstance().signOut()
        } catch (e: Exception) {
            Log.e(TAG, "Error during app termination", e)
        }
    }

    // Enhanced Firebase initialization check
    fun isFirebaseInitialized(): Boolean {
        return try {
            isFirebaseInitialized &&
                    FirebaseApp.getInstance() != null &&
                    FirebaseAuth.getInstance() != null &&
                    FirebaseDatabase.getInstance() != null
        } catch (e: Exception) {
            Log.e(TAG, "Firebase initialization check failed", e)
            false
        }
    }

    fun resetFirebaseInstance() {
        try {
            cleanupUserSession()
            FirebaseAuth.getInstance().signOut()
            isFirebaseInitialized = false
            initializeFirebase()
        } catch (e: Exception) {
            Log.e(TAG, "Error resetting Firebase instance", e)
        }
    }
}