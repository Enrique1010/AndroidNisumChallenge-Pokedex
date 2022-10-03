package com.erapps.pokedexapp.ui.screens.details.abilities

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.erapps.pokedexapp.R
import com.erapps.pokedexapp.data.api.models.abilities.AbilityDetails
import com.erapps.pokedexapp.data.api.models.abilities.EffectEntryX
import com.erapps.pokedexapp.data.api.models.abilities.FlavorTextEntry
import com.erapps.pokedexapp.ui.shared.ErrorScreen
import com.erapps.pokedexapp.ui.shared.LoadingScreen
import com.erapps.pokedexapp.ui.shared.UiState
import com.erapps.pokedexapp.utils.makeGoodTitle

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
                        .padding(16.dp)
                        .background(MaterialTheme.colors.surface),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = data.name.makeGoodTitle(),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = previousBackGroundColor
                    )
                    Spacer(modifier = Modifier.height(16.dp))
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
private fun BackButtonBar(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
) {
    Box(
        modifier = modifier
            .background(Color.Transparent),
        contentAlignment = Alignment.TopStart
    ) {
        Icon(
            modifier = modifier
                .size(32.dp)
                .offset(8.dp, 8.dp)
                .clickable { onBackPressed() },
            imageVector = Icons.Default.ArrowBack,
            tint = MaterialTheme.colors.onBackground,
            contentDescription = null
        )
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
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        EffectSection(effect = effects.effect, shortEffect = effects.short_effect)
        AbilitiesResultsSection(flavorTexts = flavorTexts, color = color)
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun AbilitiesResultsSection(
    modifier: Modifier = Modifier,
    flavorTexts: List<FlavorTextEntry>,
    color: Color,
) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyColumn {
            items(flavorTexts) { flavorText ->
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = modifier.width(8.dp))
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            modifier = modifier.fillMaxWidth(),
                            text = buildAnnotatedString {
                                append(
                                    AnnotatedString(
                                        "Game: ",
                                        spanStyle = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = color
                                        )
                                    )
                                )
                                append(
                                    AnnotatedString(
                                        flavorText.version_group.name.makeGoodTitle(),
                                        spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                                    )
                                )
                            }
                        )
                        Text(
                            text = buildAnnotatedString {
                                append(
                                    AnnotatedString(
                                        "Result: ",
                                        spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                                    )
                                )
                                append(flavorText.flavor_text)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EffectSection(
    modifier: Modifier = Modifier,
    effect: String,
    shortEffect: String
) {
    Text(
        text = stringResource(id = R.string.effect_label),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
    Text(text = effect, fontSize = 20.sp)
    Spacer(modifier = modifier.height(4.dp))
    Text(text = shortEffect, fontSize = 20.sp)
    Spacer(modifier = modifier.height(8.dp))
    Text(
        text = stringResource(id = R.string.results_in_label),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
}

private fun getFilteredEffectEntries(list: List<EffectEntryX>): List<EffectEntryX> {

    return list.filter {
        it.language.name.contains("en")
    }
}

private fun getFilteredFlavorEntries(list: List<FlavorTextEntry>): List<FlavorTextEntry> {

    return list.filter {
        it.language.name.contains("en")
    }
}

