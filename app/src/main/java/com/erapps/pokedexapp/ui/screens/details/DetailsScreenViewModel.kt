package com.erapps.pokedexapp.ui.screens.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erapps.pokedexapp.data.source.PokemonDetailsRepository
import com.erapps.pokedexapp.ui.shared.UiState
import com.erapps.pokedexapp.ui.shared.mapResultToUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val pokemonDetailsRepository: PokemonDetailsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = mutableStateOf<UiState?>(null)
    val uiState: State<UiState?> = _uiState

    val backGroundColor = mutableStateOf(Color.Transparent)

    init {
        savedStateHandle.get<String>("pokemonId")?.let { pokemonId ->
            getPokemon(pokemonId)
        }
    }

    private fun getPokemon(pokemonId: String) = viewModelScope.launch {
        pokemonDetailsRepository.getPokemon(pokemonId).collect { result ->
            mapResultToUiState(result, _uiState) { pokemon ->
                _uiState.value = UiState.Success(pokemon)
            }
        }
    }

    //details methods

}