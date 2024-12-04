package com.andyslab.geobud.ui.nav

sealed class Screen(val route: String) {
    data object LandmarksQuizScreen : Screen(route = "landmarks_quiz")

    data object LoadingScreen : Screen(route = "loading")
}
