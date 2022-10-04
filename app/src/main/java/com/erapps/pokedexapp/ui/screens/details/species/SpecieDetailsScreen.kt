package com.erapps.pokedexapp.ui.screens.details.species

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwipeDown
import androidx.compose.material.icons.filled.SwipeUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.erapps.pokedexapp.R
import com.erapps.pokedexapp.data.api.models.species.EvolutionChain
import com.erapps.pokedexapp.data.api.models.species.FlavorTextEntry
import com.erapps.pokedexapp.data.api.models.species.SpeciesDetails
import com.erapps.pokedexapp.data.api.models.species.evolutionchain.EvolutionChainDetails
import com.erapps.pokedexapp.ui.shared.DetailsPageWithState
import com.erapps.pokedexapp.ui.shared.SpacedDivider
import com.erapps.pokedexapp.utils.Constants
import com.erapps.pokedexapp.utils.Constants.EVOLUTIONCHAINTABLEPERCENT
import com.erapps.pokedexapp.utils.getSpecialConditionColor
import com.erapps.pokedexapp.utils.makeGoodTitle

@Composable
fun SpecieDetailsScreen(
    viewModel: SpecieDetailsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val uiState = viewModel.uiState.value
    val previousBackGroundColor = viewModel.backGroundColor.value

    DetailsPageWithState<SpeciesDetails>(
        previousBackGroundColor = previousBackGroundColor,
        uiState = uiState,
        onBackPressed = onBackPressed
    ) { data ->
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
            SpecieDetailsContent(
                specieDetails = data,
                viewModel = viewModel,
                color = previousBackGroundColor
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SpecieDetailsContent(
    modifier: Modifier = Modifier,
    specieDetails: SpeciesDetails,
    color: Color,
    viewModel: SpecieDetailsViewModel
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetContent = {
            EvolutionChainPage(
                viewModel = viewModel,
                scaffoldState = scaffoldState,
                evolutionChain = specieDetails.evolution_chain,
                color = color
            )
        }
    ) {
        PokemonInfoSection(specieDetails = specieDetails, color = color)
        //stats (base happiness, capture rate)
        PokemonSpecieStatsSection(
            happiness = specieDetails.base_happiness,
            captureRate = specieDetails.capture_rate,
            color = color
        )
        DescriptionSection(flavorEntries = specieDetails.flavor_text_entries, color = color)
    }
}

@Composable
fun DescriptionSection(
    modifier: Modifier = Modifier,
    flavorEntries: List<FlavorTextEntry>,
    color: Color
) {
    val description = remember {
        getFilteredEffectEntries(flavorEntries)[0]
    }

    SpacedDivider(modifier = modifier, color = color)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.dimen_16dp))
    ) {
        Text(
            text = stringResource(id = R.string.description_label),
            fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = description.flavor_text.replace("\n", " "),
            fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp,
            maxLines = 10,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private fun getFilteredEffectEntries(list: List<FlavorTextEntry>): List<FlavorTextEntry> {

    return list.filter {
        it.language.name.contains(Constants.FILTERLANGUAGE)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EvolutionChainPage(
    viewModel: SpecieDetailsViewModel,
    color: Color,
    scaffoldState: BottomSheetScaffoldState,
    evolutionChain: EvolutionChain
) {

    LaunchedEffect(evolutionChain.url.isNotEmpty()) {
        viewModel.getEvolutionChain(evolutionChain.url)
    }

    viewModel.evolutionChain.value?.let {
        EvolutionHeaderSection(isExpanded = scaffoldState.bottomSheetState.isExpanded)
        EvolutionTableSection(evolutionChainDetails = it, color = color)
        return
    }
}

@Composable
fun EvolutionHeaderSection(
    modifier: Modifier = Modifier,
    isExpanded: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = modifier.padding(dimensionResource(id = R.dimen.dimen_16dp)),
            text = stringResource(id = R.string.evolution_chain),
            fontSize = dimensionResource(id = R.dimen.common_screen_font_size).value.sp,
            fontWeight = FontWeight.Bold
        )
        Icon(
            modifier = modifier.padding(dimensionResource(id = R.dimen.dimen_16dp)),
            imageVector = if (!isExpanded) Icons.Default.SwipeUp else Icons.Default.SwipeDown,
            contentDescription = null
        )
    }
    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.dimen_8dp)))
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EvolutionTableSection(
    modifier: Modifier = Modifier,
    color: Color,
    evolutionChainDetails: EvolutionChainDetails
) {
    val chain = evolutionChainDetails.chain

    Column {
        //header
        if (chain.evolves_to.isNotEmpty()) {
            Row(
                modifier = modifier.background(color)
            ) {
                AnnotatedStringTableCell(
                    text = stringResource(id = R.string.form1),
                    weight = EVOLUTIONCHAINTABLEPERCENT,
                    color = MaterialTheme.colors.onBackground
                )
                AnnotatedStringTableCell(
                    text = stringResource(id = R.string.form2),
                    weight = EVOLUTIONCHAINTABLEPERCENT,
                    color = MaterialTheme.colors.onBackground
                )
                AnnotatedStringTableCell(
                    text = stringResource(id = R.string.form3),
                    weight = EVOLUTIONCHAINTABLEPERCENT,
                    color = MaterialTheme.colors.onBackground
                )
            }
        } else {
            Row(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.dimen_8dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.this_pokemon_has_not_evolution_chain),
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        //table
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            LazyColumn(
                modifier
                    .padding(dimensionResource(id = R.dimen.dimen_4dp))
                    .background(MaterialTheme.colors.surface)
            ) {
                items(chain.evolves_to) { evolveTo ->
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TableCell(
                            text = chain.species.name.makeGoodTitle(),
                            EVOLUTIONCHAINTABLEPERCENT
                        )
                        TableCell(
                            text = evolveTo.species.name.makeGoodTitle(),
                            EVOLUTIONCHAINTABLEPERCENT
                        )
                        val text = if (evolveTo.evolves_to.isNotEmpty()) {
                            evolveTo.evolves_to[0].species.name.makeGoodTitle()
                        } else {
                            stringResource(id = R.string.none)
                        }
                        TableCell(
                            text = text,
                            EVOLUTIONCHAINTABLEPERCENT
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
            .weight(weight)
            .padding(dimensionResource(id = R.dimen.dimen_8dp)),
        color = color
    )
}

@Composable
fun PokemonSpecieStatsSection(
    modifier: Modifier = Modifier,
    happiness: Int,
    captureRate: Int,
    color: Color
) {

    val stats = listOf(
        SpecieStat(name = stringResource(id = R.string.happiness), value = happiness),
        SpecieStat(name = stringResource(id = R.string.captura_rate), value = captureRate)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.dimen_16dp),
                vertical = dimensionResource(id = R.dimen.dimen_8dp)
            )
    ) {
        Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.dimen_8dp)))

        stats.forEachIndexed { i, stat ->
            stat.value?.let {
                PokemonStat(
                    statName = stat.name,
                    statValue = it,
                    statColor = color,
                    animDelay = i * Constants.MAXMOVESTAT
                )
            }
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.dimen_8dp)))
        }
    }
}

