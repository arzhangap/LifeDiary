package com.arzhang.lifediary.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arzhang.lifediary.presentation.components.DisplayAlertDialog
import com.arzhang.lifediary.presentation.screens.auth.AuthenticationScreen
import com.arzhang.lifediary.presentation.screens.auth.AuthenticationViewModel
import com.arzhang.lifediary.presentation.screens.home.HomeScreen
import com.arzhang.lifediary.util.Constants.APP_ID
import com.arzhang.lifediary.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetUpNavGraph(
    startDestination: String,
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = startDestination) {
        authenticationRoute(
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            }
        )
        homeRoute(
            navigateToWrite = {
                navController.navigate(Screen.Write.route)
            },
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route)
            }
        )
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit
) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        val loggedInState by viewModel.loggedInState
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        AuthenticationScreen(
            oneTapSignInState = oneTapState,
            loadingState = loadingState,
            messageBarState = messageBarState,
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
            onTokenReceived = {tokenId->
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
    navigateToAuth: () -> Unit
) {
    composable(route = Screen.Home.route) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember {
            mutableStateOf(false)
        }
        val scope = rememberCoroutineScope()
        HomeScreen(
            navigateToWrite = navigateToWrite,
            drawerState = drawerState,
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }},
            onSignOutClicked = {signOutDialogOpened = true}
        )
        DisplayAlertDialog(
            dialogOpened = signOutDialogOpened,
            onDismissRequest = { signOutDialogOpened = false },
            onConfirmation = {
                             scope.launch(Dispatchers.IO) {
                                 val user = App.create(APP_ID).currentUser
                                 if(user != null) {
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
    }
}
fun NavGraphBuilder.writeRoute() {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {

    }
}