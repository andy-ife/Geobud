package com.andyslab.geobud.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ObserveThemeChangesUseCase {
    companion object {
        val themeState = MutableSharedFlow<Boolean?>()
    }

    operator fun invoke(newState: Boolean?) {
        CoroutineScope(Dispatchers.Default).launch {
            themeState.emit(newState)
        }
    }
}
