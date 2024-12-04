package com.andyslab.geobud.data.repository.player

import com.andyslab.geobud.data.model.Landmark
import com.andyslab.geobud.data.model.Player
import com.andyslab.geobud.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    suspend fun loadPlayerData(): Flow<Resource<Player>>

    suspend fun savePlayerData(player: Player): Boolean

    fun generateOptions(landmark: Landmark): Set<String>
}
