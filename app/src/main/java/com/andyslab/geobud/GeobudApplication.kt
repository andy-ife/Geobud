package com.andyslab.geobud

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.andyslab.geobud.data.repository.landmark.LandmarkRepository
import com.andyslab.geobud.work.FetchPhotosWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltAndroidApp
class GeobudApplication: Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: FetchPhotosWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerCoroutineContext(Dispatchers.IO)
            .setWorkerFactory(workerFactory)
            .build()
}

class FetchPhotosWorkerFactory @Inject constructor(private val repo: LandmarkRepository): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = FetchPhotosWorker(appContext, workerParameters, repo)
}