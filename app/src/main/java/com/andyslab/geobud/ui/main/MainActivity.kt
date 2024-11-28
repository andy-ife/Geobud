package com.andyslab.geobud.ui.main

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.andyslab.geobud.ui.nav.RootNavGraph
import com.andyslab.geobud.work.FetchPhotosWorker
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val workConstraints = Constraints
            .Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val worker = OneTimeWorkRequestBuilder<FetchPhotosWorker>()
            .setInitialDelay(Duration.ofSeconds(10))
            .setConstraints(workConstraints)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                duration = Duration.ofSeconds(10)
            )
            .build()
        WorkManager.getInstance(applicationContext).enqueue(worker)

        enableEdgeToEdge()
        viewModel.startTimer()
        setContent {
            RootNavGraph()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopTimer()
    }
}

