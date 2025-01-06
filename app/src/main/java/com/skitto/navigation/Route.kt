package com.skitto.navigation

import com.skitto.constants.NavigationConstants

sealed class Route(val route : String) {
    data object SplashNavigation : Route(route = NavigationConstants.SPLASH_NAVIGATION)
    data object HomeNavigation : Route(route = NavigationConstants.HOME_NAVIGATION)
    data object CAMERA_NAVIGATION : Route(route = NavigationConstants.CAMERA_NAVIGATION)
}