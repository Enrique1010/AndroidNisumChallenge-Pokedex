package com.erapps.pokedexapp.ui.screens.details.moves.movedetails

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.erapps.pokedexapp.R
import com.erapps.pokedexapp.data.api.models.moves.EffectEntry
import com.erapps.pokedexapp.data.api.models.moves.FlavorTextEntry
import com.erapps.pokedexapp.data.api.models.moves.MoveDetails
import com.erapps.pokedexapp.ui.shared.BackButtonBar
import com.erapps.pokedexapp.ui.shared.ErrorScreen
import com.erapps.pokedexapp.ui.shared.LoadingScreen
import com.erapps.pokedexapp.ui.shared.UiState
import com.erapps.pokedexapp.utils.Constants.ANIMPOKEMONSTATDURATION
import com.erapps.pokedexapp.utils.Constants.COLUMN1WEIGHT
import com.erapps.pokedexapp.utils.Constants.COLUMN2WEIGHT
import com.erapps.pokedexapp.utils.Constants.FILTERLANGUAGE
import com.erapps.pokedexapp.utils.Constants.MAXMOVESTAT
import com.erapps.pokedexapp.utils.getMoveStatColor
import com.erapps.pokedexapp.utils.getPokemonTypeToColor
import com.erapps.pokedexapp.utils.makeGoodTitle

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
                        .padding(dimensionResource(id = R.dimen.dimen_8dp))
                        .align(Alignment.TopCenter),
                    errorMessage = uiState.errorMessage,
                    errorStringResource = uiState.errorStringResource
                )
            }
            is UiState.Success<*> -> {
                val data = uiState.data as MoveDetails
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
                    MoveDetailsScreenContent(moveDetails = data, color = previousBackGroundColor)
                }
            }
            else -> {}
        }
        BackButtonBar(onBackPressed = onBackPressed)
    }

}

@Composable
fun MoveDetailsScreenContent(
    moveDetails: MoveDetails,
    color: Color
) {

    TypeSection(moveType = moveDetails.type.name)
    StatsSection(
        color = color,
        accuracy = moveDetails.accuracy,
        power = moveDetails.power,
        powerPoints = moveDetails.pp
    )
    EffectSection(
        effectEntries = moveDetails.effect_entries,
        effectChance = moveDetails.effect_chance
    )
    MoveEffectsTable(flavorEntries = moveDetails.flavor_text_entries, color = color)
}

