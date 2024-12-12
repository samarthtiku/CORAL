package com.coral.utils

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CarbonCalculatorTest {

    @Before
    fun setup() {
        // Setup if needed
    }

    // Transportation Emission Tests
    @Test
    fun testTransportationEmissions() {
        // Test car emissions
        assertEquals(14.4, CarbonCalculator.calculateEmission("Driving Car", 60.0), 0.01)

        // Test public transport
        assertEquals(7.2, CarbonCalculator.calculateEmission("Bus Travel", 60.0), 0.01)
        assertEquals(4.8, CarbonCalculator.calculateEmission("Train Travel", 60.0), 0.01)

        // Test zero emission transport
        assertEquals(0.0, CarbonCalculator.calculateEmission("Cycling", 60.0), 0.01)
        assertEquals(0.0, CarbonCalculator.calculateEmission("Walking", 60.0), 0.01)
    }

    // Food Emission Tests
    @Test
    fun testFoodEmissions() {
        assertEquals(6.0, CarbonCalculator.calculateEmission("Beef Meal", 1.0), 0.01)
        assertEquals(3.5, CarbonCalculator.calculateEmission("Pork Meal", 1.0), 0.01)
        assertEquals(1.5, CarbonCalculator.calculateEmission("Chicken Meal", 1.0), 0.01)
        assertEquals(0.8, CarbonCalculator.calculateEmission("Vegetarian Meal", 1.0), 0.01)
        assertEquals(0.5, CarbonCalculator.calculateEmission("Vegan Meal", 1.0), 0.01)
    }

    // Home Energy Tests
    @Test
    fun testHomeEnergyEmissions() {
        assertEquals(10.2, CarbonCalculator.calculateEmission("Air Conditioning", 60.0), 0.01)
        assertEquals(11.4, CarbonCalculator.calculateEmission("Heating", 60.0), 0.01)
        assertEquals(7.2, CarbonCalculator.calculateEmission("Washing Machine", 60.0), 0.01)
    }

    // Category Tests
    @Test
    fun testActivityCategories() {
        assertEquals(
            CarbonCalculator.Category.TRANSPORTATION,
            CarbonCalculator.getActivityCategory("Driving Car")
        )
        assertEquals(
            CarbonCalculator.Category.FOOD,
            CarbonCalculator.getActivityCategory("Vegetarian Meal")
        )
        assertEquals(
            CarbonCalculator.Category.HOME_ENERGY,
            CarbonCalculator.getActivityCategory("Air Conditioning")
        )
    }

    // Input Validation Tests
    @Test
    fun testInputTypes() {
        assertEquals("duration", CarbonCalculator.getInputType("Driving Car"))
        assertEquals("quantity", CarbonCalculator.getInputType("Beef Meal"))
        assertEquals("quantity", CarbonCalculator.getInputType("Shopping (Clothes)"))
    }

    @Test
    fun testInputHints() {
        assertEquals("Duration (minutes)", CarbonCalculator.getInputHint("Driving Car"))
        assertEquals("Number of meals", CarbonCalculator.getInputHint("Vegetarian Meal"))
        assertEquals("Number of items", CarbonCalculator.getInputHint("Shopping (Clothes)"))
    }

    // Activity List Tests
    @Test
    fun testGetAllActivities() {
        val activities = CarbonCalculator.getAllActivities()
        assertTrue(activities.contains("Driving Car"))
        assertTrue(activities.contains("Vegetarian Meal"))
        assertTrue(activities.contains("Air Conditioning"))
        assertTrue(activities.size > 10) // Should have a substantial number of activities
    }

    @Test
    fun testGetActivitiesByCategory() {
        val transportActivities = CarbonCalculator.getActivitiesByCategory(
            CarbonCalculator.Category.TRANSPORTATION
        )
        assertTrue(transportActivities.contains("Driving Car"))
        assertTrue(transportActivities.contains("Cycling"))
        assertFalse(transportActivities.contains("Vegetarian Meal"))
    }

    // Edge Cases
    @Test
    fun testEdgeCases() {
        // Test zero values
        assertEquals(0.0, CarbonCalculator.calculateEmission("Driving Car", 0.0), 0.01)

        // Test very large values
        assertTrue(CarbonCalculator.calculateEmission("Driving Car", 1000000.0).isFinite())

        // Test unknown activity
        assertEquals(0.0, CarbonCalculator.calculateEmission("Unknown Activity", 60.0), 0.01)
    }

    // Impact Description Tests
    @Test
    fun testImpactDescriptions() {
        assertEquals(
            "This activity helps reduce carbon emissions!",
            CarbonCalculator.getImpactDescription("Cycling", 0.0)
        )
        assertEquals(
            "This activity has a very high carbon footprint",
            CarbonCalculator.getImpactDescription("Driving Car", 14.4)
        )
    }
}
