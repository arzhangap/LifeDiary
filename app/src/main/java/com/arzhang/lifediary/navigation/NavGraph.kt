package com.arzhang.lifediary.navigation

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arzhang.lifediary.model.Mood
import com.arzhang.lifediary.presentation.components.DisplayAlertDialog
import com.arzhang.lifediary.presentation.screens.auth.AuthenticationScreen
import com.arzhang.lifediary.presentation.screens.auth.AuthenticationViewModel
import com.arzhang.lifediary.presentation.screens.home.HomeScreen
import com.arzhang.lifediary.presentation.screens.home.HomeViewModel
import com.arzhang.lifediary.presentation.screens.write.WriteScreen
import com.arzhang.lifediary.presentation.screens.write.WriteViewModel
import com.arzhang.lifediary.util.Constants.APP_ID
import com.arzhang.lifediary.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.arzhang.lifediary.model.RequestState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetUpNavGraph(
    startDestination: String,
    navController: NavHostController,
    onDataLoaded: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authenticationRoute(
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            },
            onDataLoaded = onDataLoaded
        )
        homeRoute(
            navigateToWrite = {
                navController.navigate(Screen.Write.route)
            },
            navigateToWriteWithArgs = {
                navController.navigate(Screen.Write.passDiaryId(diaryId = it))
            },
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route)
            },
            onDataLoaded = onDataLoaded
        )
        writeRoute(onBackPressed = {
            navController.popBackStack()
        })
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit,
    onDataLoaded: () -> Unit
) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        val loggedInState by viewModel.loggedInState
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        LaunchedEffect(key1 = loadingState) {
            onDataLoaded()
        }
        AuthenticationScreen(
            oneTapSignInState = oneTapState,
            loadingState = loadingState,
            messageBarState = messageBarState,
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
            onSuccessfulFirebaseSignIn = { tokenId ->
                viewModel.signInWithMongoAtlas(
                    tokenId = tokenId,
                    onSuccess = {
                        messageBarState.addSuccess("ورود موفق")
                        viewModel.setLoading(false)
                    },
                    onError = {
                        messageBarState.addError(it)
                        viewModel.setLoading(false)
                    }
                )
            },
            onFailedFirebaseSignIn = {
                messageBarState.addError(it)
                viewModel.setLoading(false)
            },
            onDialogDismissed = {
                messageBarState.addError(Exception("ورود ناموفق"))
                viewModel.setLoading(false)
            },
            loggedInState = loggedInState,
            navigateToHome = navigateToHome
        )
    }
}

fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
    navigateToAuth: () -> Unit,
    onDataLoaded: () -> Unit
) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = hiltViewModel()
        val diaries by viewModel.diaries
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember {mutableStateOf(false) }
        var deleteAllDialogOpened by remember {mutableStateOf(false)}
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        LaunchedEffect(key1 = diaries) {
            if (diaries !is RequestState.Loading) {
                onDataLoaded()
            }
        }
        HomeScreen(
            diaries = diaries,
            drawerState = drawerState,
            onSignOutClicked = { signOutDialogOpened = true },
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            onDeleteAllClicked = {
                                 deleteAllDialogOpened = true
            },
            navigateToWrite = navigateToWrite,
            navigateToWriteWithArgs = navigateToWriteWithArgs,
        )

        DisplayAlertDialog(
            dialogOpened = signOutDialogOpened,
            onDismissRequest = { signOutDialogOpened = false },
            onConfirmation = {
                scope.launch(Dispatchers.IO) {
                    val user = App.create(APP_ID).currentUser
                    if (user != null) {
                        user.logOut()
                        signOutDialogOpened = false
                        withContext(Dispatchers.Main) {
                            navigateToAuth()
                        }
                    }
                }
            },
            dialogTitle = "خارج شدن از اکانت",
            dialogText = "آیا مطمئن هستید که می خواهید از اکانت خود خارج شوید؟",
            icon = Icons.Default.Warning
        )
        DisplayAlertDialog(
            dialogTitle = "حذف تمام نوشته ها",
            dialogText = "آیا مطمئن هستید که می خواهید تمام نوشته ها را پاک کنید؟",
            dialogOpened = deleteAllDialogOpened,
            onDismissRequest = { deleteAllDialogOpened = false },
            onConfirmation = {
                            deleteAllDialogOpened = false
                             viewModel.deleteAllDiaries(
                                 onSuccess = {
                                             Toast.makeText(
                                                 context,
                                                 "All Diaries Deleted",
                                                 Toast.LENGTH_SHORT
                                             ).show()
                                     scope.launch {
                                         drawerState.close()
                                     }
                                 },
                                 onError = {
                                     Toast.makeText(
                                         context,
                                         if(it.message == "No Internet connection.")
                                             "We need internet connection" else it.message,
                                         Toast.LENGTH_SHORT
                                     ).show()
                                     scope.launch {
                                         drawerState.close()
                                     }
                                 }
                             )
            },
           icon = Icons.Default.Warning
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.writeRoute(
    onBackPressed: () -> Unit
) {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {
        val context = LocalContext.current
        val viewModel: WriteViewModel = hiltViewModel()
        val uiState = viewModel.uiState
        val galleryState = viewModel.galleryState
        val pagerState = rememberPagerState { Mood.entries.size }
        val pageNumber by remember { derivedStateOf { pagerState.currentPage } }

        WriteScreen(
            uiState = uiState,
            galleryState = galleryState,
            onBackPressed = onBackPressed,
            onDeleteConfirmed = {
                viewModel.deleteDiary(
                    onSuccess = {
                        Toast.makeText(
                            context,
                            "Deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()
                    },
                    onError = { message ->
                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                )
            },
            onTitleChanged = { viewModel.setTitle(title = it) },
            onDescriptionChanged = { viewModel.setDescription(description = it) },
            pagerState = pagerState,
            moodName = { Mood.entries[pageNumber].name },
            onSaveClicked = {
                viewModel.upsertDiary(
                    diary = it.apply { mood = Mood.entries[pageNumber].name },
                    onSuccess = { onBackPressed() },
                    onError = {message ->
                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            },
            onDateAndTimeUpdated = { viewModel.updateDateAndTime(it) },
            onImageSelected = {uri ->
                val type = context.contentResolver.getType(uri)?.split("/")?.last() ?: "jpg"
                viewModel.addImage(
                    image = uri,
                    imageType =type
                )
            },
            onImageDeleteClicked = {
                galleryState.removeImage(it)
            },
        )
    }
}
