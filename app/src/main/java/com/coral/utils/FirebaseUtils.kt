package com.coral.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseUtils {
    private const val TAG = "FirebaseUtils"

    val auth: FirebaseAuth by lazy {
        try {
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing FirebaseAuth", e)
            throw e
        }
    }

    val database: FirebaseDatabase by lazy {
        try {
            FirebaseDatabase.getInstance()
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing FirebaseDatabase", e)
            throw e
        }
    }

    fun setupDatabasePersistence() {
        try {
            database.apply {
                setPersistenceEnabled(true)
                getReference(".info/connected").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val connected = snapshot.getValue(Boolean::class.java) ?: false
                        Log.d(TAG, "Firebase connection state: ${if (connected) "connected" else "disconnected"}")
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "Connection state error: ${error.message}")
                    }
                })
            }
        } catch (e: Exception) {
            Log.e(TAG, "Firebase persistence setup failed", e)
        }
    }

    fun isUserAuthenticated(): Boolean = auth.currentUser != null

    fun getCurrentUserId(): String? = auth.currentUser?.uid
}