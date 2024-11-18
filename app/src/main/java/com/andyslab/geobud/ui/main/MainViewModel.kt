package com.andyslab.geobud.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andyslab.geobud.data.model.Player
import com.andyslab.geobud.data.repository.player.PlayerRepository
import com.andyslab.geobud.domain.StartTimerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val playerRepo: PlayerRepository,
    private val startTimerUseCase: StartTimerUseCase
): ViewModel() {

    private var timerJob = Job()
        get() {
            if(field.isCancelled) field = Job()
            return field
        }
    lateinit var player: Player

    fun startTimer(){
        timerJob.cancel()
        viewModelScope.launch(timerJob){
            val player = playerRepo.loadPlayerData().first().data!!.also{ player = it }

            if(player.hearts < 3){
                val lastSessionTimestamp = player.lastSessionTimestamp
                if(lastSessionTimestamp == 0L) return@launch
                val currentTimestamp = System.currentTimeMillis()
                val timeSinceLastSession = currentTimestamp - lastSessionTimestamp

                if(timeSinceLastSession in 600000..1199999){
                    player.hearts+=1
                    playerRepo.savePlayerData(player)
                }
                else if(timeSinceLastSession in 1200000..1799999){
                    player.run{
                        hearts+=2
                        if(hearts > 3) hearts = 3
                        playerRepo.savePlayerData(this)
                    }
                }
                else if(timeSinceLastSession >= 1800000){
                    player.hearts = 3
                    playerRepo.savePlayerData(player)
                }
                val timeLeft = player.timeLeftTillNextHeart
                val newTimeLeft = (timeLeft - timeSinceLastSession) % 600000
                startTimerUseCase(newTimeLeft)
            }
        }
    }

    fun stopTimer(){
        timerJob.cancel()
    }

    fun savePlayerData(){
        GlobalScope.launch {
            playerRepo.savePlayerData(player)
        }
    }

    override fun onCleared() {
        timerJob.cancel()
    }
}