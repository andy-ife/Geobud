package com.andyslab.geobud.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andyslab.geobud.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QuizOptionButton(
    modifier: Modifier = Modifier,
    text: String,
    checkAnswer: (String) -> Boolean,
) {
    val buttonColorScope = rememberCoroutineScope()
    val col = MaterialTheme.colorScheme.inversePrimary

    var buttonColor by remember {
        mutableStateOf(col)
    }

    val buttonColorAnim by animateColorAsState(
        targetValue = buttonColor,
        label = "button color anim",
    )

    TextButton(
        onClick = {
            if (checkAnswer(text)) {
                buttonColorScope.launch {
                    buttonColor = Color(0x6566BB6A)
                    delay(2000)
                    buttonColor = col
                }
            } else {
                buttonColorScope.launch {
                    buttonColor = Color(0x65F44336)
                    delay(1000)
                    buttonColor = col
                }
            }
        },
        shape = RoundedCornerShape(8.dp),
        colors =
            ButtonDefaults.textButtonColors(
                containerColor = buttonColorAnim,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        // contentPadding = PaddingValues(2.dp),
        modifier = modifier.width(180.dp),
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.bubblegum_sans)),
            textAlign = TextAlign.Center,
        )
    }
}
