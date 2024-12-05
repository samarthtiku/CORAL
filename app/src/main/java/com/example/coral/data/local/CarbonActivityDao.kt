package com.example.coral.data.local

import androidx.room.*
import com.example.coral.data.CarbonActivity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarbonActivityDao {
    @Query("SELECT * FROM carbon_activities ORDER BY timestamp DESC")
    fun getAllActivities(): Flow<List<CarbonActivity>>

    @Insert
    suspend fun insertActivity(activity: CarbonActivity)

    @Query("SELECT SUM(co2Amount) FROM carbon_activities")
    fun getTotalCO2(): Flow<Double>
}