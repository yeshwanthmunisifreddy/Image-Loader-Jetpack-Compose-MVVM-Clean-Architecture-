package com.thesubgraph.wallpaper

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.thesubgraph.wallpaper.ui.theme.WallpaperTheme
import com.thesubgraph.wallpaper.view.common.DestinationRouteProtocol
import com.thesubgraph.wallpaper.view.common.Router
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WallpaperTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WallpaperApp(
                        applicationContext = applicationContext,
                        mainActivity = this@MainActivity
                    )
                }
            }
        }
    }
}

@Composable
fun WallpaperApp(applicationContext: Context, mainActivity: MainActivity) {
    val navController = rememberNavController()
    val router = Router(
        context = applicationContext,
        navController = navController,
        activity = mainActivity,
    )
    router.ComposeRouter(DestinationRouteProtocol.Destination.Home.route)
}

