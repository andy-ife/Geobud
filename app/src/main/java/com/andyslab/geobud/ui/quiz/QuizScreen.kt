package com.andyslab.geobud.ui.quiz
import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImagePainter
import coil3.compose.ImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.andyslab.geobud.R
import com.andyslab.geobud.ui.components.CorrectAnswerBottomSheet
import com.andyslab.geobud.ui.components.ErrorDialog
import com.andyslab.geobud.ui.components.FetchingMorePhotosDialog
import com.andyslab.geobud.ui.components.QuizOptionButton
import com.andyslab.geobud.ui.components.ShowBottomSheetButton
import com.andyslab.geobud.ui.components.TopBarItem
import com.andyslab.geobud.ui.nav.Screen
import com.andyslab.geobud.utils.shimmerLoadingEffect
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.ktx.ExperimentGlideFlows
import kotlinx.coroutines.launch

//Stateful
@Composable
fun QuizScreen(
    navController: NavHostController,
    viewModel: QuizViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if(uiState.player != null){
        QuizScreen(
            navController = navController,
            uiState = uiState,
            getPhoto = viewModel::getPhoto,
            checkAnswer = viewModel::checkAnswer,
            generateExclamation = viewModel::generateExclamation,
            onPhotoLoadFailed = viewModel::onPhotoLoadFailed
        )
    }
}

//Stateless
@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun QuizScreen(
    navController: NavHostController,
    uiState: QuizUiState,
    getPhoto: () -> Unit,
    checkAnswer: (String) -> Boolean,
    generateExclamation: () -> String,
    onPhotoLoadFailed: () -> Unit,
    ){
    val landmark = uiState.player!!.currentLandmark!!

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false,
        )

    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    val scope = rememberCoroutineScope()
    val lottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.confetti))

    var textVisible by remember{ mutableStateOf(true) }

    val painter: AsyncImagePainter = rememberAsyncImagePainter(landmark.photoUrl ?: R.drawable.placeholder_drawable)
    val painterState by painter.state.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if(uiState.answerCorrect == true){
            sheetState.expand()
        }
    }

    //the point of this wrapper box is to have something to add the confetti animations to
    Box(modifier = Modifier.fillMaxSize()){
        BottomSheetScaffold(
        sheetContent = {
            CorrectAnswerBottomSheet(
                exclamation = generateExclamation(),
                addedCoins = 20,
                addedStars = 1,
                landmark = landmark,
                continueButtonClick = {
                    scope.launch{ sheetState.hide() }
                    getPhoto()
                }
            )
                       },
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            sheetPeekHeight = 0.dp,
            sheetContainerColor = Color.White) {

            Image(
                painter = painter,
                contentDescription = "landmark photo",
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_UP -> {
                                textVisible = !textVisible
                            }
                            else -> {}
                        }
                        true
                    },
                contentScale = ContentScale.FillBounds,
            )

            //shimmer loading box
            AnimatedVisibility(
                visible = (painterState is AsyncImagePainter.State.Loading || uiState.photoLoading),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmerLoadingEffect()
                )
            }

            //gradient box to improve text visibility
            AnimatedVisibility(
                visible = textVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ){
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0x19000000),
                                Color.Transparent,
                                Color(0xB2000000)
                            ),
                        )
                    ))
            }

            Column(verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)) {

                AnimatedVisibility(
                    visible = textVisible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ){
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ){

                        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "back",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    navController.navigate(Screen.LoadingScreen.route) {
                                        popUpTo(Screen.LoadingScreen.route) {
                                            inclusive = true
                                        }
                                    }
                                },
                            tint = Color.White)

                        Text(text = "Guess the Country - ${landmark.id + 1}",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.bubblegum_sans)),
                            color = Color.White,
                        )

                        Spacer(modifier = Modifier.width(50.dp))

                        TopBarItem(
                            icon = R.drawable.heart,
                            text = uiState.player.hearts.toString(),
                            )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                AnimatedVisibility(visible = textVisible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ){
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),) {

                    Icon(painter = painterResource(
                        id = R.drawable.outline_file_download_24),
                        contentDescription = "save photo",
                        modifier = Modifier.size(30.dp),
                        tint = Color.White
                    )
                }
                }

            }


        AnimatedVisibility(
            visible = textVisible && uiState.answerCorrect!=true,
            enter = fadeIn(),
            exit = fadeOut()
        ){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            val options = uiState.player.currentOptions
            QuizOptionButton(text = options.elementAt(0),){
                checkAnswer(it)
            }
            QuizOptionButton(text = options.elementAt(1),){
                checkAnswer(it)
            }
            QuizOptionButton(text = options.elementAt(2),){
                checkAnswer(it)
            }
            QuizOptionButton(text = options.elementAtOrElse(3) { "Nigeria" },){
                checkAnswer(it)
            }
        }
        }

            if(uiState.fetchingMorePhotos != null){
                FetchingMorePhotosDialog(progress = uiState.fetchingMorePhotos.progress) {
                    getPhoto()
                }
            }

            if((uiState.error != null || painterState is AsyncImagePainter.State.Error)){
                ErrorDialog(message = uiState.error?.message?:"We couldn't load the photo. Check your internet connection.") {
                    getPhoto()
                    painter.restart()
                }
            }


        AnimatedVisibility(
            visible = uiState.answerCorrect == true && textVisible,
            enter = fadeIn(tween(2000)),
            exit = fadeOut(tween(0))
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Bottom,){
                ShowBottomSheetButton(modifier = Modifier) {
                scope.launch {
                    sheetState.expand()
                }
            }
            }
        }
        }
        //lottie
        if(uiState.answerCorrect == true){
            LottieAnimation(composition = lottie)
        }
    }
}


@Composable
@Preview
fun QuizScreenPrev(){

}