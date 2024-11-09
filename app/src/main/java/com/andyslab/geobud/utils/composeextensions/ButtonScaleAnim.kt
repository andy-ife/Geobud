package com.andyslab.geobud.utils.composeextensions

import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInteropFilter

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Modifier.onClickWithScaleAnim(scaleDown: Float, onClick: () -> Unit = {}) : Modifier{
    val selected = remember {
        mutableStateOf(false)
    }

    val scale = animateFloatAsState(targetValue = if (selected.value) scaleDown else 1f,
        label = "scale anim",
        animationSpec = tween(
            delayMillis = 0,
            durationMillis = 50,
        )
    )

    return(
            this
                .scale(scale.value)
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            selected.value = true
                        }

                        MotionEvent.ACTION_UP -> {
                            selected.value = false
                            onClick()
                        }

                        MotionEvent.ACTION_CANCEL -> {
                            selected.value = false
                        }
                    }
                    true
                }
            )

}