package com.andyslab.geobud.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.andyslab.geobud.R
import com.andyslab.geobud.utils.clickableNoRipple

@Composable
fun SettingsDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    resetProgressClick: () -> Unit = {},
    checkForUpdatesClick: () -> Unit = {},
    viewSourceClick: () -> Unit = {},
){
    val interactionSource = remember { MutableInteractionSource() }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
        )
    ) {
        Card (
            modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(5.dp)){

            Column(modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp)) {
                Text(
                    text = "Settings",
                    fontSize = 16.sp,
                )
                
                Spacer(modifier = Modifier.height(20.dp))

                Row (
                    modifier = Modifier.fillMaxWidth().clickableNoRipple(interactionSource){
                        resetProgressClick()
                    },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                    )

                    Text(
                        text = "Reset progress"
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color.LightGray)
                Spacer(modifier = Modifier.height(20.dp))

                Row (
                    modifier = Modifier.fillMaxWidth().clickableNoRipple(interactionSource){
                        checkForUpdatesClick()
                    },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        painter = painterResource(R.drawable.outline_file_download_24),
                        contentDescription = null,
                    )

                    Text(
                        text = "Check for updates"
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color.LightGray)

                Spacer(modifier = Modifier.height(20.dp))

                Row (
                    modifier = Modifier.fillMaxWidth().clickableNoRipple(interactionSource){
                        viewSourceClick()
                    },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        painter = painterResource(R.drawable.programming_svgrepo_com),
                        contentDescription = null,
                    )

                    Text(
                        text = "Source code"
                    )
                }
            }

        }
    }
}

@Preview
@Composable
fun SettingsDialogPrev(){
    SettingsDialog(onDismiss = {})
}