package com.erapps.pokedexapp.domain.usecases.observeconnectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityMonitor {

    fun observe(): Flow<State>

    enum class State {
        Connected, Disconnected
    }
}