@Composable
private fun PokemonStat(
    statName: String,
    statValue: Int,
    statColor: Color,
    height: Dp = dimensionResource(id = R.dimen.dimen_28dp),
    animDuration: Int = Constants.ANIMPOKEMONSTATDURATION,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue / Constants.MAXSPECIERATE.toFloat()
        } else 0f,
        animationSpec = tween(
            animDuration,
            animDelay
        )
    )
    LaunchedEffect(true) {
        animationPlayed = true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.dimen_4dp)))
            .background(
                if (isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else {
                    Color.LightGray
                }
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(curPercent.value)
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.dimen_4dp)))
                .background(statColor)
                .padding(horizontal = dimensionResource(id = R.dimen.dimen_8dp))
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = (curPercent.value * Constants.MAXSPECIERATE).toInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PokemonInfoSection(
    modifier: Modifier = Modifier,
    specieDetails: SpeciesDetails,
    color: Color
) {

    val listOfSpecialCondition = mutableListOf<SpecialCondition>()
    when {
        specieDetails.is_legendary -> {
            listOfSpecialCondition.add(SpecialCondition(true, "legendary"))
        }
        specieDetails.is_mythical -> {
            listOfSpecialCondition.add(SpecialCondition(true, "mythical"))
        }
        specieDetails.is_baby -> {
            listOfSpecialCondition.add(SpecialCondition(true, "baby"))
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpecialConditionSection(specialConditions = listOfSpecialCondition)
        VarietiesSection(modifier, specieDetails, color)
        PokemonBasicInfoSection(modifier, specieDetails, color)
    }
}

@Composable
private fun PokemonBasicInfoSection(
    modifier: Modifier = Modifier,
    specieDetails: SpeciesDetails,
    color: Color
) {
    SpacedDivider(modifier, color)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.dimen_16dp)),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = buildAnnotatedString {
                append(
                    AnnotatedString(
                        stringResource(id = R.string.color_label),
                        SpanStyle(color = color)
                    )
                )
                append(" ${specieDetails.color.name.makeGoodTitle()}")
            },
            fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = buildAnnotatedString {
                append(
                    AnnotatedString(
                        stringResource(id = R.string.grow_rate_label),
                        SpanStyle(color = color)
                    )
                )
                append(" ${specieDetails.growth_rate.name.makeGoodTitle()}")
            },
            fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = buildAnnotatedString {
                append(
                    AnnotatedString(
                        stringResource(id = R.string.shape_label),
                        SpanStyle(color = color)
                    )
                )
                append(" ${specieDetails.shape.name.makeGoodTitle()}")
            },
            fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = buildAnnotatedString {
                append(
                    AnnotatedString(
                        stringResource(id = R.string.habitat_label),
                        SpanStyle(color = color)
                    )
                )
                append(" ${specieDetails.habitat.name.makeGoodTitle()}")
            },
            fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = buildAnnotatedString {
                append(
                    AnnotatedString(
                        stringResource(id = R.string.generation_label),
                        SpanStyle(color = color)
                    )
                )
                append(" ${specieDetails.generation.name.makeGoodTitle()}")
            },
            fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp,
            fontWeight = FontWeight.Bold
        )
    }
    SpacedDivider(modifier, color)
}

