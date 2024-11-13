package com.andyslab.geobud.data.model

data class PlayerModel(
    var progress: Int = 0,
    var currentLandmark: LandmarkModel? = null,
    var currentOptions: MutableSet<String> = mutableSetOf(),
    var hearts: Int = 5,
    var isFirstLaunch: Boolean = true
)