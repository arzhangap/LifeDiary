package com.arzhang.lifediary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arzhang.lifediary.presentation.screens.auth.AuthenticationScreen
import com.arzhang.lifediary.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.rememberOneTapSignInState

@Composable
fun SetUpNavGraph(
    startDestination: String,
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = startDestination) {
        authenticationRoute()
        homeScreenRoute()
        writeScreenRoute()
    }
}

fun NavGraphBuilder.authenticationRoute() {
    composable(route = Screen.Authentication.route) {
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        AuthenticationScreen(
            oneTapSignInState = oneTapState,
            loadingState = oneTapState.opened,
            messageBarState = messageBarState,
            onButtonClicked = {
                oneTapState.open()
            }
        )
    }
}
fun NavGraphBuilder.homeScreenRoute() {
    composable(route = Screen.Home.route) {

    }
}
fun NavGraphBuilder.writeScreenRoute() {
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