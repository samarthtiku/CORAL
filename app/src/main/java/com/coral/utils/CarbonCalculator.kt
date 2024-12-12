package com.coral.utils

/**
 * Utility class for calculating carbon emissions for different activities
 */
object CarbonCalculator {

    // Activity categories for grouping in UI
    enum class Category {
        TRANSPORTATION,
        FOOD,
        HOME_ENERGY,
        WASTE,
        SHOPPING,
        GREEN_ACTIVITIES
    }

    // Mapping of activities to their categories
    fun getActivityCategory(activityType: String): Category {
        return when (activityType) {
            "Driving Car", "Bus Travel", "Train Travel", "Motorcycle Riding",
            "Flying (Short Distance)", "Flying (Long Distance)", "Electric Car",
            "Cycling", "Walking" -> Category.TRANSPORTATION

            "Beef Meal", "Pork Meal", "Chicken Meal", "Fish Meal",
            "Vegetarian Meal", "Vegan Meal" -> Category.FOOD

            "Air Conditioning", "Heating", "Washing Machine",
            "Dishwasher", "Electric Cooking" -> Category.HOME_ENERGY

            "Recycling", "Composting", "General Waste" -> Category.WASTE

            "Shopping (Clothes)", "Shopping (Electronics)",
            "Online Shopping" -> Category.SHOPPING

            "Planting Trees", "Using Renewable Energy",
            "Water Conservation" -> Category.GREEN_ACTIVITIES

            else -> Category.TRANSPORTATION // Default category
        }
    }

    /**
     * Calculate carbon emissions based on activity type and quantity/duration
     * @param activityType The type of activity
     * @param value The quantity or duration of the activity
     * @return Calculated carbon emissions in kg CO2
     */
    fun calculateEmission(activityType: String, value: Double): Double {
        return when (activityType) {
            // Transportation (emissions per minute)
            "Driving Car" -> value * 0.24            // Average car emissions
            "Bus Travel" -> value * 0.12             // Public bus
            "Train Travel" -> value * 0.08           // Electric train
            "Motorcycle Riding" -> value * 0.15      // Average motorcycle
            "Flying (Short Distance)" -> value * 0.85 // <3 hours flight
            "Flying (Long Distance)" -> value * 0.65  // >3 hours flight
            "Electric Car" -> value * 0.10           // EV emissions
            "Cycling" -> 0.0                         // Zero emissions
            "Walking" -> 0.0                         // Zero emissions

            // Food Related (emissions per meal)
            "Beef Meal" -> 6.0 * value              // High impact
            "Pork Meal" -> 3.5 * value              // Medium-high impact
            "Chicken Meal" -> 1.5 * value           // Medium impact
            "Fish Meal" -> 1.8 * value              // Medium impact
            "Vegetarian Meal" -> 0.8 * value        // Low impact
            "Vegan Meal" -> 0.5 * value             // Lowest impact

            // Home Energy (emissions per minute)
            "Air Conditioning" -> value * 0.17       // AC unit
            "Heating" -> value * 0.19               // Home heating
            "Washing Machine" -> value * 0.12       // Average cycle
            "Dishwasher" -> value * 0.10           // Average cycle
            "Electric Cooking" -> value * 0.13      // Electric stove

            // Waste Management (emissions per minute)
            "Recycling" -> value * -0.05           // Negative emissions
            "Composting" -> value * -0.03          // Negative emissions
            "General Waste" -> value * 0.08        // Landfill waste

            // Shopping (emissions per item)
            "Shopping (Clothes)" -> 2.5 * value    // Per clothing item
            "Shopping (Electronics)" -> 4.0 * value // Per electronic item
            "Online Shopping" -> 1.5 * value       // Per item including delivery

            // Green Activities (emissions per minute)
            "Planting Trees" -> value * -0.02      // Negative emissions
            "Using Renewable Energy" -> value * 0.01 // Small positive emissions
            "Water Conservation" -> value * -0.01   // Negative emissions

            else -> 0.0 // Default case
        }
    }

    /**
     * Get the appropriate input type for an activity
     * @param activityType The type of activity
     * @return Either "quantity" or "duration"
     */
    fun getInputType(activityType: String): String {
        return when (activityType) {
            // Fixed emission activities
            "Beef Meal", "Pork Meal", "Chicken Meal", "Fish Meal",
            "Vegetarian Meal", "Vegan Meal", "Shopping (Clothes)",
            "Shopping (Electronics)", "Online Shopping" -> "quantity"

            // Duration-based activities
            else -> "duration"
        }
    }

    /**
     * Get the appropriate input hint for an activity
     * @param activityType The type of activity
     * @return User-friendly hint text
     */
    fun getInputHint(activityType: String): String {
        return when (activityType) {
            // Meal quantities
            "Beef Meal", "Pork Meal", "Chicken Meal", "Fish Meal",
            "Vegetarian Meal", "Vegan Meal" -> "Number of meals"

            // Shopping quantities
            "Shopping (Clothes)", "Shopping (Electronics)",
            "Online Shopping" -> "Number of items"

            // Duration-based activities
            else -> "Duration (minutes)"
        }
    }

    /**
     * Get list of all available activities
     * @return List of activity names
     */
    fun getAllActivities(): List<String> {
        return listOf(
            // Transportation
            "Driving Car", "Bus Travel", "Train Travel", "Motorcycle Riding",
            "Flying (Short Distance)", "Flying (Long Distance)", "Electric Car",
            "Cycling", "Walking",

            // Food
            "Beef Meal", "Pork Meal", "Chicken Meal", "Fish Meal",
            "Vegetarian Meal", "Vegan Meal",

            // Home Energy
            "Air Conditioning", "Heating", "Washing Machine",
            "Dishwasher", "Electric Cooking",

            // Waste Management
            "Recycling", "Composting", "General Waste",

            // Shopping
            "Shopping (Clothes)", "Shopping (Electronics)", "Online Shopping",

            // Green Activities
            "Planting Trees", "Using Renewable Energy", "Water Conservation"
        )
    }

    /**
     * Get activities filtered by category
     * @param category The category to filter by
     * @return List of activities in that category
     */
    fun getActivitiesByCategory(category: Category): List<String> {
        return getAllActivities().filter { activity ->
            getActivityCategory(activity) == category
        }
    }

    /**
     * Get a user-friendly description of an activity's environmental impact
     * @param activityType The type of activity
     * @param emission The calculated emission value
     * @return Impact description string
     */
    fun getImpactDescription(activityType: String, emission: Double): String {
        return when {
            emission <= 0 -> "This activity helps reduce carbon emissions!"
            emission < 1.0 -> "This activity has a low carbon footprint"
            emission < 3.0 -> "This activity has a moderate carbon footprint"
            emission < 5.0 -> "This activity has a high carbon footprint"
            else -> "This activity has a very high carbon footprint"
        }
    }
}