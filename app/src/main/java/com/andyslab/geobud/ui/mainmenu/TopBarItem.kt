package com.andyslab.geobud.ui.mainmenu

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andyslab.geobud.R
import com.andyslab.geobud.utils.composeextensions.onClickWithScaleAnim


@Composable
fun TopBarItem(@DrawableRes icon: Int,
               modifier: Modifier,
               text:String){

    Box(modifier = modifier
        .height(45.dp)
        .width(10.dp),
        contentAlignment = Alignment.Center){


        Text(modifier = Modifier
            .fillMaxWidth(0.7f)
            .background(Color(0xB2000000), RoundedCornerShape(8.dp))
            .align(Alignment.Center)
            .padding(2.dp),
            text = text,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 15.sp,
            fontFamily = FontFamily(Font(R.font.bubblegum_sans)),
            overflow = TextOverflow.Clip
        )

        Button3d(icon = R.drawable.plus,)

        Image(modifier = Modifier
            .size(28.dp)
            .align(Alignment.CenterStart)
            .offset(x=4.dp),
            painter = painterResource(id = icon),
            contentDescription = "Top bar item icon",
            contentScale = ContentScale.FillBounds,)



    }
}

@Composable
fun Button3d(@DrawableRes icon: Int){
    Row(horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
            .offset((-8).dp)){
    Box(modifier = Modifier.background(Brush.verticalGradient(
        listOf(Color(0xFF74D680),
            Color(0XFF28731B))
    ),CircleShape)
        .onClickWithScaleAnim(0.9f)
        .border(1.dp, Color.White, CircleShape)
        .size(26.dp)
        .shadow(2.dp, CircleShape)
        .clickable {  },
        contentAlignment = Alignment.Center){
        Image(painter = painterResource(id = R.drawable.plus),
            contentDescription = null ,
            modifier = Modifier.onClickWithScaleAnim(0.9f))
    }
    }
}