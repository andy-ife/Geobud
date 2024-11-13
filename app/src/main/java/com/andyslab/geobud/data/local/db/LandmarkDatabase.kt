package com.andyslab.geobud.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andyslab.geobud.data.model.LandmarkModel

@Database(entities = [LandmarkModel::class], version = 1)
abstract class LandmarkDatabase: RoomDatabase() {
    abstract val dao: LandmarkDao
}