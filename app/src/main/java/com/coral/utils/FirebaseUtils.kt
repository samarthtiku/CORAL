package com.coral.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

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
}