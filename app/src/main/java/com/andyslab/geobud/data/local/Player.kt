package com.andyslab.geobud.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.andyslab.geobud.data.model.PlayerModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.first


const val PLAYER_DATA_NAME = "player_data"
const val PLAYER_DATA_KEY = "player_data_key"
const val TAG = "Quiz Activity"

class Player(private val context: Context) {
    companion object{
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PLAYER_DATA_NAME)
        var instance = PlayerModel()
    }

    suspend fun loadPlayerData(): PlayerModel {
        val dataStoreKey = stringPreferencesKey(PLAYER_DATA_KEY)
        val prefs = context.dataStore.data.first()

        val json = prefs[dataStoreKey]
        val gson = Gson()

        return (gson.fromJson(json, PlayerModel::class.java) ?: PlayerModel()).also{
            instance = it
        }
    }

    suspend fun savePlayerData(player: PlayerModel) {
        val dataStoreKey = stringPreferencesKey(PLAYER_DATA_KEY)
        //convert player data to string so it can be stored with a string preferences key
        val gson = Gson()
        val json = gson.toJson(player)

        context.dataStore.edit {
            it[dataStoreKey] = json
        }
    }

}