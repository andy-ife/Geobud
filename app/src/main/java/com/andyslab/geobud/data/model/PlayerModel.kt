package com.andyslab.geobud.data.model

data class PlayerModel(
    val progress: Int = 0,
    val currentLandmark: LandmarkModel? = null,
    val currentOptions: Set<String> = mutableSetOf(),
    val hearts: Int = 3,
    val isFirstLaunch: Boolean = true
)