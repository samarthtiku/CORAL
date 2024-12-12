// In app/src/main/java/com/coral/models/ActivityLog.kt
package com.coral.models

data class ActivityLog(
    val id: String = "",
    val type: String = "",
    val duration: Double = 0.0,
    val emission: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)