package com.andyslab.geobud.ui.menu


import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.andyslab.geobud.R
import com.andyslab.geobud.ui.components.ErrorDialog
import com.andyslab.geobud.ui.components.ResetProgressDialog
import com.andyslab.geobud.ui.components.SettingsDialog
import com.andyslab.geobud.ui.components.TopBarItem
import com.andyslab.geobud.ui.nav.Screen
import com.andyslab.geobud.utils.findActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//Stateful
@Composable
fun MenuScreen(
    navController: NavHostController,
    viewModel: MenuViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val activity = context.findActivity()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MenuScreen(navController, activity, uiState, viewModel::toggleSound, viewModel::resetProgress, viewModel::load)
}

//Stateless
@Composable
fun MenuScreen(
    navController: NavHostController,
    activity: ComponentActivity,
    uiState: MenuUiState?,
    toggleSound: () -> Unit,
    resetProgress: () -> Unit,
    load: () -> Unit,
    ) {

    val progress = if(uiState is MenuUiState.Loading) uiState.progress else 0f

    val loadAnim by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing
        ),
        label = "loading bar anim"
    )

    val scope = rememberCoroutineScope()
    var showTimerPopup by remember{mutableStateOf(false)}
    var showSettings by remember{mutableStateOf(false)}
    var showResetProgressDialog by remember{mutableStateOf(false)}


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = if (!isSystemInDarkTheme()) painterResource(id = R.drawable.background_light) else painterResource(id = R.drawable.background_dark),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Image(
            painter = painterResource(id = R.drawable.geobud_logo),
            contentDescription = stringResource(R.string.app_logo_desc),
            modifier = Modifier
                .size(200.dp)
                .offset(y = -80.dp)
        )

        if(uiState is MenuUiState.Success && showTimerPopup){
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 100.dp),) {
                Box(modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)) {
                    Text(
                        text = "${stringResource(R.string.more_in)} ${uiState.timeTillNextHeart}",
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontFamily = FontFamily(Font(R.font.bubblegum_sans))
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = uiState is MenuUiState.Success,
            enter = slideInVertically(),
            exit = slideOutVertically(),
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    IconButton(
                        onClick = {showSettings = true},
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Image(
                            painter = painterResource(R.drawable.settings_icon),
                            contentDescription = stringResource(R.string.settings)
                        )
                    }

                    val text = if(uiState is MenuUiState.Success)uiState.data.hearts.toString() else ""
                    TopBarItem(
                        modifier = Modifier.clickable{
                            if(uiState is MenuUiState.Success && uiState.data.hearts < 3){
                                scope.launch{
                                    showTimerPopup = true
                                    delay(5000)
                                    showTimerPopup = false
                                }
                            }
                        },
                        icon = R.drawable.heart,
                        text = text
                    )
                }
            }

        AnimatedVisibility(
            visible = uiState is MenuUiState.Success,
            enter = slideInVertically(initialOffsetY = { it / 2}),
            exit = slideOutVertically(targetOffsetY = { it / 2})
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 100.dp, horizontal = 60.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
            TextButton(
                onClick = {
                    navController.navigate(Screen.LandmarksQuizScreen.route)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    ),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                val state = if (uiState is MenuUiState.Success) uiState else return@TextButton
                if(state.data.isFirstLaunch){
                    Text(
                        text = stringResource(R.string.start),
                        fontFamily = FontFamily(Font(R.font.bubblegum_sans),),
                        fontSize = 20.sp
                    )
                }else{
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ){
                        Text(
                            text = stringResource(R.string.continue_),
                            fontFamily = FontFamily(Font(R.font.bubblegum_sans),),
                            fontSize = 20.sp
                        )

                        Box(contentAlignment = Alignment.Center){
                            LinearProgressIndicator(
                                progress = {
                                    state.data.progress.toFloat() / state.maxId.toFloat()
                                },
                                modifier = Modifier
                                    .height(16.dp)
                                    .width(160.dp)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline,
                                        RoundedCornerShape(10.dp)
                                    ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                strokeCap = StrokeCap.Round,
                                gapSize = -300.dp,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                drawStopIndicator = {}
                            )
                            Text(
                                text = "${state.data.progress+1} of ${state.maxId+1}",
                                fontFamily = FontFamily(Font(R.font.bubblegum_sans),),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
        }


        AnimatedVisibility(
                visible = uiState is MenuUiState.Loading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 100.dp, horizontal = 60.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                LinearProgressIndicator(
                    progress = { loadAnim },
                    modifier = Modifier
                        .height(12.dp)
                        .border(2.dp, MaterialTheme.colorScheme.outline, RectangleShape),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    trackColor = MaterialTheme.colorScheme.tertiaryContainer,
                    drawStopIndicator = {}
                )

                Spacer(modifier = Modifier.size(10.dp))

                Text(
                    text = "${stringResource(R.string.loading)}...",
                    fontFamily = FontFamily(Font(R.font.bubblegum_sans)),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }}


        if(uiState is MenuUiState.Error){
            ErrorDialog(
                modifier = Modifier.align(Alignment.Center),
                message = uiState.message,
                onDismiss = load,
            )
        }

        if(showSettings && uiState is MenuUiState.Success){
            SettingsDialog(
                soundState = uiState.data.isSoundEnabled,
                onDismiss = { showSettings = false },
                resetProgressClick = { showResetProgressDialog = true },
                viewSourceClick = {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://www.github.com/andy-ife/Geobud")
                    val title = activity.getText(R.string.check_source)
                    val chooser = Intent.createChooser(intent, title)
                    activity.startActivity(chooser)
                },
                toggleSoundClick = toggleSound
            )
        }

        if(showResetProgressDialog){
            ResetProgressDialog(
                message = stringResource(R.string.confirm_reset_data),
                onDismiss = { showResetProgressDialog = false }
            ) {
                showSettings = false
                showResetProgressDialog = false
                resetProgress()
            }
        }
    }
}




