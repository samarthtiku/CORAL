package com.example.coral.domain

enum class ActivityType {
    CAR_TRAVEL,
    ELECTRICITY_USAGE,
    AIR_TRAVEL,
    PUBLIC_TRANSPORT,
    HOUSEHOLD;

    fun getDisplayName(): String = when(this) {
        CAR_TRAVEL -> "Car Travel"
        ELECTRICITY_USAGE -> "Electricity Usage"
        AIR_TRAVEL -> "Air Travel"
        PUBLIC_TRANSPORT -> "Public Transport"
        HOUSEHOLD -> "Household"
    }

    fun getUnit(): String = when(this) {
        CAR_TRAVEL, AIR_TRAVEL, PUBLIC_TRANSPORT -> "kilometers"
        ELECTRICITY_USAGE -> "kilowatt-hours"
        HOUSEHOLD -> "units"
    }

    fun getCo2Factor(): Double = when(this) {
        CAR_TRAVEL -> 0.2
        ELECTRICITY_USAGE -> 0.5
        AIR_TRAVEL -> 0.1
        PUBLIC_TRANSPORT -> 0.04
        HOUSEHOLD -> 0.3
    }
}
