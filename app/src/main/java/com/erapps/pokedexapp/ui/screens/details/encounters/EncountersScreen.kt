package com.erapps.pokedexapp.ui.screens.details.encounters

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.erapps.pokedexapp.R
import com.erapps.pokedexapp.data.api.models.encounters.PokemonEncounters
import com.erapps.pokedexapp.data.api.models.encounters.PokemonEncountersItem
import com.erapps.pokedexapp.ui.screens.getNetworkStatus
import com.erapps.pokedexapp.ui.shared.*
import com.erapps.pokedexapp.utils.makeGoodTitle

@Composable
fun EncountersScreen(
    viewModel: EncountersViewModel = hiltViewModel(),
    onLocationClick: (String, Int) -> Unit,
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
                LoadingScreen(modifier = Modifier.background(MaterialTheme.colors.surface))
            }
            is UiState.Error -> {
                ErrorScreen(
                    errorMessage = uiState.errorMessage,
                    errorStringResource = uiState.errorStringResource
                )
            }
            is UiState.Empty -> {
                ScreenWithMessage(message = R.string.this_pokemon_cannot_be_found)
            }
            is UiState.Success<*> -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.dimen_8dp))
                        .background(MaterialTheme.colors.surface),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val data = uiState.data as PokemonEncounters
                    Text(
                        text = stringResource(id = R.string.encounters_title),
                        fontSize = dimensionResource(id = R.dimen.ability_details_title).value.sp,
                        fontWeight = FontWeight.Bold,
                        color = previousBackGroundColor
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_16dp)))
                    EncountersScreenContent(
                        encountersList = data,
                        onLocationClick = onLocationClick,
                        color = previousBackGroundColor
                    )
                }
            }
            else -> {}
        }
        BackButtonBar(onBackPressed = onBackPressed)
    }
}

@Composable
private fun EncountersScreenContent(
    modifier: Modifier = Modifier,
    encountersList: PokemonEncounters,
    color: Color,
    onLocationClick: (String, Int) -> Unit
) {

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(encountersList) { pokemonEncountersItem ->
            EncountersItem(
                item = pokemonEncountersItem,
                onLocationClick = onLocationClick,
                color = color
            )
        }
    }
}

@Composable
private fun EncountersItem(
    modifier: Modifier = Modifier,
    item: PokemonEncountersItem,
    color: Color,
    onLocationClick: (String, Int) -> Unit
) {
    val context = LocalContext.current
    val status = getNetworkStatus()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = dimensionResource(id = R.dimen.dimen_8dp),
                horizontal = dimensionResource(id = R.dimen.dimen_8dp)
            )
            .clickable {
                if (!status) {
                    Toast
                        .makeText(
                            context,
                            R.string.cant_see_details_without_internet_text,
                            Toast.LENGTH_SHORT
                        )
                        .show()
                    return@clickable
                }
                onLocationClick(item.location_area.url, color.toArgb())
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = modifier
                .padding(vertical = dimensionResource(id = R.dimen.dimen_8dp))
                .fillMaxWidth(.9f),
            text = item.location_area.name.makeGoodTitle(),
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Icon(
            modifier = modifier.clickable {
                if (!status) {
                    Toast
                        .makeText(
                            context,
                            R.string.cant_see_details_without_internet_text,
                            Toast.LENGTH_SHORT
                        )
                        .show()
                    return@clickable
                }
                onLocationClick(item.location_area.url, color.toArgb())
            },
            imageVector = Icons.Default.NavigateNext,
            contentDescription = null
        )
    }
}
