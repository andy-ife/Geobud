package com.andyslab.geobud.ui.quiz
import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.andyslab.geobud.R
import com.andyslab.geobud.ui.components.CorrectAnswerBottomSheet
import com.andyslab.geobud.ui.components.ErrorDialog
import com.andyslab.geobud.ui.components.FetchingMorePhotosDialog
import com.andyslab.geobud.ui.components.OutOfHeartsDialog
import com.andyslab.geobud.ui.components.QuizOptionButton
import com.andyslab.geobud.ui.components.ShowBottomSheetButton
import com.andyslab.geobud.ui.components.TopBarItem
import com.andyslab.geobud.ui.nav.Screen
import com.andyslab.geobud.utils.clickableNoRipple
import com.andyslab.geobud.utils.findActivity
import com.andyslab.geobud.utils.hasWriteStoragePermissions
import com.andyslab.geobud.utils.hideSystemUI
import com.andyslab.geobud.utils.openAppSettings
import com.andyslab.geobud.utils.shimmerLoadingEffect
import com.andyslab.geobud.utils.showSystemUI
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val permissions =
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    else
        emptyArray()

//Stateful
@Composable
fun QuizScreen(
    navController: NavHostController,
    viewModel: QuizViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val activity = context.findActivity()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if(uiState.player != null){
        QuizScreen(
            navController = navController,
            activity = activity,
            uiState = uiState,
            getPhoto = viewModel::getPhoto,
            checkAnswer = viewModel::checkAnswer,
            generateExclamation = viewModel::generateExclamation,
            savePhoto = viewModel::savePhoto
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
    activity: ComponentActivity,
    uiState: QuizUiState,
    getPhoto: () -> Unit,
    checkAnswer: (String) -> Boolean,
    generateExclamation: () -> String,
    savePhoto: (String, Bitmap) -> Unit,
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
    var showTimerPopup by remember { mutableStateOf(false) }
    var showOutOfHeartsPopup by remember { mutableStateOf(false) }

    val painter: AsyncImagePainter = rememberAsyncImagePainter(landmark.photoUrl ?: R.drawable.placeholder_drawable)
    val painterState by painter.state.collectAsStateWithLifecycle()

    val interactionSource = remember{ MutableInteractionSource() }
    val snackbarHostState = remember{ SnackbarHostState() }

    var lastPhotoUrl = remember{ landmark.photoUrl ?: "" }
    var lastLandmarkName = remember { landmark.name }
    var exclamation by remember{ mutableStateOf(generateExclamation()) }

    var showPermissionDialog by remember{ mutableStateOf(false) }
    val permsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permsMap ->
        permsMap.forEach{
            if(!it.value)
                showPermissionDialog = true
        }
    }

    val correctSoundEffectPlayer = remember { MediaPlayer.create(activity, R.raw.correct_sound_effect) }
    val wrongSoundEffectPlayer = remember { MediaPlayer.create(activity, R.raw.wrong_sound_effect) }

    LaunchedEffect(uiState) {
        if(uiState.savingPhoto != null){
            snackbarHostState.showSnackbar(uiState.savingPhoto)
            return@LaunchedEffect
        }

        if(uiState.answerCorrect == true){
            correctSoundEffectPlayer.start()
            exclamation = generateExclamation()
            sheetState.expand()
        }else if(uiState.answerCorrect == false){
            wrongSoundEffectPlayer.start()
        }

        if(uiState.answerCorrect != true && landmark.photoUrl != lastPhotoUrl){
            lastPhotoUrl = landmark.photoUrl ?: ""
            lastLandmarkName = landmark.name
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            correctSoundEffectPlayer.release()
            wrongSoundEffectPlayer.release()
        }
    }

    //the point of this wrapper box is to have something to add the confetti animations to
    Box(modifier = Modifier.fillMaxSize()){
        BottomSheetScaffold(
        sheetContent = {
            CorrectAnswerBottomSheet(
                exclamation = exclamation,
                addedCoins = 20,
                addedStars = 1,
                landmark = landmark,
                photographer = "Photo by ${landmark.photographer}",
                photographerUrlClick = {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(landmark.photographerUrl)
                    val title = "Go to photographer's page"
                    val chooser = Intent.createChooser(intent, title)
                    activity.startActivity(chooser)
                },
                continueButtonClick = {
                    scope.launch{ sheetState.hide() }
                    getPhoto()
                },
                savePhotoBtnClick = {
                    if (!activity.hasWriteStoragePermissions()){
                        permsLauncher.launch(permissions)
                    }
                    else{
                        scope.launch{
                            val loader = ImageLoader(activity)
                            val req = ImageRequest.Builder(activity)
                                .data(lastPhotoUrl)
                                .allowHardware(false)
                                .build()

                            val bitmap = (loader.execute(req) as SuccessResult).image.toBitmap()
                            savePhoto(lastLandmarkName, bitmap)
                        }
                    }
                }
            )
                       },
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            sheetPeekHeight = 0.dp,
            sheetContainerColor = Color.White,
            snackbarHost = {SnackbarHost(hostState = snackbarHostState, modifier = Modifier.offset(y = -40.dp))}
            ) {

            Image(
                painter = painter,
                contentDescription = "landmark photo",
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_UP -> {
                                textVisible = !textVisible
                                if (textVisible)
                                    activity.showSystemUI()
                                else
                                    activity.hideSystemUI()
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 40.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
                ){
                AsyncImage(
                    model = "https://images.pexels.com/lib/api/pexels-white.png",
                    contentDescription = null,
                    modifier = Modifier.size(60.dp).clickableNoRipple(interactionSource){
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("https://www.pexels.com")
                        val title = "Go to pexels.com"
                        val chooser = Intent.createChooser(intent, title)
                        activity.startActivity(chooser)
                    }
                )
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp, bottom = 10.dp)
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
                            modifier = Modifier.clickable{
                                if(uiState.player.hearts < 3){
                                    scope.launch{
                                        showTimerPopup = true
                                        delay(5000)
                                        showTimerPopup = false
                                    }
                                }
                            },
                            icon = R.drawable.heart,
                            text = uiState.player.hearts.toString(),
                            )
                    }
                }

                if(showTimerPopup && textVisible){
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp),) {
                        Box(modifier = Modifier
                            .background(Color(0xB2000000), RoundedCornerShape(8.dp))
                            .padding(8.dp)) {
                            Text(
                                text = "More in ${uiState.timeTillNextHeart ?: "00 : 00"}",
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.bubblegum_sans))
                            )
                        }
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
                        modifier = Modifier.size(30.dp).clickableNoRipple(interactionSource){
                            if (!activity.hasWriteStoragePermissions()){
                                permsLauncher.launch(permissions)
                            }
                            else{
                                scope.launch{
                                    val loader = ImageLoader(activity)
                                    val req = ImageRequest.Builder(activity)
                                        .data(lastPhotoUrl)
                                        .allowHardware(false)
                                        .build()

                                    val bitmap = (loader.execute(req) as SuccessResult).image.toBitmap()
                                    savePhoto(lastLandmarkName, bitmap)
                                }
                            }
                        },
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
            .padding(bottom = 60.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            val options = uiState.player.currentOptions
            QuizOptionButton(text = options.elementAt(0),){
                if(uiState.player.hearts == 0) showOutOfHeartsPopup = true
                checkAnswer(it)
            }
            QuizOptionButton(text = options.elementAt(1),){
                if(uiState.player.hearts == 0) showOutOfHeartsPopup = true
                checkAnswer(it)
            }
            QuizOptionButton(text = options.elementAt(2),){
                if(uiState.player.hearts == 0) showOutOfHeartsPopup = true
                checkAnswer(it)
            }
            QuizOptionButton(text = options.elementAtOrElse(3) { "Nigeria" },){
                if(uiState.player.hearts == 0) showOutOfHeartsPopup = true
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
                ErrorDialog(message = uiState.error?.message?:"We couldn't load the photo. Please check your internet connection.") {
                    getPhoto()
                    painter.restart()
                }
            }

            if(uiState.player.hearts == 0 && showOutOfHeartsPopup){
                OutOfHeartsDialog(timer = uiState.timeTillNextHeart!!) {
                    showOutOfHeartsPopup = false
                }
            }

            if(showPermissionDialog){
                ErrorDialog(
                    message = "Geobud can't save photos to your phone storage without storage permission. " +
                        "Please go to app settings to grant it.") {
                    showPermissionDialog = false
                    activity.openAppSettings()
                }
            }


        AnimatedVisibility(
            visible = uiState.answerCorrect == true && textVisible,
            enter = fadeIn(tween(2000)),
            exit = fadeOut(tween(0))
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 100.dp),
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
