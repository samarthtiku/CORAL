package com.coral.utils

object RecommendationEngine {
    fun getRecommendations(carbonEmission: Double): List<String> {
        return when {
            carbonEmission > 100 -> listOf("Switch to public transport", "Reduce driving")
            carbonEmission > 50 -> listOf("Carpool with others", "Use a bike")
            else -> listOf("Keep up the good work!")
        }
    }
}
