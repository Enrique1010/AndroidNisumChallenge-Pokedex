package com.erapps.pokedexapp.ui.screens.pokemonlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erapps.pokedexapp.data.source.SearchPokemonRepository
import com.erapps.pokedexapp.ui.shared.UiState
import com.erapps.pokedexapp.ui.shared.mapResultToUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonSearchViewModel @Inject constructor(
    private val repository: SearchPokemonRepository
) : ViewModel() {

    private val _uiState = mutableStateOf<UiState?>(null)
    val uiState: State<UiState?> = _uiState

    private val _isEmptyList = mutableStateOf(false)
    val isEmptyList: State<Boolean> = _isEmptyList

    val text = mutableStateOf("")
    val focused = mutableStateOf(false)

    init {
        filterPokemonsByName("")
    }

    fun filterPokemonsByName(name: String) = viewModelScope.launch {
        repository.getPokemons().collect { result ->
            mapResultToUiState(result, _uiState) { response ->

                //return screen with try again when no internet and no data
                if (response.isEmpty()) {
                    _uiState.value = null
                    _isEmptyList.value = true
                    return@mapResultToUiState
                }

                val filteredPokemons = response.filter {
                    it.name.contains(name)
                }

                //return empty screen when no filtered results
                if (filteredPokemons.isEmpty()) {
                    _uiState.value = UiState.Empty
                    _isEmptyList.value = true
                    return@mapResultToUiState
                }

                _uiState.value = UiState.Success(filteredPokemons)
            }
        }
    }
}