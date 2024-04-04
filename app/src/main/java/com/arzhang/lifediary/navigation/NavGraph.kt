package com.arzhang.lifediary.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arzhang.lifediary.presentation.screens.auth.AuthenticationScreen
import com.arzhang.lifediary.presentation.screens.auth.AuthenticationViewModel
import com.arzhang.lifediary.util.Constants.APP_ID
import com.arzhang.lifediary.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

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
        homeRoute()
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
            },
            loggedInState = loggedInState,
            navigateToHome = navigateToHome
        )
    }
}
fun NavGraphBuilder.homeRoute() {
    composable(route = Screen.Home.route) {
        val scope = rememberCoroutineScope()
        Column(modifier = Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Button(onClick = {
                scope.launch(Dispatchers.IO) {
                    App.create(APP_ID).currentUser?.logOut()
                }
            }) {
                Text("خروج")
            }
        }
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