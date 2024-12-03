package com.andyslab.geobud.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.andyslab.geobud.data.repository.landmark.LandmarkRepository
import com.andyslab.geobud.utils.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class FetchPhotosWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repo: LandmarkRepository
): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try{
            val limit = repo.getMaxId()
            var numberOfPhotosFetched = 0
            var success = true
            repo.fetchLandmarkPhotos(limit).collect{
                when(it){
                    is Resource.Success -> {
                        Log.d("Success!", "All photos fetched successfully")
                        success = true
                        return@collect
                    }
                    is Resource.Error -> {
                        Log.d("Retrying...", it.message.toString())
                        success = false
                        return@collect
                    }
                    is Resource.Loading -> {
                        Log.d("Loading...", "Photos fetched: $numberOfPhotosFetched")
                        numberOfPhotosFetched++
                    }
                }
            }
            if (success) Result.success() else Result.retry() // retry for errors from the repository function
        }catch(e: Exception){
            Log.d("Worker error", e.message.toString())
            Result.failure() // failure for some other error
        }
    }
}