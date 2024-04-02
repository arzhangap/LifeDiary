package com.arzhang.lifediary.presentation.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.arzhang.lifediary.util.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import java.lang.Exception

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    messageBarState: MessageBarState,
    oneTapSignInState: OneTapSignInState,
    onButtonClicked: () -> Unit
) {
    Scaffold(
        content = {
            ContentWithMessageBar(messageBarState = messageBarState) {
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
            Log.d("login", tokenId)
            messageBarState.addSuccess("با موفقیت وارد شدید")
        },
        onDialogDismissed = { message ->
            Log.d("login", message)
            messageBarState.addError(Exception("ورود ناموفق"))
        }
    )
}