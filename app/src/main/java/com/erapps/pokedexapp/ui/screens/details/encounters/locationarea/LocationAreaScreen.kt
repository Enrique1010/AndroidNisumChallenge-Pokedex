package com.erapps.pokedexapp.ui.screens.details.encounters.locationarea

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.erapps.pokedexapp.R
import com.erapps.pokedexapp.data.api.models.encounters.locationarea.EncounterMethodRate
import com.erapps.pokedexapp.data.api.models.encounters.locationarea.LocationAreaDetails
import com.erapps.pokedexapp.ui.shared.*
import com.erapps.pokedexapp.utils.Constants
import com.erapps.pokedexapp.utils.makeGoodTitle

@Composable
fun LocationAreaScreen(
    viewModel: LocationAreaViewModel = hiltViewModel(),
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
                    val data = uiState.data as LocationAreaDetails
                    Text(
                        text = stringResource(id = R.string.location_area_title),
                        fontSize = dimensionResource(id = R.dimen.ability_details_title).value.sp,
                        fontWeight = FontWeight.Bold,
                        color = previousBackGroundColor
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_16dp)))
                    LocationAreaScreenContent(locationArea = data, color = previousBackGroundColor)
                }
            }
            else -> {}
        }
        BackButtonBar(onBackPressed = onBackPressed)
    }
}

@Composable
fun LocationAreaScreenContent(
    modifier: Modifier = Modifier,
    locationArea: LocationAreaDetails,
    color: Color
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        LocationAreaNameSection(locationArea = locationArea.name.makeGoodTitle(), color = color)
        LocationAreaTableSection(
            encounterMethodRates = locationArea.encounter_method_rates,
            color = color
        )
    }

}

@Composable
fun LocationAreaNameSection(
    modifier: Modifier = Modifier,
    locationArea: String,
    color: Color
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.dimen_16dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = Icons.Default.Map, contentDescription = null, tint = color)
        Spacer(modifier = modifier.width(dimensionResource(id = R.dimen.dimen_8dp)))
        Text(
            text = locationArea,
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LocationAreaTableSection(
    modifier: Modifier = Modifier,
    encounterMethodRates: List<EncounterMethodRate>,
    color: Color
) {

    Column(
        modifier = modifier.padding(
            horizontal = dimensionResource(id = R.dimen.dimen_16dp),
            vertical = dimensionResource(id = R.dimen.dimen_8dp)
        )
    ) {
        //header
        Row(
            modifier = modifier.background(color)
        ) {
            AnnotatedStringTableCell(
                text = stringResource(id = R.string.encounter_method_label),
                weight = Constants.COLUMN1WEIGHT,
                color = MaterialTheme.colors.onBackground
            )
            AnnotatedStringTableCell(
                text = stringResource(id = R.string.encounter_rate_label),
                weight = Constants.COLUMN2WEIGHT,
                color = MaterialTheme.colors.onBackground
            )
        }
        //table
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            LazyColumn(
                modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.dimen_4dp))
            ) {
                items(encounterMethodRates) { methodRates ->
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnnotatedStringTableCell(
                            text = methodRates.encounter_method.name.makeGoodTitle(),
                            weight = Constants.COLUMN1WEIGHT,
                            color = color
                        )
                        TableCell(
                            text = "${methodRates.version_details[0].rate}%",
                            weight = Constants.COLUMN2WEIGHT
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.TableCell(
    text: String,
    weight: Float,
) {
    Text(
        text = text,
        modifier = Modifier
            .border(dimensionResource(id = R.dimen.basic_border), MaterialTheme.colors.surface)
            .weight(weight)
            .padding(dimensionResource(id = R.dimen.dimen_8dp)),
        color = MaterialTheme.colors.onBackground
    )
}

@Composable
private fun RowScope.AnnotatedStringTableCell(
    modifier: Modifier = Modifier,
    text: String,
    weight: Float,
    color: Color
) {
    Text(
        text = buildAnnotatedString {
            append(
                AnnotatedString(
                    text,
                    spanStyle = SpanStyle(
                        fontWeight = FontWeight.Bold,
                    )
                )
            )
        },
        modifier = modifier
            .border(dimensionResource(id = R.dimen.basic_border), MaterialTheme.colors.surface)
            .weight(weight)
            .padding(dimensionResource(id = R.dimen.dimen_8dp)),
        color = color
    )
}
