package com.skitto.ui.camera

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.skitto.ui.player.YoutubePlayer

@Composable
fun CameraScreen() {
    var isRedSurfaceDetected by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val context = LocalContext.current

    BackHandler {

    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Camera preview
        CameraPreview { detected ->
            isRedSurfaceDetected = detected
        }

        // YouTube video overlay
        if (isRedSurfaceDetected) {

            Box(modifier = Modifier.align(Alignment.Center)){
                YoutubePlayer(
                    youtubeVideoId = "1ayGY7s1xuM",
                    lifecycleOwner = LocalLifecycleOwner.current,
                )
            }

        }
    }


}

