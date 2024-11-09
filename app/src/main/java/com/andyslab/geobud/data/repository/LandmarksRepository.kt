package com.andyslab.geobud.data.repository

import android.graphics.drawable.Drawable
import com.andyslab.geobud.data.model.PlayerModel
import com.andyslab.geobud.utils.Resource
import kotlinx.coroutines.flow.Flow

interface LandmarksRepository {
    //suspend fun loadPlayerData(): PlayerData
    //suspend fun savePlayerData(player: PlayerData)
    fun updatePlayerData(player: PlayerModel)

    suspend fun loadPhoto(imageUrl: String?): Drawable?

    suspend fun getLandmarkPhotoURLs(player: PlayerModel): Flow<Resource<MutableSet<String?>>>
    suspend fun checkAndUpdatePhotoURLs(player: PlayerModel): Flow<Resource<MutableSet<String?>>>
    suspend fun downloadAndCachePhotos(player: PlayerModel): Flow<Resource<Unit>>

    fun generateOptions(player: PlayerModel): MutableSet<String>
}