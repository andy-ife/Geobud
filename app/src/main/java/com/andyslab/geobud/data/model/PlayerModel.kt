package com.andyslab.geobud.data.model

import com.andyslab.geobud.data.app.LandmarkPacks.Companion.LANDMARKS

data class PlayerModel(
    var progress: Int = 0,
    var currentLandmark: Landmark = LANDMARKS[0],
    var currentLandmarkPhoto: String? = null,
    var currentOptions: MutableSet<String> = mutableSetOf(),
    var photoURLs: MutableSet<String?> = mutableSetOf(),
    var hearts: Int = 5,
    var firstLaunch: Boolean = true
    )