package com.andyslab.geobud.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andyslab.geobud.ui.Screen
import com.andyslab.geobud.ui.quiz.QuizScreen
import com.andyslab.geobud.ui.menu.MenuScreen

@Composable
fun RootNavGraph() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController , route = Graph.ROOT, startDestination = Screen.LoadingScreen.route,){
        composable(route = Screen.LoadingScreen.route){
            MenuScreen(navController)
        }
        composable(route = Screen.LandmarksQuizScreen.route){
            QuizScreen(navController)
        }
    }
}

object Graph{
    const val ROOT = "root_graph"
}