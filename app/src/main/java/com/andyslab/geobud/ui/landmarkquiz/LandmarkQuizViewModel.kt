package com.andyslab.geobud.ui.landmarkquiz

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andyslab.geobud.data.local.Player
import com.andyslab.geobud.data.repository.LandmarksRepoImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LandmarkQuizViewModel(context: Context): ViewModel() {

    private val _imageState = MutableStateFlow<ImageLoadEvent>(ImageLoadEvent.ImageLoaded(null))
    val imageState = _imageState.asStateFlow()

    private val _correctAnswerState = MutableStateFlow(false)
    val correctAnswerState = _correctAnswerState.asStateFlow()

    private val _hearts = MutableStateFlow(Player.instance.hearts)
    val hearts = _hearts.asStateFlow()

    private val _currLandmark = MutableStateFlow(Player.instance.currentLandmark)
    val currLandmark = _currLandmark.asStateFlow()

    private val landmarksRepo = LandmarksRepoImpl(context)

    companion object{
        var lastExclamation: String? = null
    }

    fun loadImage() {
        val imageUrl = requireNotNull(Player.instance.currentLandmarkPhoto)
        _imageState.value = ImageLoadEvent.LoadImage(imageUrl)
        var drawable: Drawable?

        viewModelScope.launch{
            //delay(500)
            drawable = landmarksRepo.loadPhoto(Player.instance.currentLandmarkPhoto)
            _imageState.value = ImageLoadEvent.ImageLoaded(drawable)
            //landmarksRepo.updatePlayerData(Player.instance)
        }
    }

    fun updatePlayerData(){
        landmarksRepo.updatePlayerData(Player.instance)
        _currLandmark.value = Player.instance.currentLandmark
    }

    fun getPhotoURL(): String?{
        return Player.instance.currentLandmarkPhoto
    }

    fun isAnswerCorrect(answer: String): Boolean{
        val result: Boolean
        if(answer == Player.instance.currentLandmark.country){
                _correctAnswerState.value = true
                result = true
            }
            else{
                _correctAnswerState.value = false
                result = false
            }
        return result
    }

    fun generateOptions(): MutableSet<String>{
        return landmarksRepo.generateOptions(Player.instance)
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

    sealed class ImageLoadEvent{
        data class LoadImage(val imageUrl: String): ImageLoadEvent()
        data class ImageLoaded(val drawable: Drawable?): ImageLoadEvent()
    }
}