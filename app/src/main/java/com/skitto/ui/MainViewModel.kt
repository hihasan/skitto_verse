package com.skitto.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skitto.constants.ApplicationConstants
import com.skitto.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel() {
    var startDestination by mutableStateOf(Route.SplashNavigation.route)

    init {
        viewModelScope.launch {
//            delay(ApplicationConstants.APP_LOAD_TIME)
//            startDestination = Route.CAMERA_NAVIGATION.route
        }
    }
}