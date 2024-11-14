package com.andyslab.geobud.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andyslab.geobud.data.model.Landmark

@Database(entities = [Landmark::class], version = 1, exportSchema = true)
abstract class LandmarkDatabase: RoomDatabase() {
    abstract val dao: LandmarkDao
}