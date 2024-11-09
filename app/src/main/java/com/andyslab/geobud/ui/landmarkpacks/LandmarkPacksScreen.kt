package com.andyslab.geobud.ui.landmarkpacks

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.andyslab.geobud.R
import com.andyslab.geobud.ui.Screen
import com.andyslab.geobud.utils.composeextensions.onClickWithScaleAnim

@Composable
fun LandmarkPacksScreen(navController: NavHostController){
    val viewModel = LandmarkPacksViewModel()
    val player = LandmarkPacksViewModel.player

    val images = listOf(
        R.drawable.eiffel_photo,
        R.drawable.camels,
        R.drawable.burg_khalifa,
        R.drawable.japan_sea,
    )

    val descs = listOf("0/10","0/15","0/20","0/25")

    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.915f),
        contentPadding = PaddingValues(top = 150.dp,
            start = 10.dp,
            end = 10.dp,),
        verticalArrangement = Arrangement.spacedBy(30.dp)){
        items(4){ index ->
            LandmarkPackCard(modifier = Modifier,
                image = images[index],
                title = "Pack " + (index+1),
                progress = 0f,
                isLocked = false,
                desc = descs[index]){
                navController.navigate(Screen.LandmarksQuizScreen.route)
            }
        }
    }

}

@Composable
fun LandmarkPackCard(
    modifier: Modifier,
    @DrawableRes image: Int,
    title: String,
    desc: String,
    progress: Float,
    isLocked: Boolean,
    onClick: () -> Unit,
){
    Box(modifier = modifier
        .onClickWithScaleAnim(0.95f, onClick)
        .fillMaxWidth()
        .height(120.dp)
        .background(Color.Transparent),
        contentAlignment = Alignment.BottomStart){

        ElevatedCard (shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.elevatedCardElevation(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)){

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 120.dp, end = 20.dp, top = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = title,
                    fontFamily = FontFamily(Font(R.font.bubblegum_sans)),
                    color = Color.Black,
                    fontSize = 24.sp)

                    IconButton(onClick = { /*TODO*/ },
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF57E15E), Color(0xFF388E3C))
                                ), CircleShape
                            )
                            .size(28.dp),
                        colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                        ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    }
                }

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 120.dp)){
                Text(text = desc,
                    fontFamily = FontFamily(Font(R.font.bubblegum_sans)),
                    fontSize = 16.sp)
            }

Spacer(modifier = Modifier
    .fillMaxWidth()
    .height(10.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 120.dp)){

                LinearProgressIndicator(modifier = Modifier
                    .height(15.dp)
                    .width(160.dp),
                    trackColor = Color(0XFF0D47A1),
                    color = Color(0xFFBBDEFB),
                    progress = 0.3f)
            }


        }
Box(modifier = Modifier
    .fillMaxHeight()
    .padding(bottom = 10.dp, start = 10.dp),
    contentAlignment = Alignment.TopStart){

        Image(painter = painterResource(id = image),
    contentDescription = null,
    modifier = Modifier
        .size(100.dp)
        .aspectRatio(1f)
        .clip(RoundedCornerShape(8.dp)),
    contentScale = ContentScale.Crop)

    }
    }
}

@Composable
@Preview
fun LandmarksScreenPrev(){
    LandmarkPacksScreen(rememberNavController())
}
