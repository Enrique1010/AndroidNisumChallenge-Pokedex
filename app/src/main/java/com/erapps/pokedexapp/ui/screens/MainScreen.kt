package com.erapps.pokedexapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.erapps.pokedexapp.R
import com.erapps.pokedexapp.ui.navigation.MainNavigation
import com.erapps.pokedexapp.ui.shared.AnimatedSnackBar
import com.erapps.pokedexapp.ui.theme.PokedexAppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun MainScreen() {
    val snackbarHostState = remember { SnackbarHostState() }

    PokedexAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
                snackbarHost = { ShowNetworkStatusSnackBar(snackbarHostState = it) }
            ) {
                MainNavigation()
            }
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
fun ShowNetworkStatusSnackBar(snackbarHostState: SnackbarHostState) {

    val scope = rememberCoroutineScope()

    if (getNetworkStatus()) {
        snackbarHostState.currentSnackbarData?.dismiss()

    } else {
        val message = stringResource(id = R.string.error_no_internet)
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "",
                duration = SnackbarDuration.Indefinite
            )
        }

        AnimatedSnackBar(
            isConnected = true,
            snackbarHostState = snackbarHostState
        )
    }
}