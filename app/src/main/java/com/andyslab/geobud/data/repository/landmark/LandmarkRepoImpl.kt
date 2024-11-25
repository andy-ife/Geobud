package com.andyslab.geobud.data.repository.landmark

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import com.andyslab.geobud.data.local.db.LandmarkDatabase
import com.andyslab.geobud.data.model.Landmark
import com.andyslab.geobud.data.model.Player
import com.andyslab.geobud.data.remote.LandmarkPhotoAPI
import com.andyslab.geobud.data.repository.player.PlayerRepoImpl
import com.andyslab.geobud.data.repository.player.PlayerRepository
import com.andyslab.geobud.utils.Resource
import com.andyslab.geobud.utils.sdk29AndUp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

class LandmarkRepoImpl @Inject constructor(
    private val context: Context,
    private val db: LandmarkDatabase,
    private val api: LandmarkPhotoAPI,
    private val playerRepo: PlayerRepository
) : LandmarkRepository{

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
                            page = 1,
                            perPage = 4,
                        )
                        if(response.isSuccessful && response.body() != null){
                            val photo = response.body()?.photos!!.let{ photos ->
                                if(photos.size >= 4)
                                    photos.filter{
                                        it.alt.lowercase().contains(landmark.name.split(" ").first())
                                    }.ifEmpty{ listOf(photos[0]) }.random() // this algo improves photo selection
                                else photos.first()
                            }

                            landmark = landmark.copy(
                                photoUrl = photo.src.portrait,
                                photographer = photo.photographer,
                                photographerUrl = photo.photographer_url
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

    override suspend fun getMaxId(): Int {
        return withContext(Dispatchers.IO){ db.dao.getMaxId().first() }
    }

    override suspend fun resetProgress() {
        withContext(Dispatchers.IO){
            playerRepo.savePlayerData(Player()) //save a new Player with default params
            PlayerRepoImpl.instance = null
            //context.deleteDatabase("landmark.db")
        }
    }

    override suspend fun savePhotoToExternalStorage(
        displayName: String,
        bmp: Bitmap
    ): Boolean {
        return withContext(Dispatchers.IO){
            val imageCollection = sdk29AndUp {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val contentValues = ContentValues().apply{
                put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.WIDTH, bmp.width)
                put(MediaStore.Images.Media.HEIGHT, bmp.height)
            }

            try{
                context.contentResolver.insert(imageCollection, contentValues)?.also{ uri ->
                    context.contentResolver.openOutputStream(uri)?.use{ outputStream ->
                        if(!bmp.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)){
                            throw IOException("Couldn't save photo.")
                        }
                    }
                }?: throw IOException("Couldn't access mediastore")
                true
            }catch(e: IOException){
                Log.d("Error saving photo", e.message.toString())
                false
            }
        }

    }

    override suspend fun getLandmarkById(id: Int): Landmark {
        return withContext(Dispatchers.IO){
            db.dao.getLandmarkById(id).first()
        }
    }
}