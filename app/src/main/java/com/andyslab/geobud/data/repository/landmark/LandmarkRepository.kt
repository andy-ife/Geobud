package com.andyslab.geobud.data.repository.landmark

import android.graphics.Bitmap
import com.andyslab.geobud.data.model.Landmark
import com.andyslab.geobud.data.model.Player
import com.andyslab.geobud.utils.Resource
import kotlinx.coroutines.flow.Flow

interface LandmarkRepository {
    suspend fun fetchLandmarkPhotos(limit: Int): Flow<Resource<Float>>

    suspend fun addProgress(player: Player)

    suspend fun getLandmarkById(id: Int): Landmark

    suspend fun getMaxId(): Int

    suspend fun resetProgress()

    suspend fun savePhotoToExternalStorage(
        displayName: String,
        bmp: Bitmap,
    ): Boolean
}
