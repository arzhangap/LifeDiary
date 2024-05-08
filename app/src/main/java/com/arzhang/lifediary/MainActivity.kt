package com.arzhang.lifediary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.arzhang.lifediary.data.database.ImageToUploadDao
import com.arzhang.lifediary.navigation.Screen
import com.arzhang.lifediary.navigation.SetUpNavGraph
import com.arzhang.lifediary.ui.theme.LifeDiaryTheme
import com.arzhang.lifediary.util.Constants.APP_ID
import com.arzhang.lifediary.util.retryUploadingImagesToFirebase
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageToUploadDao: ImageToUploadDao
    private var keepSplashOpened = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            keepSplashOpened
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        FirebaseApp.initializeApp(this)
        setContent {
            LifeDiaryTheme {
                val navController = rememberNavController()
                SetUpNavGraph(
                    startDestination = calculateStartDestination(),
                    navController = navController,
                    onDataLoaded = {
                        keepSplashOpened = false
                    }
                )
            }
        }

        cleanupCheck(scope = lifecycleScope, imageToUploadDao = imageToUploadDao)
    }
}

private fun cleanupCheck(
    scope: CoroutineScope,
    imageToUploadDao: ImageToUploadDao
) {
    scope.launch(Dispatchers.IO) {
        val result = imageToUploadDao.getAllImages()
        result.forEach {imageToUpload ->
            retryUploadingImagesToFirebase(imageToUpload = imageToUpload) {
                scope.launch(Dispatchers.IO) {
                    imageToUploadDao.cleanupImage(imageId = imageToUpload.id)
                }
            }
        }
    }
}

fun calculateStartDestination(): String {
    val user = App.create(appId = APP_ID).currentUser
    return if (user != null && user.loggedIn) Screen.Home.route
    else Screen.Authentication.route
}
