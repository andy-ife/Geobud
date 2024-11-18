package com.andyslab.geobud.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andyslab.geobud.data.model.Player
import com.andyslab.geobud.data.repository.landmark.LandmarkRepository
import com.andyslab.geobud.data.repository.player.PlayerRepository
import com.andyslab.geobud.domain.StartTimerUseCase
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
class QuizViewModel @Inject constructor(
    private val landmarkRepo: LandmarkRepository,
    private val playerRepo: PlayerRepository,
    private val startTimerUseCase: StartTimerUseCase,
) : ViewModel(){

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState = _uiState.asStateFlow()

    private var job = Job()
        get(){
            if(field.isCancelled) field = Job()
            return field
        }

    private lateinit var player: Player

    init{
        getPhoto()
        viewModelScope.launch{
            StartTimerUseCase.millisLeft.asSharedFlow().collect{ millisLeft ->
                player.timeLeftTillNextHeart = millisLeft
                if (_uiState.value.answerCorrect != true){
                _uiState.update {
                    it.copy(timeTillNextHeart = millisLeft.timeMillisToString())
                }
                if(millisLeft <= 0 && player.hearts < 3){
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
        viewModelScope.launch(job){
            val url = playerRepo.loadPlayerData().first().data!!
                .also{ p ->
                    player = p
                    delay(200) //give bottom sheet time to hide
                    _uiState.update {
                        it.copy(
                            player = p,
                            error = null,
                            fetchingMorePhotos = null,
                            answerCorrect = null,
                            photoLoading = false
                        )
                    }
                }.currentLandmark!!.photoUrl

            if(url == null){ // limit should be 10
                landmarkRepo.fetchLandmarkPhotos(10).collect{ result ->
                    when(result){
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    error = ErrorState(result.message.toString()),
                                    fetchingMorePhotos = null,
                                    photoLoading = false
                                )
                            }
                        }
                        is Resource.Loading -> {
                            _uiState.update {
                                it.copy(
                                    error = null,
                                    fetchingMorePhotos = FetchingState(result.data!!),
                                    photoLoading = false
                                )
                            }
                        }
                        is Resource.Success -> {
                            player = playerRepo.loadPlayerData().first().data!!
                            _uiState.update{
                                it.copy(
                                    player = player,
                                    answerCorrect = null,
                                    photoLoading = false,
                                    error = null,
                                    fetchingMorePhotos = null
                                )
                            }
                        }
                    }
                }
            }else{
                _uiState.update{
                    it.copy(
                        player = player,
                        answerCorrect = null,
                        photoLoading = false,
                        error = null,
                        fetchingMorePhotos = null
                    )
                }
            }
        }
    }

    fun checkAnswer(answer: String): Boolean{
        var result = false
        if(player.hearts > 0){
            if(answer == player.currentLandmark?.country){
                _uiState.update {
                    it.copy(answerCorrect = true)
                }
                viewModelScope.launch {
                    delay(200) // give option buttons time to vanish
                    landmarkRepo.addProgress(player)
                }
                result = true
            }else{
                player.hearts--
                if(player.hearts == 2) startTimerUseCase(600000)
                _uiState.update {
                    it.copy(player = player.copy(), answerCorrect = false)
                }
                viewModelScope.launch {
                    playerRepo.savePlayerData(player)
                }
            }
        }
        return result
    }

    fun generateExclamation(): String{
        val exclams = setOf(
            "Nice!", "Great job!", "You're on fire!", "Amazing!", "Great!", "Cool!", "Geo-master!"
        )
        return exclams.random()
    }

    override fun onCleared() {
        super.onCleared()
        player = player.copy(lastSessionTimestamp = System.currentTimeMillis())
        GlobalScope.launch{
            playerRepo.savePlayerData(player)
        }
    }
}

data class QuizUiState(
    // mutually exclusive
    val player: Player? = null,
    //val outOfHearts: Boolean = false,
    val answerCorrect: Boolean? = null,
    //concurrent
    val photoLoading: Boolean = true,
    val error: ErrorState? = null,
    val fetchingMorePhotos: FetchingState? = null,
    val timeTillNextHeart: String? = "00:00",
    )

data class ErrorState(val message: String)
data class FetchingState(val progress: Float)
