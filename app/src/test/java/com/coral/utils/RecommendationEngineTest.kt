package com.coral.utils

import org.junit.Assert.*
import org.junit.Test

class RecommendationEngineTest {
    @Test
    fun testHighEmissionRecommendations() {
        val recommendations = RecommendationEngine.getRecommendations(150.0)
        val expected = listOf("Switch to public transport", "Reduce driving")
        assertEquals(expected, recommendations)
    }

    @Test
    fun testLowEmissionRecommendations() {
        val recommendations = RecommendationEngine.getRecommendations(30.0)
        assertTrue(recommendations.contains("Keep up the good work!"))
    }
}