@Composable
private fun TypeSection(
    modifier: Modifier = Modifier,
    moveType: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(
            horizontal = dimensionResource(id = R.dimen.dimen_16dp),
            vertical = dimensionResource(id = R.dimen.dimen_8dp)
        )
    ) {
        moveType.let { type ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .weight(1f)
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_8dp))
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.dimen_4dp)))
                    .background(getPokemonTypeToColor(type))
                    .height(dimensionResource(id = R.dimen.move_type_section_height))
            ) {
                Text(
                    text = type.capitalize(Locale.current),
                    color = Color.White,
                    fontSize = dimensionResource(id = R.dimen.move_type_section_text_size).value.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun StatsSection(
    modifier: Modifier = Modifier,
    color: Color,
    accuracy: Int?,
    power: Int?,
    powerPoints: Int
) {
    val stats = listOf(
        MoveStat(name = stringResource(id = R.string.accuracy_stat_text), value = accuracy),
        MoveStat(name = stringResource(id = R.string.power_stat_text), value = power)
    )

    Column(modifier = modifier.padding(
        horizontal = dimensionResource(id = R.dimen.dimen_16dp),
        vertical = dimensionResource(id = R.dimen.dimen_8dp)
    )
    ) {
        Row(
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.this_move_can_be_used_text1))
                    append(AnnotatedString("$powerPoints ", SpanStyle(color = color)))
                    append(stringResource(id = R.string.this_move_can_be_used_text2))
                },
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.dimen_8dp)))

            stats.forEachIndexed { i, stat ->
                stat.value?.let {
                    PokemonStat(
                        statName = stat.name,
                        statValue = it,
                        statColor = getMoveStatColor(stat.name.lowercase()),
                        animDelay = i * MAXMOVESTAT
                    )
                }
                Spacer(modifier = modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun PokemonStat(
    statName: String,
    statValue: Int,
    statColor: Color,
    height: Dp = dimensionResource(id = R.dimen.dimen_28dp),
    animDuration: Int = ANIMPOKEMONSTATDURATION,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue / MAXMOVESTAT.toFloat()
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
                text = (curPercent.value * MAXMOVESTAT).toInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun EffectSection(
    modifier: Modifier = Modifier,
    effectChance: Int?,
    effectEntries: List<EffectEntry>
) {
    val effects = remember {
        getFilteredEffectEntries(effectEntries)[0]
    }

    if (effects.effect.isNotEmpty() || effects.short_effect.isNotEmpty()) {
        Text(
            text = stringResource(id = R.string.effect_label),
            fontSize = dimensionResource(id = R.dimen.dimen_24dp).value.sp,
            fontWeight = FontWeight.Bold
        )
    }

    Column(
        modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.dimen_16dp))
    ) {
        when (effectChance) {
            null -> {
                EffectTextSection(effects)
            }
            else -> {
                if (effects.effect.contains(stringResource(id = R.string.effect_contains_text))) {
                    val effectWithChance =
                        effects.effect.replace(
                            stringResource(id = R.string.effect_replace_text),
                            "$effectChance"
                        )
                    val shortEffectWithChance =
                        effects.effect.replace(stringResource(id = R.string.effect_replace_text), "$effectChance")

                    EffectWithChanceTextSection(effectWithChance, modifier, shortEffectWithChance)
                }
            }
        }
    }
    Text(
        text = stringResource(id = R.string.move_effect_per_game_label),
        fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.dimen_4dp)))
}

@Composable
private fun EffectTextSection(
    effects: EffectEntry,
    modifier: Modifier = Modifier
) {
    Text(text = effects.effect, fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp)
    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.dimen_4dp)))
    Text(
        text = effects.short_effect,
        fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp,
        maxLines = 10,
        overflow = TextOverflow.Ellipsis
    )
    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.dimen_8dp)))
}

@Composable
private fun EffectWithChanceTextSection(
    effectWithChance: String,
    modifier: Modifier = Modifier,
    shortEffectWithChance: String
) {
    Text(text = effectWithChance, fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp)
    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.dimen_4dp)))
    Text(
        text = shortEffectWithChance,
        fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp,
        maxLines = 10,
        overflow = TextOverflow.Ellipsis
    )
    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.dimen_8dp)))
}

private fun getFilteredEffectEntries(list: List<EffectEntry>): List<EffectEntry> {

    return list.filter {
        it.language.name.contains(FILTERLANGUAGE)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MoveEffectsTable(
    modifier: Modifier = Modifier,
    flavorEntries: List<FlavorTextEntry>,
    color: Color
) {
    val entries = remember {
        getFilteredFlavorEntries(flavorEntries)
    }

    Column(modifier = modifier.padding(
        horizontal = dimensionResource(id = R.dimen.dimen_16dp),
        vertical = dimensionResource(id = R.dimen.dimen_8dp))
    ) {
        //header
        Row(
            modifier = modifier.background(color)
        ) {
            AnnotatedStringTableCell(
                text = stringResource(id = R.string.feature_table_game_label),
                weight = COLUMN1WEIGHT,
                color = MaterialTheme.colors.onBackground
            )
            AnnotatedStringTableCell(
                text = stringResource(id = R.string.feature_table_feature_label),
                weight = COLUMN2WEIGHT,
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
                items(entries) { flavorTexts ->
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnnotatedStringTableCell(
                            text = flavorTexts.version_group.name.makeGoodTitle(),
                            weight = COLUMN1WEIGHT,
                            color = color
                        )
                        TableCell(text = flavorTexts.flavor_text, weight = COLUMN2WEIGHT)
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

private fun getFilteredFlavorEntries(list: List<FlavorTextEntry>): List<FlavorTextEntry> {

    return list.filter {
        it.language.name.contains(FILTERLANGUAGE)
    }
}
