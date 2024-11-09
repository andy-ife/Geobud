package com.andyslab.geobud.ui.loading

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andyslab.geobud.ui.Screen
import com.andyslab.geobud.ui.landmarkquiz.LandmarkQuizScreen
import com.andyslab.geobud.ui.mainmenu.MainMenuScreen

@Composable
fun RootNavGraph() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController , route = Graph.ROOT, startDestination = Screen.LoadingScreen.route,){
        composable(route = Screen.LoadingScreen.route){
            LoadingScreen(navController)
        }
        composable(route = Screen.MainMenuScreen.route){
            MainMenuScreen(navController)
        }
        composable(route = Screen.LandmarksQuizScreen.route){
            LandmarkQuizScreen(navController)
        }
    }
}

object Graph{
    const val ROOT = "root_graph"
    const val MAIN_MENU = "main_menu_graph"
    const val LANDMARKS = "landmarks_graph"
}