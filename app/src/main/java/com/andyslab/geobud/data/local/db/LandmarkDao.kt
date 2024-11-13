package com.andyslab.geobud.data.local.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.andyslab.geobud.data.model.LandmarkModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LandmarkDao {
    @Query("SELECT * FROM landmark WHERE id = :id")
    suspend fun getLandmarkById(id: Int): Flow<LandmarkModel>

    @Update(LandmarkModel::class)
    suspend fun updateLandmark(landmark: LandmarkModel)
}