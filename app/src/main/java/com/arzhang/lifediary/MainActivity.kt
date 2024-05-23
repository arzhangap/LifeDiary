package com.arzhang.lifediary

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.arzhang.lifediary.navigation.SetUpNavGraph
import com.arzhang.mongo.database.ImageToDeleteDao
import com.arzhang.mongo.database.ImageToUploadDao
import com.arzhang.mongo.database.entity.ImageToDelete
import com.arzhang.mongo.database.entity.ImageToUpload
import com.arzhang.ui.theme.LifeDiaryTheme
import com.arzhang.util.Constants.APP_ID
import com.arzhang.util.Screen
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storageMetadata
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
    @Inject
    lateinit var imageToDeleteDao: ImageToDeleteDao
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

        cleanupCheck(scope = lifecycleScope, imageToUploadDao = imageToUploadDao, imageToDeleteDao = imageToDeleteDao)
    }
}

private fun cleanupCheck(
    scope: CoroutineScope,
    imageToUploadDao: ImageToUploadDao,
    imageToDeleteDao: ImageToDeleteDao
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
        val deleteResult = imageToDeleteDao.getAllImages()
        deleteResult.forEach {imageToDelete ->
            retryDeletingImagesToFirebase(imageToDelete = imageToDelete) {
                scope.launch(Dispatchers.IO) {
                    imageToDeleteDao.cleanUpImage(imageId = imageToDelete.id)
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


fun retryUploadingImagesToFirebase(
    imageToUpload: ImageToUpload,
    onSuccess: () -> Unit
) {
    val storage = FirebaseStorage.getInstance().reference
    storage.child(imageToUpload.remoteImagePath).putFile(
        imageToUpload.imageUri.toUri(),
        storageMetadata {  },
        imageToUpload.sessionUri.toUri()
    ).addOnSuccessListener { onSuccess() }
}
fun retryDeletingImagesToFirebase(
    imageToDelete: ImageToDelete,
    onSuccess: () -> Unit
) {
    val storage = FirebaseStorage.getInstance().reference
    storage.child(imageToDelete.remoteImagePath).delete(
    ).addOnSuccessListener { onSuccess() }
}