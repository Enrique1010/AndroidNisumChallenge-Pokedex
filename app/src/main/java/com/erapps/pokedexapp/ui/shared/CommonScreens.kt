package com.erapps.pokedexapp.ui.shared

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.erapps.pokedexapp.R
import kotlinx.coroutines.ExperimentalCoroutinesApi

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
            fontSize = dimensionResource(id = R.dimen.common_screen_font_size).value.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ScreenWithMessage(
    modifier: Modifier = Modifier,
    message: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = message),
            fontSize = dimensionResource(id = R.dimen.common_screen_font_size).value.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        OutlinedButton(onClick = onClick) {
            Text(text = stringResource(id = R.string.try_again_btn_text))
        }
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
fun AnimatedSnackBar(
    modifier: Modifier = Modifier,
    isConnected: Boolean,
    snackbarHostState: SnackbarHostState
) {
    SnackbarHost(
        modifier = modifier,
        hostState = snackbarHostState
    ) { snackbarData ->
        AnimatedVisibility(
            visible = isConnected,
            enter = slideInVertically() + expandVertically(),
            exit = slideOutVertically() + shrinkVertically()
        ) {
            Snackbar {
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = snackbarData.message,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
fun connectivityState(): androidx.compose.runtime.State<NetworkState> {
    val context = LocalContext.current

    return produceState(initialValue = context.currentConnectivityNetworkState) {
        context.observeConnectivityAsFlow().collect { value = it }
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
            Text(
                text = it,
                fontSize = dimensionResource(id = R.dimen.common_screen_font_size).value.sp,
                fontWeight = FontWeight.Bold
            )
            return@Column
        }
        errorStringResource?.let {
            Text(
                text = stringResource(id = it),
                fontSize = dimensionResource(id = R.dimen.common_screen_font_size).value.sp,
                fontWeight = FontWeight.Bold
            )
            return@Column
        }
    }
}