package com.andyslab.geobud.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShowBottomSheetButton(modifier: Modifier, onClick: () -> Unit){
    val transition = rememberInfiniteTransition(label = "button color")
    val color by transition.animateColor(
        initialValue = Color(0xFFCDDC39),
        targetValue = Color(0xFFFFB300),
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = "button color"
    )

    FilledIconButton(onClick = { onClick()},
        modifier = modifier
            .size(48.dp)
            .border(2.dp, Color.White, CircleShape,),
        shape = CircleShape,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = color
        )) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = "award",
            modifier = Modifier
                .size(25.dp)
                .offset(1.dp))
    }
}