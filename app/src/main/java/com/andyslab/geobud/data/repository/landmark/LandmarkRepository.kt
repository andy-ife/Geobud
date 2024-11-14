package com.andyslab.geobud.data.repository.landmark

import com.andyslab.geobud.data.model.Landmark
import com.andyslab.geobud.utils.Resource
import kotlinx.coroutines.flow.Flow

interface LandmarkRepository {
    suspend fun fetchLandmarkPhotos(limit: Int): Flow<Resource<Unit>>
    suspend fun getLandmarkById(id: Int): Flow<Landmark>
}