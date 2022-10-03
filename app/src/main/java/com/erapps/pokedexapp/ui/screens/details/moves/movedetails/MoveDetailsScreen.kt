package com.erapps.pokedexapp.ui.screens.details.moves.movedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.erapps.pokedexapp.data.api.models.Pokemon
import com.erapps.pokedexapp.ui.shared.BackButtonBar
import com.erapps.pokedexapp.ui.shared.ErrorScreen
import com.erapps.pokedexapp.ui.shared.LoadingScreen
import com.erapps.pokedexapp.ui.shared.UiState

@Composable
fun MoveDetailsScreen(
    viewModel: MoveDetailsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {

    val uiState = viewModel.uiState.value
    val previousBackGroundColor = viewModel.backGroundColor.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colors.surface,
                        previousBackGroundColor,
                        MaterialTheme.colors.surface
                    )
                )
            )
    ) {
        when (uiState) {
            UiState.Loading -> {
                LoadingScreen()
            }
            is UiState.Error -> {
                ErrorScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                        .padding(8.dp)
                        .align(Alignment.TopCenter),
                    errorMessage = uiState.errorMessage,
                    errorStringResource = uiState.errorStringResource
                )
            }
            is UiState.Success<*> -> {

            }
            else -> {}
        }
        BackButtonBar(onBackPressed = onBackPressed)
    }

}