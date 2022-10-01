package com.erapps.pokedexapp.ui.shared

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/**
 * Network utility to get current state of internet connection
 * credits to: Shreyas Patil
 * modified by: Luis Enrique Ramirez
 * source: https://medium.com/scalereal/observing-live-connectivity-status-in-jetpack-compose-way-f849ce8431c7
 */
val Context.currentConnectivityNetworkState: NetworkState
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getCurrentConnectivityState(connectivityManager)
    }

private fun getCurrentConnectivityState(
    connectivityManager: ConnectivityManager
): NetworkState {
    val network = connectivityManager.activeNetwork
    val connected = connectivityManager.getNetworkCapabilities(network)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        ?: false

    return if (connected) NetworkState.Connected else NetworkState.Disconnected
}

@ExperimentalCoroutinesApi
fun Context.observeConnectivityAsFlow() = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callback = networkCallBack { connectionState -> trySend(connectionState) }

    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    connectivityManager.registerNetworkCallback(networkRequest, callback)

    val currentState = getCurrentConnectivityState(connectivityManager)
    trySend(currentState)

    awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
}

fun networkCallBack(callback: (NetworkState) -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(NetworkState.Connected)
        }

        override fun onLost(network: Network) {
            callback(NetworkState.Disconnected)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            callback(NetworkState.Disconnected)
        }
    }
}