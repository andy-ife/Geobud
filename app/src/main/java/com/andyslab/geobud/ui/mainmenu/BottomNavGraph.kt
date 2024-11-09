package com.andyslab.geobud.ui.mainmenu

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.andyslab.geobud.ui.Screen

import com.andyslab.geobud.ui.landmarkpacks.LandmarkPacksScreen
import com.andyslab.geobud.ui.trivia.TriviaScreen


@Composable
fun BottomNavGraph(mainMenuNavController: NavHostController ,bottomNavController: NavHostController){
    NavHost(navController = bottomNavController, startDestination = Screen.LandmarksScreen.route){
        composable(route = Screen.LandmarksScreen.route){
            LandmarkPacksScreen(mainMenuNavController)
        }

        composable(route = Screen.TriviaScreen.route){
            TriviaScreen()
        }

    }
}




