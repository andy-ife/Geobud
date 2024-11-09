package com.andyslab.geobud.data.repository

import android.graphics.drawable.Drawable
import com.andyslab.geobud.data.model.PlayerDto
import com.andyslab.geobud.utils.Resource
import kotlinx.coroutines.flow.Flow

interface LandmarksRepository {
    //suspend fun loadPlayerData(): PlayerData
    //suspend fun savePlayerData(player: PlayerData)
    fun updatePlayerData(player: PlayerDto)

    suspend fun loadPhoto(imageUrl: String?): Drawable?

    suspend fun getLandmarkPhotoURLs(player: PlayerDto): Flow<Resource<MutableSet<String?>>>
    suspend fun checkAndUpdatePhotoURLs(player: PlayerDto): Flow<Resource<MutableSet<String?>>>
    suspend fun downloadAndCachePhotos(player: PlayerDto): Flow<Resource<Unit>>

    fun generateOptions(player: PlayerDto): MutableSet<String>
}