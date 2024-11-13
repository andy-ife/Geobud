package com.andyslab.geobud.data.repository.player

import com.andyslab.geobud.data.model.PlayerModel
import com.andyslab.geobud.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    suspend fun loadPlayerData(): Flow<Resource<PlayerModel>>
    suspend fun savePlayerData(player: PlayerModel): Boolean
}