@Composable
private fun SpecialConditionSection(
    modifier: Modifier = Modifier,
    specialConditions: List<SpecialCondition>
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.dimen_16dp))
    ) {
        specialConditions.forEach { specialCondition ->
            Row(
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.dimen_4dp))
                    .shadow(
                        dimensionResource(id = R.dimen.basic_border),
                        RoundedCornerShape(dimensionResource(id = R.dimen.rounded_corner_shape_basic))
                    )
                    .background(getSpecialConditionColor(specialCondition.conditionName)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = modifier.padding(
                        vertical = dimensionResource(id = R.dimen.dimen_8dp),
                        horizontal = dimensionResource(id = R.dimen.dimen_4dp)
                    ),
                    text = specialCondition.conditionName.makeGoodTitle(),
                    fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun VarietiesSection(
    modifier: Modifier,
    specieDetails: SpeciesDetails,
    color: Color
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.dimen_16dp))
    ) {
        Text(
            text = stringResource(id = R.string.varieties_label),
            fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp,
            fontWeight = FontWeight.Bold
        )
        LazyRow {
            items(specieDetails.varieties) { variety ->
                Row(
                    modifier = modifier
                        .padding(dimensionResource(id = R.dimen.dimen_4dp))
                        .shadow(
                            dimensionResource(id = R.dimen.basic_border),
                            RoundedCornerShape(dimensionResource(id = R.dimen.rounded_corner_shape_basic))
                        )
                        .background(color),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = modifier.padding(
                            vertical = dimensionResource(id = R.dimen.dimen_8dp),
                            horizontal = dimensionResource(id = R.dimen.dimen_4dp)
                        ),
                        text = " ${ variety.pokemon.name.makeGoodTitle() }",
                        fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
