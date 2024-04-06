package com.arzhang.lifediary.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.arzhang.lifediary.util.JustifiedRTLText

@Composable
fun DisplayAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogOpened: Boolean,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    if (dialogOpened) {
        JustifiedRTLText(textCompose = {
        AlertDialog(
            icon = {
                Icon(icon, contentDescription = "Icon")
            },
            title = {
                Text(text = dialogTitle)
            },
            text = {
                Text(text = dialogText)
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text("تایید")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("منصرف شدم")
                }
            }
        )
        })
    }
}