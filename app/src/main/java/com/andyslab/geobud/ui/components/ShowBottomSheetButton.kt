package com.andyslab.geobud.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andyslab.geobud.R

@Composable
fun ShowBottomSheetButton(modifier: Modifier, onClick: () -> Unit){
    val transition = rememberInfiniteTransition(label = "button color")
    val color by transition.animateColor(
        initialValue = Color(0xFF53A0EA),
        targetValue = Color(0xFF1976d2),
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = "button color"
    )

    TextButton(onClick = { onClick() },
        modifier = modifier.fillMaxWidth(),
        shape = CircleShape,
        colors = ButtonDefaults.textButtonColors(
            containerColor = color,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(vertical = 14.dp)
    ) {
        Text(
            text = "Continue",
            fontFamily = FontFamily(Font(R.font.bubblegum_sans)),
            fontSize = 16.sp
        )
    }
}