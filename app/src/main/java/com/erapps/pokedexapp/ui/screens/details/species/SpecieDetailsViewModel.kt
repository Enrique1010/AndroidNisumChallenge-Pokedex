package com.erapps.pokedexapp.ui.screens.details.species

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erapps.pokedexapp.data.Result
import com.erapps.pokedexapp.data.api.models.species.evolutionchain.EvolutionChainDetails
import com.erapps.pokedexapp.data.source.PokemonDetailsRepository
import com.erapps.pokedexapp.ui.shared.UiState
import com.erapps.pokedexapp.ui.shared.mapResultToUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class SpecieDetailsViewModel @Inject constructor(
    private val repository: PokemonDetailsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = mutableStateOf<UiState?>(null)
    val uiState: State<UiState?> = _uiState

    private val _evolutionChain = mutableStateOf<EvolutionChainDetails?>(null)
    val evolutionChain: State<EvolutionChainDetails?> = _evolutionChain

    val backGroundColor = mutableStateOf(Color.Transparent)

    init {
        savedStateHandle.get<String>("specieUrl")?.let { url ->
            val decodeUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.toString())
            getPokemonSpecieDetails(decodeUrl)
        }
        savedStateHandle.get<Int>("specieColorRGB")?.let {
            backGroundColor.value = Color(it)
        }
    }

    private fun getPokemonSpecieDetails(url: String) = viewModelScope.launch {
        repository.getPokemonSpecieDetails(url).collect { result ->
            mapResultToUiState(result, _uiState) { response ->
                _uiState.value = UiState.Success(response)
            }
        }
    }

    fun getEvolutionChain(url: String) = viewModelScope.launch {
        repository.getEvolutionChain(url).collect { result ->
            when(result) {
                is Result.Error -> {
                    _evolutionChain.value = null
                }
                is Result.Success -> {
                    _evolutionChain.value = result.data
                }
                else -> {
                    _evolutionChain.value = null
                }
            }
        }
    }
}