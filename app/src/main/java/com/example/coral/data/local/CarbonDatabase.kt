package com.example.coral.data.local

package com.example.coral.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.coral.data.CarbonActivity

@Database(entities = [CarbonActivity::class], version = 1)
@TypeConverters(Converters::class)
abstract class CarbonDatabase : RoomDatabase() {
    abstract fun carbonActivityDao(): CarbonActivityDao
}