package com.andyslab.geobud.data.model

import com.andyslab.geobud.data.local.LandmarkPacks

data class PlayerDto(
    var position: Position<Int, Int> = Position(0, 0),
    var packs: List<Set<Landmark>> = LandmarkPacks.packs,
    var currentLandmark: Landmark = packs[position.x].elementAt(position.y),
    var currentLandmarkPhoto: String? = null,
    var currentOptions: MutableSet<String> = mutableSetOf(),
    var photoURLs: MutableSet<String?> = mutableSetOf(),

    var flagsProgress: Int = 0,
    var hearts: Int = 5,
    var coins: Int = 0,
    var stars: Int = 0,

    var firstLaunch: Boolean = true
    )

data class Position<A, B>(var x: A, var y: B)