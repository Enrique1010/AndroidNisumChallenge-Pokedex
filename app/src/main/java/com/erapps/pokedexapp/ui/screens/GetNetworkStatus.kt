package com.erapps.pokedexapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.erapps.pokedexapp.ui.shared.NetworkState
import com.erapps.pokedexapp.ui.shared.connectivityState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun getNetworkStatus(): Boolean {
    val connection by connectivityState()
    return connection === NetworkState.Connected
}