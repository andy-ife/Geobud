package com.andyslab.geobud.ui

import androidx.annotation.DrawableRes
import com.andyslab.geobud.R

 sealed class Screen (val route: String,
                      val title: String,
                      @DrawableRes val icon: Int,){

     data object LandmarksScreen: Screen(
         route = "landmarks",
         title = "Landmarks",
         icon = R.drawable.eiffel_tower)

     data object LandmarksQuizScreen: Screen(
         route = "landmarks_quiz",
         title = "Landmarks Quiz",
         icon = 0
     )

     data object TriviaScreen: Screen(
         route = "trivia",
         title = "Trivia",
         icon = R.drawable.globe)

     data object LoadingScreen: Screen(
         route = "loading",
         title = "Loading",
         icon = 0
     )

     data object MainMenuScreen: Screen(
         route = "main_menu",
         title = "Main Menu",
         icon = 0
     )
 }