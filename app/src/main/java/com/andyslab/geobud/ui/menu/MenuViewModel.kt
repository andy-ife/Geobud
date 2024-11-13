package com.andyslab.geobud.ui.menu
//
//import android.content.Context
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.andyslab.geobud.data.local.Player
//import com.andyslab.geobud.data.repository.landmarks.LandmarksRepoImpl
//import com.andyslab.geobud.utils.Resource
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//
//class MenuViewModel(): ViewModel() {
//
////    private val _loadProgress = MutableStateFlow(0f)
////    val loadProgress = _loadProgress.asStateFlow()
////
////    private val _hasPhotoURLs = MutableStateFlow(false)
////    val hasPhotoURLs = _hasPhotoURLs.asStateFlow()
////
////    private val _hasDownloadedPhotos = MutableStateFlow(false)
////    val hasDownloadedPhotos = _hasDownloadedPhotos.asStateFlow()
////
////    private val _showErrorDialog = MutableStateFlow("")
////    val showErrorDialog = _showErrorDialog.asStateFlow()
////
////    private val landmarksRepo = LandmarksRepoImpl(context)
////    private val player = Player(context)
////
////
////    fun doLoading(){
////        _loadProgress.value = 0f
////        viewModelScope.launch {
////
////            player.loadPlayerData()
////
////            if(!_hasPhotoURLs.value){
////            landmarksRepo.getLandmarkPhotoURLs(Player.instance).collect { resource ->
////                when(resource){
////                    is Resource.Loading -> _loadProgress.value+=0.04f
////                    is Resource.Error -> {
////                        _showErrorDialog.value = (resource.message.toString())
////                        cancel()
////                    }
////                    is Resource.Success -> {
////                        //_loadProgress.value+=0.04f
////                        _hasPhotoURLs.value = true
////                        _hasDownloadedPhotos.value = true
////                        _loadProgress.value = 1f
////                    }
////                }
////            }
////            }
////
////            /*if(!_hasDownloadedPhotos.value){
////            landmarksRepo.downloadAndCachePhotos(Player.instance).collect{ resource ->
////                when(resource){
////                    is Resource.Loading -> _loadProgress.value+=0.04f //crashes after this line
////                    is Resource.Error -> {
////                        _showErrorDialog.value =(resource.message.toString())
////                        cancel()
////                    }
////                    is Resource.Success -> {
////                        _hasDownloadedPhotos.value = true
////                        _loadProgress.value = 1f
////                    }
////                }
////            }
////        }*/
////
////        }
////    }
////
////
////    fun retryLoading(){
////        //Changing showError.value will cause ui recomposition and consequently retry of loading
////        _showErrorDialog.value = ""
////        _loadProgress.value = 0f
////    }
//
//}