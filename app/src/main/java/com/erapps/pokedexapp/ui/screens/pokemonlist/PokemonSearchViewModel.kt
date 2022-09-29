package com.erapps.pokedexapp.ui.screens.pokemonlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erapps.pokedexapp.data.api.models.PokemonListResponse
import com.erapps.pokedexapp.data.source.SearchPokemonRepository
import com.erapps.pokedexapp.ui.shared.UiState
import com.erapps.pokedexapp.ui.shared.mapResultToUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonSearchViewModel @Inject constructor(
    private val repository: SearchPokemonRepository
): ViewModel() {

    private val _uiState = mutableStateOf<UiState?>(null)
    val uiState: State<UiState?> = _uiState

    init {
        getPokemons()
    }

    private fun getPokemons() = viewModelScope.launch {
        repository.getPokemons().collect { result ->
            mapResultToUiState(result, _uiState) { response ->
                _uiState.value = UiState.Success(response.results)
            }
        }
    }

    fun filterPokemonByName(name: String) = viewModelScope.launch {
        repository.getPokemons().collect { result ->
            mapResultToUiState(result, _uiState) { response ->

                val filteredPokemons = response.results.filter {
                    it.name.contains(name)
                }

                _uiState.value = UiState.Success(filteredPokemons)
            }
        }
    }
}