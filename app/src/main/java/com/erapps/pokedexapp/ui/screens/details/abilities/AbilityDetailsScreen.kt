package com.erapps.pokedexapp.ui.screens.details.abilities

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
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
import com.erapps.pokedexapp.data.api.models.abilities.AbilityDetails
import com.erapps.pokedexapp.data.api.models.abilities.EffectEntryX
import com.erapps.pokedexapp.data.api.models.abilities.FlavorTextEntry
import com.erapps.pokedexapp.ui.shared.BackButtonBar
import com.erapps.pokedexapp.ui.shared.ErrorScreen
import com.erapps.pokedexapp.ui.shared.LoadingScreen
import com.erapps.pokedexapp.ui.shared.UiState
import com.erapps.pokedexapp.utils.makeGoodTitle

const val COLUMN1WEIGHT = .4f
const val COLUMN2WEIGHT = .6f
const val FILTERLANGUAGE = "en"

@Composable
fun AbilityDetailsScreen(
    viewModel: AbilityDetailsViewModel = hiltViewModel(),
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
            is UiState.Success<*> -> {
                val data = uiState.data as AbilityDetails
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.dimen_8dp))
                        .background(MaterialTheme.colors.surface),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = data.name.makeGoodTitle(),
                        fontSize = dimensionResource(id = R.dimen.ability_details_title).value.sp,
                        fontWeight = FontWeight.Bold,
                        color = previousBackGroundColor
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_16dp)))
                    AbilityDetailsContent(
                        flavorTextEntries = data.flavor_text_entries,
                        effectEntries = data.effect_entries,
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
private fun AbilityDetailsContent(
    modifier: Modifier = Modifier,
    flavorTextEntries: List<FlavorTextEntry>,
    effectEntries: List<EffectEntryX>,
    color: Color,
) {

    val effects = remember {
        getFilteredEffectEntries(effectEntries)[0]
    }
    val flavorTexts = remember {
        getFilteredFlavorEntries(flavorTextEntries)
    }

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.dimen_8dp)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        EffectSection(effect = effects.effect, shortEffect = effects.short_effect)
        TableScreen(flavorTexts = flavorTexts, color = color)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TableScreen(
    modifier: Modifier = Modifier,
    flavorTexts: List<FlavorTextEntry>,
    color: Color,
) {

    val column1Weight = COLUMN1WEIGHT // 40%
    val column2Weight = COLUMN2WEIGHT // 60%
    //header
    Row(Modifier.background(color)) {
        AnnotatedStringTableCell(
            text = stringResource(id = R.string.feature_table_game_label),
            weight = column1Weight,
            color = MaterialTheme.colors.onBackground
        )
        AnnotatedStringTableCell(
            text = stringResource(id = R.string.feature_table_feature_label),
            weight = column2Weight,
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
            items(flavorTexts) { flavorTexts ->
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnnotatedStringTableCell(
                        text = flavorTexts.version_group.name.makeGoodTitle(),
                        weight = column1Weight,
                        color = color
                    )
                    TableCell(text = flavorTexts.flavor_text, weight = column2Weight)
                }
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
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
fun RowScope.AnnotatedStringTableCell(
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

@Composable
private fun EffectSection(
    modifier: Modifier = Modifier,
    effect: String,
    shortEffect: String
) {
    Text(
        text = stringResource(id = R.string.effect_label),
        fontSize = dimensionResource(id = R.dimen.dimen_24dp).value.sp,
        fontWeight = FontWeight.Bold
    )
    Text(text = effect, fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp)
    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.dimen_4dp)))
    Text(text = shortEffect, fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp)
    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.dimen_8dp)))
    Text(
        text = stringResource(id = R.string.feature_list_per_game_label),
        fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.dimen_4dp)))
}

private fun getFilteredEffectEntries(list: List<EffectEntryX>): List<EffectEntryX> {

    return list.filter {
        it.language.name.contains(FILTERLANGUAGE)
    }
}

private fun getFilteredFlavorEntries(list: List<FlavorTextEntry>): List<FlavorTextEntry> {

    return list.filter {
        it.language.name.contains(FILTERLANGUAGE)
    }
}

