package com.andyslab.geobud.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andyslab.geobud.R
import com.andyslab.geobud.data.model.Landmark
import com.andyslab.geobud.utils.onClickWithScaleAnim


@Composable
fun CorrectAnswerBottomSheet(
    modifier: Modifier = Modifier,
    exclamation: String,
    addedCoins: Int,
    addedStars: Int,
    landmark: Landmark,
    continueButtonClick: () -> Unit = {},
    ){

    Column(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(Color.White)
        .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start){
        
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center){
            Text(text = exclamation,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xff1A5518),)

            Spacer(modifier = Modifier.width(30.dp))

            Image(painter = painterResource(id = R.drawable.star),
                modifier = Modifier.size(24.dp,),
                contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = addedStars.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xff1A5518))

            Spacer(modifier = Modifier.width(30.dp))

            Image(painter = painterResource(id = R.drawable.coin),
                modifier = Modifier.size(24.dp),
                contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = addedCoins.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xff1A5518))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = landmark.name
                .split(" ")
                .joinToString(" ")
                { it.capitalize() },
            fontSize = 16.sp)

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "${landmark.city}, ${landmark.country}",
            color = Color.Gray,
            fontSize = 13.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                Color(0xFFEAF3FB),
                RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
        ){
            Text(text = landmark.funFact,
                fontSize = 14.sp,
                textAlign = TextAlign.Center)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            ){
            IconButton(onClick = { /*TODO*/ },
                modifier = Modifier.onClickWithScaleAnim(0.9f,),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF227B14)
                )) {
                Icon(imageVector = Icons.Filled.ThumbUp,
                    contentDescription = null)
            }

            IconButton(onClick = { /*TODO*/ },
                modifier = Modifier
                    .onClickWithScaleAnim(0.9f)
                    .rotate(180f)
                    .offset(x = 10.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFFDD2929)
                )) {
                Icon(imageVector = Icons.Filled.ThumbUp,
                    contentDescription = null)
            }

            Spacer(modifier = Modifier.width(100.dp))

            TextButton(onClick = { /*TODO*/ },
                modifier = Modifier.weight(1f)
                    .onClickWithScaleAnim(0.9f) { continueButtonClick() },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color(0xFF1976D2),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(2.dp),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
               Text(text = "Continue",
                   fontSize = 14.sp)
            }

        }
    }
}
