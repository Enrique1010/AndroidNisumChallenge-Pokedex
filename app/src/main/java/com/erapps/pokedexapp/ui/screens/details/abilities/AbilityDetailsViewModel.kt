package com.erapps.pokedexapp.ui.screens.details.abilities

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.Rgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erapps.pokedexapp.data.source.PokemonDetailsRepository
import com.erapps.pokedexapp.ui.shared.UiState
import com.erapps.pokedexapp.ui.shared.mapResultToUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class AbilityDetailsViewModel @Inject constructor(
    private val pokemonDetailsRepository: PokemonDetailsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = mutableStateOf<UiState?>(null)
    val uiState: State<UiState?> = _uiState

    val backGroundColor = mutableStateOf(Color.Transparent)

    init {
        savedStateHandle.get<String>("abilityUrl")?.let { url ->
            val decodeUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.toString())
            getAbilityDetails(decodeUrl)
        }
        savedStateHandle.get<Int>("abilityColorRGB")?.let {
            backGroundColor.value = Color(it)
        }
    }

    private fun getAbilityDetails(url: String) = viewModelScope.launch {
        pokemonDetailsRepository.getAbilityDetails(url).collect { result ->
            mapResultToUiState(result, _uiState) { response ->
                _uiState.value = UiState.Success(response)
            }
        }
    }
}