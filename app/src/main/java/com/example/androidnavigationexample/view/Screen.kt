package com.example.androidnavigationexample.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object Search : Screen("search")
    object Detail : Screen("detail/{movieId}") {
        fun createRoute(movieId: Int) = "detail/$movieId"
    }
}

@Composable
fun MovieNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Search.route
    ) {
        composable(Screen.Search.route) {
            MovieSearchScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Detail.createRoute(movieId))
                }
            )
        }
        
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.StringType
                }
            )
        ) {
            MovieDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}