package com.arzhang.lifediary.presentation.screens.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.arzhang.lifediary.util.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    loggedInState: Boolean,
    messageBarState: MessageBarState,
    oneTapSignInState: OneTapSignInState,
    onButtonClicked: () -> Unit,
    onTokenReceived: (String) -> Unit,
    onDialogDismissed: (String) -> Unit,
    navigateToHome: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            // to set color correctly on bar ( needed because of padding)
            .background(MaterialTheme.colorScheme.surface)
            // set paddings to avoid overlap of transparent bar
            .statusBarsPadding()
            .navigationBarsPadding()
        ,
        content = {
            ContentWithMessageBar(messageBarState = messageBarState, successMaxLines = 2) {
                AuthenticationContent(
                    loadingState = loadingState,
                    onButtonClicked = onButtonClicked
                )
            }
        }
    )
    OneTapSignInWithGoogle(
        state = oneTapSignInState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            onTokenReceived(tokenId)
        },
        onDialogDismissed = { message ->
            onDialogDismissed(message)
        }
    )

    LaunchedEffect(key1 = loggedInState) {
        if(loggedInState) navigateToHome()
    }
}