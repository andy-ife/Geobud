package com.andyslab.geobud.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
fun OutOfHeartsDialog(
    modifier: Modifier = Modifier,
    timer: String,
    onDismiss: () -> Unit,
){
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card (
            modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(5.dp),){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            ) {
                Image(
                painter = painterResource(R.drawable.heart),
                contentDescription = "out of hearts",
                modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Oh dear, you seem to be out of hearts :(",
                    textAlign = TextAlign.Center,
                    fontSize=18.sp,
                    fontFamily = FontFamily(Font(R.font.bubblegum_sans))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "More in:",
                    fontFamily = FontFamily(Font(R.font.bubblegum_sans)),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = timer,
                    fontFamily = FontFamily(Font(R.font.bubblegum_sans)),
                    fontSize = 28.sp
                    )

            }
        }
    }
}

@Composable
@Preview
fun OutOfHeartsDialogPrev(){
    OutOfHeartsDialog(timer="00 : 30") { }
}