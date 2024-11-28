package com.andyslab.geobud.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.andyslab.geobud.data.repository.landmark.LandmarkRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class FetchPhotosWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    @Inject
    lateinit var repo: LandmarkRepository

    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }
}