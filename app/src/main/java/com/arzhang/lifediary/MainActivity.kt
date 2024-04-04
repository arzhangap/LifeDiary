package com.arzhang.lifediary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.arzhang.lifediary.navigation.Screen
import com.arzhang.lifediary.navigation.SetUpNavGraph
import com.arzhang.lifediary.ui.theme.LifeDiaryTheme
import com.arzhang.lifediary.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window,false)
        setContent {
            LifeDiaryTheme {
                val navController = rememberNavController()
                SetUpNavGraph(
                    startDestination = calculateStartDestination(),
                    navController = navController
                )
            }
        }
    }
}

fun calculateStartDestination() : String {
    val user = App.create(appId = APP_ID).currentUser
    return if(user != null && user.loggedIn) Screen.Home.route
    else Screen.Authentication.route
}
