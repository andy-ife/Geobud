package com.andyslab.geobud.data.model

import androidx.annotation.Keep

@Keep
data class Player(
    var progress: Int = 0,
    var currentLandmark: Landmark? = null,
    var currentOptions: Set<String> = mutableSetOf(),
    var hearts: Int = 3,
    var isFirstLaunch: Boolean = true,
    var lastSessionTimestamp: Long = 0L,
    var timeLeftTillNextHeart: Long = 0L,
    var isSoundEnabled: Boolean = true,
    var forceDarkMode: Boolean? = null,
)
