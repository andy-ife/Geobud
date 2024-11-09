package com.andyslab.geobud.ui.trivia

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andyslab.geobud.R

@Composable
fun TriviaScreen(){
    Column(modifier = Modifier
        .background(Brush.verticalGradient(
            listOf(Color(0xFF2C3E50), Color.Black), startY = 200f
        ))
        .fillMaxSize()
        .padding(start = 8.dp, top = 60.dp, end = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){

        Image(painter = painterResource(id = R.drawable.explorer),
            contentDescription = "Explorer of the unknown",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .clip(RoundedCornerShape(60.dp)))

        Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))

        Text(text = "More quizzes coming soon...",
            fontSize = 16.sp,
            color = Color.White)

    }
}

@Composable
@Preview
fun TriviaScrePrev(){
    TriviaScreen()
}
