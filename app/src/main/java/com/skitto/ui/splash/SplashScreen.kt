package com.skitto.ui.splash

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.skitto.R
import com.skitto.navigation.Route

@Composable
fun SplashScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.iv_bg),
            contentDescription = null, // Replace with a meaningful description if necessary
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Adjust as needed (Crop, Fit, etc.)
        )



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.wc_txt),
                    contentDescription = null, // Replace with a meaningful description if necessary
                    modifier = Modifier
                        .width(193.dp)
                        .height(27.dp),
                    contentScale = ContentScale.Crop // Adjust as needed (Crop, Fit, etc.)
                )

                Image(
                    painter = painterResource(id = R.drawable.wc_creative),
                    contentDescription = null, // Replace with a meaningful description if necessary
                    modifier = Modifier
                        .width(398.dp)
                        .height(252.dp),
                    contentScale = ContentScale.Crop // Adjust as needed (Crop, Fit, etc.)
                )

                Image(
                    painter = painterResource(id = R.drawable.ctn_btn),
                    contentDescription = null, // Replace with a meaningful description if necessary
                    modifier = Modifier
                        .width(312.dp)
                        .height(70.dp)
                        .padding(top = 24.dp)
                        .clickable {
                            navController.navigate(Route.CAMERA_NAVIGATION.route)
                        },
                    contentScale = ContentScale.Crop // Adjust as needed (Crop, Fit, etc.)
                )


            }

        }
    }
}
