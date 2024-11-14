package com.andyslab.geobud.data.repository.player

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.andyslab.geobud.data.local.db.LandmarkDatabase
import com.andyslab.geobud.data.local.sources.CountriesDataSource
import com.andyslab.geobud.data.model.Landmark
import com.andyslab.geobud.data.model.Player
import com.andyslab.geobud.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayerRepoImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val countries: CountriesDataSource,
    private val db: LandmarkDatabase,
): PlayerRepository {

    private val heartsKey = intPreferencesKey("player_hearts")
    private val progressKey = intPreferencesKey("player_progress")
    private val firstLaunchKey = booleanPreferencesKey("first_launch")

    companion object {
        var instance: Player? = null
    }

    override suspend fun loadPlayerData(): Flow<Resource<Player>> {
        return flow {
            withContext(Dispatchers.IO){
                if(instance == null){
                    try{
                        val prefs = dataStore.data.first()
                        val hearts = prefs[heartsKey] ?: 3
                        val progress = prefs[progressKey] ?: 0
                        val isFirstLaunch = prefs[firstLaunchKey] ?: true

                        val landmark = db.dao.getLandmarkById(progress).first()

                        Player(
                            progress = progress,
                            currentLandmark = landmark,
                            currentOptions = generateOptions(landmark),
                            hearts = hearts,
                            isFirstLaunch = isFirstLaunch
                        ).also{
                            instance = it
                            emit(Resource.Success(it))
                        }
                    }catch(e: IOException){
                        emit(Resource.Error("Error loading player data"))
                        Log.d("Error loading player data", e.message.toString())
                    }
                }
            }
        }
    }

    override suspend fun savePlayerData(player: Player): Boolean {
        withContext(Dispatchers.IO){
            try{
                dataStore.edit{ prefs ->
                    prefs[heartsKey] = player.hearts
                    prefs[progressKey] = player.progress
                    prefs[firstLaunchKey] = player.isFirstLaunch
                }
            }catch(e: IOException){
                Log.d("Datastore IO Exception", e.message.toString())
                return@withContext false
            }
        }
        return true
    }

    override fun generateOptions(landmark: Landmark): Set<String>{
        val result = mutableSetOf<String>()
        val country = landmark.country
        result.add(country)

        when(country){
            in countries.AS -> {
                result.add(countries.EU.random())
                result.add(countries.AS.random())
                result.add(countries.AS.random())
        }
            in countries.AF -> {
                result.add(countries.AF.random())
                result.add(countries.AF.random())
                result.add(countries.EU.random())
        }
            in countries.EU -> {
                result.add(countries.NA.random())
                result.add(countries.EU.random())
                result.add(countries.EU.random())
            }
            in countries.NA -> {
                result.add(countries.EU.random())
                result.add(countries.NA.random())
                result.add(countries.EU.random())
            }
            in countries.SA -> {
                result.add(countries.SA.random())
                result.add(countries.EU.random())
                result.add(countries.OC.random())
            }
            in countries.OC -> {
                result.add(countries.SA.random())
                result.add(countries.NA.random())
                result.add(countries.OC.random())
            }
        }
        return result.shuffled().toMutableSet()
    }
}