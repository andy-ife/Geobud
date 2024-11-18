package com.andyslab.geobud.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andyslab.geobud.data.model.Player
import com.andyslab.geobud.data.repository.landmark.LandmarkRepository
import com.andyslab.geobud.data.repository.player.PlayerRepository
import com.andyslab.geobud.domain.StartTimerLoopUseCase
import com.andyslab.geobud.utils.Resource
import com.andyslab.geobud.utils.timeMillisToString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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

    init {
        load()
        viewModelScope.launch{
            StartTimerLoopUseCase.millisLeft.asSharedFlow().collect{ millisLeft ->
                player.timeLeftTillNextHeart = millisLeft
                _uiState.update {
                    MenuUiState.Success(player, millisLeft.timeMillisToString())
                }
                if(millisLeft <= 0L && player.hearts < 3){
                    player.hearts++
                    _uiState.update {
                        MenuUiState.Success(player)
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
                          val player = playerRepo.loadPlayerData().first{ it is Resource.Success }.data!!.also{ player = it }
                          _uiState.update {
                              MenuUiState.Success(player)
                          }
                      }
                  }
            }
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
    data class Success(val data: Player, val timeTillNextHeart: String = "00:00"): MenuUiState
}