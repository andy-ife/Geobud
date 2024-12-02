package com.andyslab.geobud.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    soundState: Boolean = true,
    onDismiss: () -> Unit,
    resetProgressClick: () -> Unit = {},
    checkForUpdatesClick: () -> Unit = {},
    viewSourceClick: () -> Unit = {},
    toggleSoundClick: () -> Unit = {},
    shouldForceDarkMode: Boolean? = null,
    toggleTheme: (Boolean?) -> Unit = {},
){
    val interactionSource = remember { MutableInteractionSource() }
    var switchState by remember{ mutableStateOf(soundState)}
    var radioBtnState by remember{ mutableStateOf(shouldForceDarkMode) }

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
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(5.dp)){

            Column(modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp)) {
                Text(
                    text = stringResource(R.string.settings),
                    fontSize = 16.sp,
                )
                
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.general),
                    fontSize = 12.sp,
                    color= MaterialTheme.colorScheme.tertiary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row (
                    modifier = Modifier.fillMaxWidth().clickableNoRipple(interactionSource){
                        toggleSoundClick()
                        switchState = !switchState
                    },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        painter = painterResource(R.drawable.volume_icon),
                        contentDescription = null,
                    )

                    Text(
                        text = stringResource(R.string.sound)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ){
                        Switch(
                            checked = switchState,
                            onCheckedChange = {
                                toggleSoundClick()
                                switchState = it
                                              },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = MaterialTheme.colorScheme.secondary
                            )
                            )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color.LightGray)
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
                        text = stringResource(R.string.reset_progress)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
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
                        text = stringResource(R.string.check_updates)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
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
                        text = stringResource(R.string.source_code)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = stringResource(R.string.theme),
                    fontSize = 12.sp,
                    color= MaterialTheme.colorScheme.tertiary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row (
                    modifier = Modifier.fillMaxWidth().clickableNoRipple(interactionSource){
                        toggleTheme(true)
                        radioBtnState = true
                    },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        painter = painterResource(R.drawable.dark_mode_icon),
                        contentDescription = null,
                    )

                    Text(
                        text = stringResource(R.string.dark)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ){
                        RadioButton(
                            selected = radioBtnState == true,
                            onClick = {
                                toggleTheme(true)
                                radioBtnState = true
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.secondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(color = Color.LightGray)
                Spacer(modifier = Modifier.height(10.dp))

                Row (
                    modifier = Modifier.fillMaxWidth().clickableNoRipple(interactionSource){
                        toggleTheme(false)
                        radioBtnState = false
                    },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        painter = painterResource(R.drawable.light_mode_icon),
                        contentDescription = null,
                    )

                    Text(
                        text = stringResource(R.string.light)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ){
                        RadioButton(
                            selected = radioBtnState == false,
                            onClick = {
                                toggleTheme(false)
                                radioBtnState = false
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.secondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(color = Color.LightGray)
                Spacer(modifier = Modifier.height(10.dp))

                Row (
                    modifier = Modifier.fillMaxWidth().clickableNoRipple(interactionSource){
                        toggleTheme(null)
                        radioBtnState = null
                    },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        painter = painterResource(R.drawable.system_default_icon),
                        contentDescription = null,
                    )

                    Text(
                        text = stringResource(R.string.use_sys_default)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ){
                        RadioButton(
                            selected = radioBtnState == null,
                            onClick = {
                                toggleTheme(null)
                                radioBtnState = null
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.secondary
                            )
                        )
                    }
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
