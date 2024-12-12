package com.coral.models

import org.junit.Assert.*
import org.junit.Test

class ActivityLogTest {
    @Test
    fun testActivityLogCreation() {
        val activity = ActivityLog(
            id = "test-id",
            type = "Driving Car",
            duration = 60.0,
            emission = 14.4,
            timestamp = 1234567890
        )
        assertEquals("test-id", activity.id)
        assertEquals("Driving Car", activity.type)
    }
}