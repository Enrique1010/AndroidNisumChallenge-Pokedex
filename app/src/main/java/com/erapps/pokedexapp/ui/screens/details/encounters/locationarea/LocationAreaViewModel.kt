package com.erapps.pokedexapp.ui.screens.details.encounters.locationarea

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
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class LocationAreaViewModel @Inject constructor(
    private val repository: PokemonDetailsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = mutableStateOf<UiState?>(null)
    val uiState: State<UiState?> = _uiState

    val backGroundColor = mutableStateOf(Color.Transparent)

    init {
        savedStateHandle.get<String>("locationAreaUrl")?.let { url ->
            val decodeUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.toString())
            getLocationAreaDetails(decodeUrl)
        }
        savedStateHandle.get<Int>("locationAreaColorRGB")?.let {
            backGroundColor.value = Color(it)
        }
    }

    private fun getLocationAreaDetails(url: String) = viewModelScope.launch {
        repository.getLocationAreas(url).collect { result ->
            mapResultToUiState(result, _uiState) { response ->

                _uiState.value = UiState.Success(response)
            }
        }
    }
}