package com.andyslab.geobud.ui.quiz

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andyslab.geobud.data.model.Player
import com.andyslab.geobud.data.repository.landmark.LandmarkRepository
import com.andyslab.geobud.data.repository.player.PlayerRepository
import com.andyslab.geobud.domain.StartHeartTimerUseCase
import com.andyslab.geobud.utils.Resource
import com.andyslab.geobud.utils.timeMillisToString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel
    @Inject
    constructor(
        private val landmarkRepo: LandmarkRepository,
        private val playerRepo: PlayerRepository,
        private val startHeartTimerUseCase: StartHeartTimerUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(QuizUiState())
        val uiState = _uiState.asStateFlow()

        private var job = Job()
            get() {
                if (field.isCancelled) field = Job()
                return field
            }

        private lateinit var player: Player
        private lateinit var lastPlayer: Player

        private var combo = 0

        init {
            getPhoto()
            viewModelScope.launch {
                StartHeartTimerUseCase.millisLeft.asSharedFlow().collect { millisLeft ->
                    player.timeLeftTillNextHeart = millisLeft
                    if (_uiState.value.answerCorrect != true && _uiState.value.savingPhoto == null) {
                        _uiState.update {
                            it.copy(timeTillNextHeart = millisLeft.timeMillisToString())
                        }
                        if (millisLeft <= 0 && player.hearts < 3) {
                            player.hearts++
                            _uiState.update {
                                it.copy(player = player)
                            }
                        }
                    }
                }
            }
        }

        fun getPhoto() {
            job.cancel()
            viewModelScope.launch(job) {
                val url =
                    playerRepo.loadPlayerData().first().data!!
                        .also { p ->
                            player = p
                            delay(200) // give bottom sheet time to hide
                            _uiState.update {
                                it.copy(
                                    player = p,
                                    error = null,
                                    fetchingMorePhotos = null,
                                    answerCorrect = null,
                                    photoLoading = false,
                                )
                            }
                        }.currentLandmark!!.photoUrl

                if (url == null) { // limit should be 10
                    landmarkRepo.fetchLandmarkPhotos(10).collect { result ->
                        when (result) {
                            is Resource.Error -> {
                                _uiState.update {
                                    it.copy(
                                        error = ErrorState(result.message.toString()),
                                        fetchingMorePhotos = null,
                                        photoLoading = false,
                                    )
                                }
                            }
                            is Resource.Loading -> {
                                _uiState.update {
                                    it.copy(
                                        error = null,
                                        fetchingMorePhotos = FetchingState(result.data!!),
                                        photoLoading = false,
                                    )
                                }
                            }
                            is Resource.Success -> {
                                player = playerRepo.loadPlayerData().first().data!!
                                lastPlayer = player.copy()
                                _uiState.update {
                                    it.copy(
                                        player = player,
                                        answerCorrect = null,
                                        photoLoading = false,
                                        error = null,
                                        fetchingMorePhotos = null,
                                    )
                                }
                            }
                        }
                    }
                } else {
                    lastPlayer = player.copy()
                    _uiState.update {
                        it.copy(
                            player = player,
                            answerCorrect = null,
                            photoLoading = false,
                            error = null,
                            fetchingMorePhotos = null,
                        )
                    }
                }
            }
        }

        fun checkAnswer(answer: String): Boolean {
            var result = false
            if (player.hearts > 0) {
                if (answer == player.currentLandmark?.country) {
                    _uiState.update {
                        it.copy(answerCorrect = true)
                    }
                    viewModelScope.launch {
                        delay(200) // give option buttons time to vanish
                        landmarkRepo.addProgress(player)
                    }
                    combo++
                    result = true
                } else {
                    player.hearts--
                    if (player.hearts == 2) startHeartTimerUseCase(600000)
                    _uiState.update {
                        it.copy(player = player.copy(), answerCorrect = false)
                    }
                    viewModelScope.launch {
                        playerRepo.savePlayerData(player)
                        delay(1000)
                        _uiState.update {
                            it.copy(answerCorrect = null)
                        }
                    }
                    combo = 0
                }
            }
            return result
        }

        fun generateExclamation(): String {
            return if (combo > 2) {
                setOf(
                    "Nice!",
                    "Great job!",
                    "You're on fire!",
                    "Amazing!",
                    "Great!",
                    "Cool!",
                    "Geo-master!",
                    "Globetrotter!",
                ).random()
            } else {
                setOf(
                    "Nice!",
                    "Great job!",
                    "Great!",
                    "Cool!",
                ).random()
            }
        }

        fun savePhoto(
            displayName: String,
            bmp: Bitmap,
        ) {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        // player might have already added progress,
                        // so I use a saved copy of player whose progress doesn't change.
                        player = lastPlayer,
                        savingPhoto = "Saving photo",
                    )
                }
                val result = landmarkRepo.savePhotoToExternalStorage(displayName, bmp)
                if (result) {
                    _uiState.update {
                        it.copy(savingPhoto = "Photo saved")
                    }
                } else {
                    _uiState.update {
                        it.copy(savingPhoto = "Could not save photo")
                    }
                }
                delay(2000) // give snackbars time to show
                _uiState.update {
                    it.copy(savingPhoto = null)
                }
            }
        }

        fun toggleSound() {
            viewModelScope.launch {
                player.isSoundEnabled = !player.isSoundEnabled
                _uiState.update {
                    it.copy(player = player)
                }
                playerRepo.savePlayerData(player)
            }
        }

        override fun onCleared() {
            super.onCleared()
            player = player.copy(lastSessionTimestamp = System.currentTimeMillis())
            GlobalScope.launch {
                playerRepo.savePlayerData(player)
            }
        }
    }

data class QuizUiState(
    // mutually exclusive
    val player: Player? = null,
    val answerCorrect: Boolean? = null,
    val timeTillNextHeart: String? = "00:00",
    val savingPhoto: String? = null,
    // concurrent
    val photoLoading: Boolean = true,
    val error: ErrorState? = null,
    val fetchingMorePhotos: FetchingState? = null,
)

data class ErrorState(val message: String)

data class FetchingState(val progress: Float)
