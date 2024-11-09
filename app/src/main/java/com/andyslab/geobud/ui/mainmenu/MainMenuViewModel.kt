package com.andyslab.geobud.ui.mainmenu

import androidx.lifecycle.ViewModel
import com.andyslab.geobud.data.local.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainMenuViewModel: ViewModel() {

    private val _hasMissions = MutableStateFlow(false)
    val hasMissions = _hasMissions.asStateFlow()

    private val _hasNewPhoto = MutableStateFlow(false)
    val hasNewPhoto = _hasNewPhoto.asStateFlow()

    private val _stars = MutableStateFlow(Player.instance.stars)
    val stars = _stars.asStateFlow()

    private val _coins = MutableStateFlow(Player.instance.coins)
    val coins = _coins.asStateFlow()

    private val _hearts = MutableStateFlow(Player.instance.hearts)
    val hearts = _hearts.asStateFlow()




}