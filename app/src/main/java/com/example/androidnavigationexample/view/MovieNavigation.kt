package com.example.androidnavigationexample.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable


sealed class Screen {
    @Serializable
    data object Home

    @Serializable
    data class Detail(val movieId: Int)
}

@Composable
fun MovieNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            MovieSearchScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Detail(movieId))
                }
            )
        }

        composable<Screen.Detail> {
            MovieDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}