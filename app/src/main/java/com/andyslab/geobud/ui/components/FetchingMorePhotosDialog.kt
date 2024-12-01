package com.andyslab.geobud.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.andyslab.geobud.R

@Composable
fun FetchingMorePhotosDialog(
    modifier: Modifier = Modifier,
    progress: Float,
    onDismiss: () -> Unit,
){
    val loadAnim by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing
        ),
        label = "loading bar anim"
    )

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
        )
    ) {
        Card(
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(5.dp),
        ) {Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Text(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                text = "Just a second!",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.bubblegum_sans)),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = { loadAnim },
                modifier = Modifier
                    .height(12.dp)
                    .border(2.dp, MaterialTheme.colorScheme.outline, RectangleShape),
                color = MaterialTheme.colorScheme.outlineVariant,
                trackColor = MaterialTheme.colorScheme.outline,
                drawStopIndicator = {}
            )

            Text(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                text = "Fetching more photos ...",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.bubblegum_sans)),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
        }
    }
}