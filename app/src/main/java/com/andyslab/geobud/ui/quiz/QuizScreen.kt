package com.andyslab.geobud.ui.quiz
import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.andyslab.geobud.R
import com.andyslab.geobud.data.model.Landmark
import com.andyslab.geobud.ui.components.CorrectAnswerBottomSheet
import com.andyslab.geobud.ui.nav.Screen
import com.andyslab.geobud.ui.components.TopBarItem
import com.andyslab.geobud.utils.shimmerLoadingEffect
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun QuizScreen(navController: NavController){
    val context = LocalContext.current
    //val viewModel = QuizViewModel(context)

    val correctAnswer = remember{
        mutableStateOf(false)
    }

    var photo by remember{
        mutableStateOf(ContextCompat.getDrawable(context, R.drawable.placeholder_drawable))
    }

    var textVisible by remember{
        mutableStateOf(true)     
    }

    var currOptions by remember{
        mutableStateOf<List<String>>(listOf("","","",""))
    }

    //val hearts by viewModel.hearts.collectAsState()
    //val currLandmark by viewModel.currLandmark.collectAsState()

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false,
        )

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val bottomSheetScope = rememberCoroutineScope()
    var bottomSheetLandmark by remember{
        mutableStateOf(Landmark(0,"","","","","","","",""))
    }//variable for landmark info displayed on the bottom sheet

    var imageLoading by remember{
        mutableStateOf(true)
    }

    val lottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.confetti))

//    LaunchedEffect(correctAnswer.value){
//        //delay(2000)
//        if(!correctAnswer.value){
//            //currOptions = viewModel.generateOptions().toList()
//        }
//
//        if(correctAnswer.value){
//            //bottomSheetLandmark = currLandmark
//            sheetState.expand()
//        }
//
//        if(!correctAnswer.value){
//            viewModel.loadImage()
//            viewModel.imageState.collect { event ->
//                    when (event) {
//                        is QuizViewModel.ImageLoadEvent.LoadImage -> {
//                            imageLoading = true
//                        }
//                        is QuizViewModel.ImageLoadEvent.ImageLoaded -> {
//                            imageLoading = false
//                            photo = event.drawable
//                            // Use the loaded bitmap with GlideImage model or other UI logic
//                        }
//
//                        else -> {}
//                    }
//            }
//        }
//    }

    //the point of this wrapper box is to have something to add the confetti animations to
    Box(modifier = Modifier.fillMaxSize()){
    BottomSheetScaffold(
        sheetContent = {
            CorrectAnswerBottomSheet(
                exclamation = "Nice",//viewModel.generateExclamation(),
                addedCoins = 20,
                addedStars = 1,
                landmark = bottomSheetLandmark,
                continueButtonClick = {
                    bottomSheetScope.launch{
                    sheetState.hide()
                    }
                    //viewModel.updatePlayerData()
                    imageLoading = true
                    correctAnswer.value = false //make launched effect run again
                }
            )
                       },
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContainerColor = Color.White){

        GlideImage(model = photo,
            contentDescription = "Landmark Photo",
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
        AnimatedVisibility(visible = imageLoading,
            enter = fadeIn(),
            exit = fadeOut()) {
            Box(modifier = Modifier
                .fillMaxSize()
                .shimmerLoadingEffect())
        }

        AnimatedVisibility(visible = textVisible,
            enter = fadeIn(),
            exit = fadeOut()){

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

            AnimatedVisibility(visible = textVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ){
            Row (verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()){

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

                Text(text = "Landmarks - Pack 1",
                    fontSize = 16.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White,
                )

                Spacer(modifier = Modifier.width(50.dp))

                TopBarItem(icon = R.drawable.heart,
                    modifier = Modifier.weight(1f),
                    text = "3",)//hearts.toString())
            }}

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedVisibility(visible = textVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ){
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),) {
                Text(text = "Landmark 1/10",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White,
                    )

                Icon(painter = painterResource(
                    id = R.drawable.outline_file_download_24),
                    contentDescription = "save photo",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White)
        }}

        }

        AnimatedVisibility(visible = textVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            OptionButton(text = currOptions.elementAt(0), correctAnswer){
                //viewModel.isAnswerCorrect(it)
                true
            }
            OptionButton(text = currOptions.elementAt(1), correctAnswer){
                //viewModel.isAnswerCorrect(it)
                true
            }
            OptionButton(text = currOptions.elementAt(2), correctAnswer){
               // viewModel.isAnswerCorrect(it)
                true
            }

            OptionButton(text = currOptions.elementAt(3), correctAnswer){
                    //viewModel.isAnswerCorrect(it)
                true
            }
        }
        }

        AnimatedVisibility(
            visible = correctAnswer.value && textVisible,
            enter = fadeIn(tween(2000)),
            exit = fadeOut(tween(0))
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End){
                ShowBottomSheetButton(modifier = Modifier) {
                bottomSheetScope.launch {
                    sheetState.expand()
                }
            }
            }
        }

    }
        //lottie
        if(correctAnswer.value){
            LottieAnimation(composition = lottie)
        }
    }
}


@Composable
@Preview
fun QuizScreenPrev(){
    QuizScreen(rememberNavController())
    //ShowBottomSheetButton(modifier = Modifier,){}
}

@Composable
fun OptionButton(text: String, isCorrect: MutableState<Boolean>, checkAnswer: (String) -> Boolean){
    val buttonColorScope = rememberCoroutineScope()

    var buttonColor by remember{
        mutableStateOf(Color(0x651976D2))
    }

    val buttonColorAnim by animateColorAsState(
        targetValue = buttonColor ,
        label = "button color anim"
    )

    TextButton(onClick = {
                         if(checkAnswer(text)){
                             buttonColorScope.launch{
                                 buttonColor = Color(0x6566BB6A)
                                 delay(2000)
                                 buttonColor = Color(0x651976D2)
                             }
                             isCorrect.value = true
                         }else{
                             buttonColorScope.launch{
                                 buttonColor = Color(0x65F44336)
                                 delay(1000)
                                 buttonColor = Color(0x651976D2)
                             }
                             isCorrect.value = false
                         }
    },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = buttonColorAnim,
            contentColor = Color.White
        ),
        //contentPadding = PaddingValues(2.dp),
        modifier = Modifier.width(180.dp)
    ) {
        Text(text = text,
            fontSize = 14.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Light)
    }
}

@Composable
fun ShowBottomSheetButton(modifier: Modifier, onClick: () -> Unit){
    val transition = rememberInfiniteTransition(label = "button color")
    val color by transition.animateColor(
        initialValue = Color(0xFFCDDC39),
        targetValue = Color(0xFFFFB300),
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = "button color"
    )

    FilledIconButton(onClick = { onClick()},
        modifier = modifier
            .size(48.dp)
            .border(2.dp, Color.White, CircleShape,),
        shape = CircleShape,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = color
        )) {
        Image(painter = painterResource(id = R.drawable.right_arrow_next_svgrepo_com),
            contentDescription = "award",
            modifier = Modifier
                .size(25.dp)
                .offset(1.dp))
    }
}




