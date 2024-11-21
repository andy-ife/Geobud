package com.andyslab.geobud.data.repository.landmark

import android.util.Log
import com.andyslab.geobud.data.local.db.LandmarkDatabase
import com.andyslab.geobud.data.model.Landmark
import com.andyslab.geobud.data.model.Player
import com.andyslab.geobud.data.remote.LandmarkPhotoAPI
import com.andyslab.geobud.data.repository.player.PlayerRepository
import com.andyslab.geobud.utils.Resource
import com.bumptech.glide.module.AppGlideModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.coroutineContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

class LandmarkRepoImpl @Inject constructor(
    private val db: LandmarkDatabase,
    private val api: LandmarkPhotoAPI,
    private val playerRepo: PlayerRepository
) : LandmarkRepository, AppGlideModule() {

    override suspend fun fetchLandmarkPhotos(limit: Int): Flow<Resource<Float>> {
        return flow {
            val player = playerRepo.loadPlayerData().first { it is Resource.Success }.data ?: return@flow
            val maxId = db.dao.getMaxId().first()
            for(i in player.progress.until(player.progress+limit)){
                val p = (i.toFloat()/(player.progress+limit).toFloat()) + 0.05f
                emit(Resource.Loading(p)) // loading progress

                if(i > maxId) {
                    emit(Resource.Success())
                    return@flow
                }

                var landmark = db.dao.getLandmarkById(i).first()
                if(landmark.photoUrl == null){
                    try{
                        val response = api.getLandmarkPhoto(
                            searchTerm = landmark.name,
                            page = Random.nextInt(1,4),
                            perPage = 1,
                        )
                        if(response.isSuccessful && response.body() != null){
                            landmark = landmark.copy(
                                photoUrl = response.body()?.photos?.first()?.src?.portrait,
                                photographer = response.body()?.photos?.first()?.photographer,
                                photographerUrl = response.body()?.photos?.first()?.photographer_url
                            )
                            db.dao.updateLandmark(landmark)
                        }
                    }catch(e: Exception){
                        emit(Resource.Error(e.message.toString())) // "Couldn't fetch photos. Check your internet connection and try again."
                        Log.d("Error fetching photos", e.message.toString())
                        return@flow
                    }
                }
            }
            emit(Resource.Success())
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun addProgress(player: Player){
        withContext(Dispatchers.IO){
            if(player.progress < db.dao.getMaxId().first()){
                player.run{
                    progress++
                    currentLandmark = db.dao.getLandmarkById(progress).first()
                    currentOptions = playerRepo.generateOptions(currentLandmark!!)
                    isFirstLaunch = false
                    playerRepo.savePlayerData(this)
                }
            }
        }
    }
}