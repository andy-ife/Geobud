package com.andyslab.geobud.ui.loading


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset

import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.andyslab.geobud.R
import com.andyslab.geobud.ui.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


var key = true

@Composable
fun LoadingScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel = LoadingScreenViewModel(context)
    var loadingProgress by remember {
        mutableFloatStateOf(0f)
    }
    val loadAnim by animateFloatAsState(
        targetValue = loadingProgress,
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing
        ),
        visibilityThreshold = 0.001f,
        label = "loading bar anim"
    )
    val hasURLs = viewModel.hasPhotoURLs
    val hasPhotos = viewModel.hasDownloadedPhotos

    var showError by remember{
        mutableStateOf("")
    }

    val niftyScope = rememberCoroutineScope()

    //launched effect has two collectors, one collects loading errors,
    //the other collects loading progress increments
    LaunchedEffect(key1 = key) {
        viewModel.doLoading()

        niftyScope.launch {
            viewModel.showErrorDialog.collect{
                showError = it
            }
        }

        viewModel.loadProgress.collect {
            loadingProgress = it
            if (hasURLs.value && hasPhotos.value) {
                delay(200)
                navController.navigate(Screen.MainMenuScreen.route) {
                    popUpTo(Screen.LoadingScreen.route) {
                        inclusive = true
                    }
                }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFBBDEFB),
                        Color(0xFFBBDEFB),
                        Color.White
                    )
                )
            ),
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.geobud_logo),
                contentDescription = "Geobud Logo",
                modifier = Modifier
                    .size(200.dp)
                    .offset(0.dp, 100.dp)
            )

            Spacer(modifier = Modifier.size(300.dp))

            LinearProgressIndicator(
                progress = loadAnim,
                modifier = Modifier
                    .height(12.dp)
                    .border(2.dp, Color.Black, RectangleShape),
                color = Color.Yellow,
                trackColor = Color.Black,
            )

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = "Loading...",
                fontFamily = FontFamily(Font(R.font.bubblegum_sans)),
                fontSize = 14.sp,
                color = Color.Black
            )
        }

        if(showError.isNotBlank()){
            ErrorDialog(
                modifier = Modifier.align(Alignment.Center),
                message = showError) {
                viewModel.retryLoading()
                showError = ""
                key = !key//to make launched effect run again.
            }
        }
    }

}




