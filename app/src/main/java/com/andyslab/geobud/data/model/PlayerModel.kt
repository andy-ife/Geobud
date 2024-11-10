package com.andyslab.geobud.data.model

import com.andyslab.geobud.data.app.LandmarksPack.Companion.LANDMARKS_PACK

data class PlayerModel(
    var progress: Int = 0,
    var currentLandmark: LandmarkModel = LANDMARKS_PACK[0],
    var currentOptions: MutableSet<String> = mutableSetOf(),
    var hearts: Int = 5,
    var isFirstLaunch: Boolean = true
)