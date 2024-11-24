package com.andyslab.geobud.utils

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


/*----------------------Compose Extensions-----------------------------*/
fun Modifier.shimmerLoadingEffect(): Modifier = composed{
    var size by remember{
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition(label = "shimmer transition")
    val startOffsetX by transition.animateFloat(
        initialValue = -5 * size.width.toFloat(),
        targetValue = 5 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = "shimmer"
    )

    background(
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF404040),
                Color(0xFF252525),
                Color(0xFF404040)
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        ),
    ).onGloballyPositioned {
        size = it.size
    }
}

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

fun Modifier.clickableNoRipple(
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit
) = run {
    this.then(
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = {onClick()}
        )
    )
}

/*----------------------Activity extensions-------------------------*/
fun ComponentActivity.hideSystemUI(){
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat
        .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
}

fun ComponentActivity.showSystemUI(){
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
    windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
}

fun Context.findActivity(): ComponentActivity {
    var context = this
    while(context is ContextWrapper){
        if(context is ComponentActivity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}

fun Context.hasWriteStoragePermissions(): Boolean{
    return if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
        this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    else
        true
}

fun Context.openAppSettings(){
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}


