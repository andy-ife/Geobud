package com.andyslab.geobud.ui.mainmenu

import com.andyslab.geobud.utils.composeextensions.onClickWithScaleAnim
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.andyslab.geobud.R
import com.andyslab.geobud.ui.Screen


@Composable
fun MainMenuScreen(mainMenuNavController: NavHostController){
    val viewModel = MainMenuViewModel()
    val bottomNavController = rememberNavController()

    val hasMissions by viewModel.hasMissions.collectAsState()
    val hasNewPhoto by viewModel.hasNewPhoto.collectAsState()
    val stars by viewModel.stars.collectAsState()
    val coins by viewModel.coins.collectAsState()
    val hearts by viewModel.hearts.collectAsState()

    Scaffold(
    bottomBar = { BottomBar(bottomNavController)})
{
    scaffoldPadding ->
    
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(scaffoldPadding)){
        Image(modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.layered_waves_haikei),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            )
    }

    BottomNavGraph(mainMenuNavController ,bottomNavController)//Nav graph that holds landmark and trivia screens

    //Column with top bars, settings btn, gallery btn, missions btn
    Column (verticalArrangement = Arrangement.Top,
            modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)){

        Row (horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()){
            TopBarItem(icon = R.drawable.star, Modifier.weight(2.4f), stars.toString())
            TopBarItem(icon = R.drawable.coin, Modifier.weight(3f), coins.toString())
            TopBarItem(icon = R.drawable.heart, Modifier.weight(2.4f), hearts.toString())

        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){

            ElevatedButton(onClick = {},
                shape = CircleShape,
                modifier = Modifier
                    .onClickWithScaleAnim(0.9f)
                    .border(2.dp, Color.White, CircleShape)
                    .size(85.dp),
                colors = ButtonDefaults
                    .elevatedButtonColors(containerColor = Color(0xFF71A9ED)),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 8.dp
                )){

                Image(painter = painterResource(id = R.drawable.treasure_map),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .onClickWithScaleAnim(0.9f),
                    contentScale = ContentScale.FillBounds)


            }

//settings and gallery btn
            Column (verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxHeight()){

                IconButton(onClick = {},
                    modifier = Modifier
                        .onClickWithScaleAnim(0.9f)
                        .shadow(2.dp, RoundedCornerShape(8.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF5EAAF5), Color(0XFF145CA4)),
                            ), RoundedCornerShape(8.dp)
                        )
                        .border(1.5.dp, Color.White, RoundedCornerShape(8.dp))
                        .weight(1f, fill = false)
                        .size(40.dp)
                        ,
                ){
                    Image(painter = painterResource(id = R.drawable.cog),
                        contentDescription = "Settings Icon",
                        modifier = Modifier
                            .onClickWithScaleAnim(0.9f)
                            .size(24.dp),
                        alignment = Alignment.TopCenter)
                }

                //Spacer(modifier = Modifier.height(4.dp))


                IconButton(onClick = { /*TODO*/ },
                modifier = Modifier
                    .onClickWithScaleAnim(0.9f)
                    .shadow(2.dp, RoundedCornerShape(8.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF5E66E3), Color(0XFF092F68)),
                        ), RoundedCornerShape(8.dp)
                    )
                    .border(1.5.dp, Color.White, RoundedCornerShape(8.dp))
                    //.weight(1f, fill = true)
                    .size(40.dp)
                    ,
                //.align(Alignment.CenterVertically)
            ){
                Image(painter = painterResource(id = R.drawable.pic_cards),
                    contentDescription = "Settings Icon",
                    modifier = Modifier
                        .onClickWithScaleAnim(0.9f)
                        .size(24.dp))
            }
            }
        }
    }//Column
}//Scaffold
}

@Preview
@Composable
fun MainMenuPreview(){
    MainMenuScreen(rememberNavController())
}

@Composable
fun BottomBar(navController: NavHostController){
    val screens = listOf(
        Screen.LandmarksScreen,
        Screen.TriviaScreen,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(backgroundColor = Color.Transparent,
        elevation = 114.dp,
        modifier = Modifier.background(Brush.verticalGradient(
            listOf(
                Color(0xFF114981), Color(0xFF002B54)
            )
        ),)){

        screens.forEach {
            AddItem(screen = it,
                currentDestination = currentDestination,
                navController = navController )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: Screen,
    currentDestination: NavDestination?,
    navController: NavHostController,
    ){
        BottomNavigationItem(
            label = {
                Text(text = screen.title,
                    fontFamily = FontFamily(Font(R.font.bubblegum_sans)),
                    color = Color.White,
                    fontSize = 14.sp
                )
            },

            icon = {
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier.size(50.dp)){
                Image(
                    modifier = Modifier.size(40.dp),
                    painter = painterResource(id = screen.icon),
                    contentDescription = null
                )}
            },

            selected = currentDestination?.hierarchy?.any{
                it.route == screen.route
            } == true,

            onClick = {
                navController.navigate(screen.route)
            },
            alwaysShowLabel = true,
            selectedContentColor = Color(0xFF0C4DB2)

        )
}