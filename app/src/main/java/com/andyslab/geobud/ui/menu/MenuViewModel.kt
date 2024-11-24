package com.andyslab.geobud.ui.menu

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val landmarkRepo: LandmarkRepository,
    private val playerRepo: PlayerRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow<MenuUiState>(MenuUiState.Loading(0.05f))
    val uiState = _uiState.asStateFlow()

    private var player = Player()
    private var maxId = 0

    init {
        load()
        viewModelScope.launch{
            StartTimerUseCase.millisLeft.asSharedFlow().collect{ millisLeft ->
                player.timeLeftTillNextHeart = millisLeft
                if(_uiState.value is MenuUiState.Success){
                    _uiState.update {
                        MenuUiState.Success(player, millisLeft.timeMillisToString(), maxId = maxId)
                    }
                    if(millisLeft <= 0 && player.hearts < 3){
                        player.hearts++
                        _uiState.update {
                            MenuUiState.Success(player, maxId = maxId)
                        }
                }
                }
            }
        }
    }

    fun load(){
        viewModelScope.launch{ //limit should be 10
            landmarkRepo.fetchLandmarkPhotos(10).collect{ result ->
                  when(result){
                      is Resource.Error -> {
                          _uiState.update {
                              MenuUiState.Error(result.message.toString())
                          }
                          return@collect
                      }
                      is Resource.Loading -> {
                          _uiState.update {
                              MenuUiState.Loading(result.data!!)
                          }
                      }
                      is Resource.Success -> {
                          player = playerRepo.loadPlayerData().first{ it is Resource.Success }.data!!.also{ player = it }
                          maxId = landmarkRepo.getMaxId()
                          _uiState.update {
                              MenuUiState.Success(data = player, maxId = maxId)
                          }
                      }
                  }
            }
        }
    }

    fun resetProgress(){
        viewModelScope.launch{
            landmarkRepo.resetProgress()
            player = Player()
            load()
        }
    }

    fun toggleSound(){
        GlobalScope.launch{
            player.isSoundEnabled = !player.isSoundEnabled
            playerRepo.savePlayerData(player)
        }
    }

    override fun onCleared() {
        player = player.copy(lastSessionTimestamp = System.currentTimeMillis())
        GlobalScope.launch{
            playerRepo.savePlayerData(player)
        }
    }
}

sealed interface MenuUiState{
    data class Loading(val progress: Float): MenuUiState
    data class Error(val message: String): MenuUiState
    data class Success(val data: Player, val timeTillNextHeart: String = "00:00", val maxId: Int = 0): MenuUiState
}