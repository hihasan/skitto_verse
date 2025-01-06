package com.skitto.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skitto.ui.camera.CameraScreen
import com.skitto.ui.home.HomeScreen
import com.skitto.ui.splash.SplashScreen

@Composable
fun NavGraph(
    startDestination : String,
    navController : NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = startDestination){
        composable(route = Route.SplashNavigation.route) {
            SplashScreen(navController)
        }

        composable(route = Route.HomeNavigation.route) {
            HomeScreen()
        }

        composable(route = Route.CAMERA_NAVIGATION.route) {
            CameraScreen()
        }
    }
}