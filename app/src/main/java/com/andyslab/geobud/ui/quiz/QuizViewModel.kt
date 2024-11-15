package com.andyslab.geobud.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andyslab.geobud.data.model.Landmark
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

    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.PhotoLoading)
    val uiState = _uiState.asStateFlow()

    lateinit var player: Player

    private fun getPhoto() {
        viewModelScope.launch{
            val photo = playerRepo.loadPlayerData().first().data!!.also{player = it}.currentLandmark!!
            val url = photo.photoUrl

            if(url == null){
                _uiState.update { QuizUiState.FetchingMorePhotos(0f) }

                landmarkRepo.fetchLandmarkPhotos(10).collect{ result ->
                    when(result){
                        is Resource.Error -> {
                            _uiState.update { QuizUiState.Error(result.message.toString()) }
                            return@collect
                        }
                        is Resource.Loading -> {
                            _uiState.update { QuizUiState.FetchingMorePhotos(result.data!!) }
                        }
                        is Resource.Success -> { onRetry() }
                    }
                }
            }else{
                val photographer = photo.photographer ?: ""
                val photographerUrl = photo.photographerUrl ?: ""

                _uiState.update {
                    QuizUiState.PhotoSuccess(
                        url,
                        photographer,
                        photographerUrl,
                        player.currentOptions,
                        player.hearts,
                        photo.id
                        )
                }
            }
        }
    }

    fun checkAnswer(answer: String){
        if(player.hearts > 0){
            if(answer == player.currentLandmark?.country){
                viewModelScope.launch { landmarkRepo.addProgress(player) }
                _uiState.update { QuizUiState.AnswerCorrect(player.currentLandmark!!) }
            }else{
                player.hearts--
                _uiState.update { QuizUiState.AnswerWrong(answer, player.hearts) }
            }
        }else{
            _uiState.update {
                QuizUiState.OutOfHearts
            }
        }
    }

    fun onRetry(){
        _uiState.update { QuizUiState.PhotoLoading }
        getPhoto()
    }

    fun onPhotoLoadFailed(){
        _uiState.update {
            QuizUiState.Error("Something went wrong. Check your internet connection and try again.")
        }
    }
}

sealed interface QuizUiState {
    object PhotoLoading: QuizUiState
    data class PhotoSuccess(
        val url: String,
        val photographer: String,
        val photographerUrl: String,
        val options: Set<String>,
        val hearts: Int,
        val id: Int,
    ): QuizUiState
    data class FetchingMorePhotos(val progress: Float): QuizUiState
    data class AnswerCorrect(val landmark: Landmark): QuizUiState
    data class AnswerWrong(val answer: String, val hearts:Int): QuizUiState
    data class Error(val message: String): QuizUiState
    object OutOfHearts: QuizUiState
}