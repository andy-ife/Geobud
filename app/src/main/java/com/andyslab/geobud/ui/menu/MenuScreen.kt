package com.andyslab.geobud.ui.menu


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.andyslab.geobud.R
import com.andyslab.geobud.data.model.Player
import com.andyslab.geobud.ui.components.ErrorDialog
import com.andyslab.geobud.ui.components.TopBarItem
import com.andyslab.geobud.ui.nav.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//Stateful
@Composable
fun MenuScreen(
    navController: NavHostController,
    viewModel: MenuViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MenuScreen(navController, uiState, viewModel::load)
}

//Stateless
@Composable
fun MenuScreen(
    navController: NavHostController,
    uiState: MenuUiState,
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

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_light),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Image(
            painter = painterResource(id = R.drawable.geobud_logo),
            contentDescription = "app logo",
            modifier = Modifier
                .size(200.dp)
                .offset(y = -80.dp)
        )

        if(uiState is MenuUiState.Success && showTimerPopup){
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 30.dp),) {
                Box(modifier = Modifier.background(Color.Black)) {
                    Text(
                        text = uiState.timeTillNextHeart,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.bubblegum_sans))
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = uiState is MenuUiState.Success,
            enter = slideInVertically(),
            ) {
                Row (
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    IconButton(
                        onClick = {},
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Image(
                            painter = painterResource(R.drawable.cog),
                            contentDescription = "settings"
                        )
                    }

                    val text = (uiState as MenuUiState.Success).data.hearts.toString()
                    TopBarItem(
                        modifier = Modifier.clickable{
                            if(uiState.data.hearts < 3){
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
            enter = slideInVertically( initialOffsetY = { it / 2} )
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
                    containerColor = Color(0xFF4CAF50),
                    ),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(
                    text = "Start",
                    fontFamily = FontFamily(Font(R.font.bubblegum_sans),),
                    fontSize = 20.sp
                )
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
                        .border(2.dp, Color.Black, RectangleShape),
                    color = Color.Yellow,
                    trackColor = Color.Black,
                    drawStopIndicator = {}
                )

                Spacer(modifier = Modifier.size(10.dp))

                Text(
                    text = "Loading...",
                    fontFamily = FontFamily(Font(R.font.bubblegum_sans)),
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }}


        if(uiState is MenuUiState.Error){
            ErrorDialog(
                modifier = Modifier.align(Alignment.Center),
                message = uiState.message,
                onDismiss = load,
            )
        }
    }
}

@Preview
@Composable
fun MenuScreenPreview(){
    MenuScreen(
        rememberNavController(),
        MenuUiState.Success(Player()),
        {}
    )
}




