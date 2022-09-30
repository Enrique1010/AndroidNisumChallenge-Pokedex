package com.erapps.pokedexapp.ui.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScreenWithMessage(
    modifier: Modifier = Modifier,
    message: Int
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = message),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun SnackBarInternet(snackbarHostState: SnackbarHostState) {
    SnackbarHost(
        modifier = Modifier.padding(16.dp),
        hostState = snackbarHostState
    ) { data ->
        Snackbar(
            action = {
                TextButton(
                    onClick = { snackbarHostState.currentSnackbarData?.dismiss() },
                ) {
                    Text(
                        text = data.actionLabel.toString(),
                        color = SnackbarDefaults.primaryActionColor
                    )
                }
            }
        ) {
            Text(text = data.message, color = MaterialTheme.colors.onBackground)
        }
    }
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: String?,
    errorStringResource: Int?
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        errorMessage?.let {
            Text(text = it, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            return@Column
        }
        errorStringResource?.let {
            Text(text = stringResource(id = it), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            return@Column
        }
    }
}