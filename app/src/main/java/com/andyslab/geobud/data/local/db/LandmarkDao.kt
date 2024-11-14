package com.andyslab.geobud.data.local.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.andyslab.geobud.data.model.Landmark
import kotlinx.coroutines.flow.Flow

@Dao
interface LandmarkDao {
    @Query("SELECT * FROM landmark WHERE id = :id")
    fun getLandmarkById(id: Int): Flow<Landmark>

    @Update(Landmark::class)
    suspend fun updateLandmark(landmark: Landmark)

    @Query("SELECT MAX(id) FROM landmark")
    fun getMaxId(): Flow<Int>
}