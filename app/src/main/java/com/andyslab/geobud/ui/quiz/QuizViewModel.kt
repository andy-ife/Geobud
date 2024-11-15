package com.andyslab.geobud.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andyslab.geobud.data.model.Player
import com.andyslab.geobud.data.repository.landmark.LandmarkRepository
import com.andyslab.geobud.data.repository.player.PlayerRepository
import com.andyslab.geobud.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init{ getPhoto() }

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var player: Player
    private var lastExclamation = ""

    fun getPhoto() {
        viewModelScope.launch{
            val photo = playerRepo.loadPlayerData().first().data!!
                .also{ p ->
                    player = p
                    _uiState.update { it.copy(player = p) }
                }.currentLandmark!!

            val url = photo.photoUrl
            if(url == null){
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
                        is Resource.Success -> { retry() }
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
                viewModelScope.launch { landmarkRepo.addProgress(player) }
                _uiState.update {
                    it.copy(answerCorrect = true)
                }
                result = true
            }else{
                player.hearts--
                _uiState.update {
                    it.copy(player = player, answerCorrect = false)
                }
            }
        }else{
            _uiState.update {
                it.copy(outOfHearts = true)
            }
        }
        return result
    }

    fun retry(){
        _uiState.update { QuizUiState() }
        getPhoto()
    }

    fun onPhotoLoadFailed(){
        _uiState.update {
            it.copy(error = ErrorState("Couldn't load photo. Check your internet connection and try again."))
        }
    }

    fun generateExclamation(): String{
        val exclams = setOf(
            "Nice!", "Great job!", "You're on fire!", "Amazing!", "Great!", "Cool!", "Geo-master!"
        )
        var curr = ""

        while(lastExclamation == curr){
            curr = exclams.random()
        }
        lastExclamation = curr
        return curr
    }
}

data class QuizUiState(
    val player: Player? = null,
    val answerCorrect: Boolean? = null,
    val photoLoading: Boolean = true,
    val error: ErrorState? = null,
    val fetchingMorePhotos: FetchingState? = null,
    val outOfHearts: Boolean = false,
)

data class ErrorState(val message: String)
data class FetchingState(val progress: Float)
