package com.andyslab.geobud.ui.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andyslab.geobud.ui.menu.MenuScreen
import com.andyslab.geobud.ui.quiz.QuizScreen

@Composable
fun RootNavGraph(darkMode: Boolean?) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, route = Graph.ROOT, startDestination = Screen.LoadingScreen.route) {
        composable(route = Screen.LoadingScreen.route) {
            MenuScreen(navController, darkMode)
        }
        composable(
            route = Screen.LandmarksQuizScreen.route,
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) },
            exitTransition = { slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) },
            popExitTransition = { slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) },
        ) {
            QuizScreen(navController)
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
}
