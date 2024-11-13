package com.andyslab.geobud.data.repository.player

import com.andyslab.geobud.data.model.PlayerModel

interface PlayerRepository {
    suspend fun loadPlayerData(): PlayerModel
    suspend fun savePlayerData(player: PlayerModel)
}