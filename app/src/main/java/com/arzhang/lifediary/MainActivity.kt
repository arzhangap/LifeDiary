package com.arzhang.lifediary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.arzhang.lifediary.navigation.Screen
import com.arzhang.lifediary.navigation.SetUpNavGraph
import com.arzhang.lifediary.ui.theme.LifeDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            LifeDiaryTheme {
                val navController = rememberNavController()
                SetUpNavGraph(
                    startDestination = Screen.Authentication.route,
                    navController = navController
                )
            }
        }
    }
}
