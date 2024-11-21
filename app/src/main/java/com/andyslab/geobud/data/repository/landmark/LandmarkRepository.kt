package com.andyslab.geobud.data.repository.landmark

import com.andyslab.geobud.data.model.Player
import com.andyslab.geobud.utils.Resource
import kotlinx.coroutines.flow.Flow

interface LandmarkRepository {
    suspend fun fetchLandmarkPhotos(limit: Int): Flow<Resource<Float>>
    suspend fun addProgress(player: Player)
    suspend fun getMaxId(): Int
}