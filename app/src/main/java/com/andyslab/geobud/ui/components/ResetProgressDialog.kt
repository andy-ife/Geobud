package com.andyslab.geobud.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.andyslab.geobud.R

@Composable
fun ResetProgressDialog(
    modifier: Modifier = Modifier,
    message: String,
    onDismiss: () -> Unit,
    onOKClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties =
            DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
            ),
    ) {
        Card(
            modifier =
                modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
            shape = RoundedCornerShape(4.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            elevation = CardDefaults.cardElevation(5.dp),
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = message,
                fontSize = 17.sp,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
            )

            Row(
                modifier =
                    Modifier.padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(
                    onClick = { onDismiss() },
                    colors =
                        ButtonDefaults.textButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.error,
                        ),
                    // elevation = ButtonDefaults.buttonElevation(2.dp),
                ) {
                    Text(
                        text = stringResource(R.string.no),
                        fontSize = 16.sp,
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                TextButton(
                    onClick = { onOKClick() },
                    colors =
                        ButtonDefaults.textButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    // elevation = ButtonDefaults.buttonElevation(2.dp),
                ) {
                    Text(
                        text = stringResource(R.string.yes),
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}
