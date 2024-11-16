package com.andyslab.geobud.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andyslab.geobud.data.model.Player
import com.andyslab.geobud.data.repository.landmark.LandmarkRepository
import com.andyslab.geobud.data.repository.player.PlayerRepository
import com.andyslab.geobud.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val landmarkRepo: LandmarkRepository,
    private val playerRepo: PlayerRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState = _uiState.asStateFlow()

    private var job = Job()
        get(){
            if(field.isCancelled) field = Job()
            return field
        }

    private lateinit var player: Player

    init{ getPhoto() }

    fun getPhoto() {
        job.cancel()
        viewModelScope.launch(job){
            val url = playerRepo.loadPlayerData().first().data!!
                .also{ p ->
                    player = p
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
                    delay(100)
                    landmarkRepo.addProgress(player)
                }
                result = true
            }else{
                player.hearts--
                _uiState.update {
                    it.copy(player = player.copy(), answerCorrect = false)
                }
            }
        }else{
            _uiState.update {
                it.copy(outOfHearts = true)
            }
        }
        return result
    }

    fun onPhotoLoadFailed(){
        _uiState.update {
            it.copy(
                error = ErrorState("Couldn't load photo. Check your internet connection and try again."),
                photoLoading = false,
                fetchingMorePhotos = null,
            )
        }
    }

    fun generateExclamation(): String{
        val exclams = setOf(
            "Nice!", "Great job!", "You're on fire!", "Amazing!", "Great!", "Cool!", "Geo-master!"
        )
        return exclams.random()
    }
}

data class QuizUiState(
    // mutually exclusive
    val player: Player? = null,
    val outOfHearts: Boolean = false,
    val answerCorrect: Boolean? = null,
    //concurrent
    val photoLoading: Boolean = true,
    val error: ErrorState? = null,
    val fetchingMorePhotos: FetchingState? = null,
    )

data class ErrorState(val message: String)
data class FetchingState(val progress: Float)
