package com.arzhang.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arzhang.ui.components.GoogleButton
import com.arzhang.util.JustifiedRTLText

@Composable
internal fun AuthenticationContent(
    loadingState: Boolean = false,
    onButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(weight = 10f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(id = com.arzhang.ui.R.drawable.logo),
                contentDescription = "Logo",
            )
            JustifiedRTLText(
                textCompose = {
                    Text(
                        text = stringResource(id = R.string.auth_title),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        text = stringResource(id = R.string.auth_subtitle),
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        textAlign = TextAlign.Center
                    )
                })
        }
        Column(
            modifier = Modifier.weight(weight = 2f),
            verticalArrangement = Arrangement.Bottom
        ) {
            GoogleButton(
                loadingState = loadingState,
                onClick = onButtonClicked
            )
        }
    }
}