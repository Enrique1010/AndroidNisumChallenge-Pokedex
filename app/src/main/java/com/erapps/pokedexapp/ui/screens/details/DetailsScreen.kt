package com.erapps.pokedexapp.ui.screens.details

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.erapps.pokedexapp.R
import com.erapps.pokedexapp.data.api.models.Pokemon
import com.erapps.pokedexapp.data.api.models.pokemon.Ability
import com.erapps.pokedexapp.data.api.models.pokemon.Species
import com.erapps.pokedexapp.data.api.models.pokemon.Stat
import com.erapps.pokedexapp.data.api.models.pokemon.Type
import com.erapps.pokedexapp.ui.screens.details.moves.MovesScreenSection
import com.erapps.pokedexapp.ui.screens.getNetworkStatus
import com.erapps.pokedexapp.ui.shared.*
import com.erapps.pokedexapp.utils.Constants.ANIMPOKEMONSTATDURATION
import com.erapps.pokedexapp.utils.Constants.DATASECTIONWEIGHT
import com.erapps.pokedexapp.utils.Constants.IMAGEMAXWEIGHT
import com.erapps.pokedexapp.utils.Constants.MAXMOVESTAT
import com.erapps.pokedexapp.utils.Constants.POKEMONBASEFORCALCULATION1
import com.erapps.pokedexapp.utils.Constants.POKEMONBASEFORCALCULATION2
import com.erapps.pokedexapp.utils.abbrStat
import com.erapps.pokedexapp.utils.getPokemonStatToColor
import com.erapps.pokedexapp.utils.getPokemonTypeToColor
import com.erapps.pokedexapp.utils.makeGoodTitle
import kotlin.math.round

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailsScreenViewModel = hiltViewModel(),
    onAbilityClick: (String, Int) -> Unit,
    onEncounterSectionClick: (String, Int) -> Unit,
    onSpecieSectionClick: (String, Int) -> Unit,
    onMoveClick: (String, Int) -> Unit,
    onBackPressed: () -> Unit
) {

    val uiState = viewModel.uiState.value
    val scaffoldState = rememberBottomSheetScaffoldState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colors.surface,
                        viewModel.backGroundColor.value,
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
                    modifier = modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                        .padding(8.dp)
                        .align(Alignment.TopCenter),
                    errorMessage = uiState.errorMessage,
                    errorStringResource = uiState.errorStringResource
                )
            }
            is UiState.Success<*> -> {
                DetailsScreenContent(
                    modifier = modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                        .padding(16.dp)
                        .align(Alignment.TopCenter),
                    pokemon = uiState.data as Pokemon,
                    backGroundColor = viewModel.backGroundColor,
                    onAbilityClick = onAbilityClick,
                    onEncounterSectionClick = onEncounterSectionClick,
                    onSpecieSectionClick = onSpecieSectionClick,
                    onMoveClick = onMoveClick,
                    scaffoldState = scaffoldState
                )
            }
            else -> {}
        }
        if (scaffoldState.bottomSheetState.isCollapsed) {
            BackButtonBar(modifier, onBackPressed)
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DetailsScreenContent(
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState,
    pokemon: Pokemon,
    onAbilityClick: (String, Int) -> Unit,
    onEncounterSectionClick: (String, Int) -> Unit,
    onSpecieSectionClick: (String, Int) -> Unit,
    onMoveClick: (String, Int) -> Unit,
    backGroundColor: MutableState<Color>
) {

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetContent = {
            MovesScreenSection(
                previousBackGroundColor = backGroundColor,
                moves = pokemon.moves,
                isExpanded = scaffoldState.bottomSheetState.isExpanded,
                onMoveClick = onMoveClick
            )
        }
    ) {
        Box(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colors.surface)
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ImageSection(
                    imageUrl = pokemon.sprites.front_default,
                    backGroundColor = backGroundColor
                )
                TitleSection(title = pokemon.name.capitalize(Locale.current))
                PokemonTypeSection(types = pokemon.types)
                PokemonDetailDataSection(
                    pokemonWeight = pokemon.weight,
                    pokemonHeight = pokemon.height
                )
                PokemonBaseStats(stats = pokemon.stats)
                AbilitiesSection(
                    abilities = pokemon.abilities,
                    onAbilityClick = onAbilityClick,
                    backGroundColor = backGroundColor
                )
                EncountersSection(
                    encountersUrl = pokemon.location_area_encounters,
                    backGroundColor = backGroundColor,
                    onEncounterSectionClick = onEncounterSectionClick
                )
                PokemonSpecieSection(
                    species = pokemon.species,
                    backGroundColor = backGroundColor,
                    onSpecieSectionClick = onSpecieSectionClick
                )
            }
        }
    }
}

@Composable
private fun ImageSection(
    modifier: Modifier = Modifier,
    imageUrl: String,
    backGroundColor: MutableState<Color>
) {
    val context = LocalContext.current

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .error(R.drawable.pokemon_face)
            .placeholder(R.drawable.pokemon_face)
            .build(),
        contentDescription = null,
        loading = { CircularProgressIndicator() },
        alignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxWidth(IMAGEMAXWEIGHT)
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.dimen_4dp))),
        contentScale = ContentScale.Crop,
        onSuccess = { painter ->
            getImageDominantColor(backGroundColor, painter.result.drawable)
        }
    )
}

@Composable
private fun TitleSection(
    modifier: Modifier = Modifier,
    title: String
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(id = R.dimen.dimen_32dp).value.sp,
            color = MaterialTheme.colors.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PokemonTypeSection(types: List<Type>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.dimen_16dp))
    ) {
        types.forEach { type ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(DATASECTIONWEIGHT)
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_8dp))
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.dimen_4dp)))
                    .background(getPokemonTypeToColor(type.type.name))
                    .height(dimensionResource(id = R.dimen.type_section_height))
            ) {
                Text(
                    text = type.type.name.capitalize(Locale.current),
                    color = Color.White,
                    fontSize = dimensionResource(id = R.dimen.type_section_text_size).value.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PokemonDetailDataSection(
    pokemonWeight: Int,
    pokemonHeight: Int,
    sectionHeight: Dp = dimensionResource(id = R.dimen.dimen_32dp)
) {
    val pokemonWeightInKg = remember {
        round(pokemonWeight * POKEMONBASEFORCALCULATION1) / POKEMONBASEFORCALCULATION2
    }
    val pokemonHeightInMeters = remember {
        round(pokemonHeight * POKEMONBASEFORCALCULATION1) / POKEMONBASEFORCALCULATION2
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        PokemonDetailDataItem(
            dataValue = pokemonWeightInKg,
            dataUnit = stringResource(id = R.string.kg_unit),
            dataIcon = Icons.Default.LineWeight,
            modifier = Modifier.weight(DATASECTIONWEIGHT)
        )
        Spacer(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.basic_border), sectionHeight)
                .background(MaterialTheme.colors.onBackground)
        )
        PokemonDetailDataItem(
            dataValue = pokemonHeightInMeters,
            dataUnit = stringResource(id = R.string.meter_unit),
            dataIcon = Icons.Default.Height,
            modifier = Modifier.weight(DATASECTIONWEIGHT)
        )
    }
}

@Composable
private fun PokemonDetailDataItem(
    dataValue: Float,
    dataUnit: String,
    dataIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            imageVector = dataIcon,
            contentDescription = null,
            tint = MaterialTheme.colors.onBackground
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_8dp)))
        Text(
            text = "$dataValue$dataUnit",
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
private fun PokemonBaseStats(
    stats: List<Stat>,
    animDelayPerItem: Int = MAXMOVESTAT
) {
    val maxBaseStat = remember {
        stats.maxOf { it.base_stat }
    }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_8dp)))

        stats.forEachIndexed { i, stat ->
            PokemonStat(
                statName = abbrStat(stat),
                statValue = stat.base_stat,
                statMaxValue = maxBaseStat,
                statColor = getPokemonStatToColor(stat),
                animDelay = i * animDelayPerItem
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_8dp)))
        }
    }
}

@Composable
private fun PokemonStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
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
            statValue / statMaxValue.toFloat()
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
            .clip(RoundedCornerShape(4.dp))
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
                .clip(RoundedCornerShape(4.dp))
                .background(statColor)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = (curPercent.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AbilitiesSection(
    modifier: Modifier = Modifier,
    abilities: List<Ability>,
    onAbilityClick: (String, Int) -> Unit,
    backGroundColor: MutableState<Color>
) {
    var expandAbilitiesSection by rememberSaveable {
        mutableStateOf(false)
    }

    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.dimen_8dp)))
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expandAbilitiesSection = !expandAbilitiesSection },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.ability_section_label),
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
        )
        Icon(
            modifier = modifier.clickable { expandAbilitiesSection = !expandAbilitiesSection },
            imageVector = if (!expandAbilitiesSection) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
            contentDescription = null
        )
    }
    AnimatedVisibility(
        visible = expandAbilitiesSection,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        AbilityItem(abilities, modifier, backGroundColor, onAbilityClick)
    }
}

@Composable
private fun AbilityItem(
    abilities: List<Ability>,
    modifier: Modifier,
    backGroundColor: MutableState<Color>,
    onAbilityClick: (String, Int) -> Unit
) {
    val context = LocalContext.current
    val status = getNetworkStatus()

    Column {
        abilities.forEach { ability ->
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.dimen_8dp))
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
                        onAbilityClick(ability.ability.url, backGroundColor.value.toArgb())
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        tint = backGroundColor.value,
                        contentDescription = null
                    )
                    Spacer(modifier = modifier.width(dimensionResource(id = R.dimen.dimen_8dp)))
                    Text(text = ability.ability.name.makeGoodTitle())
                }
                Icon(imageVector = Icons.Outlined.NavigateNext, contentDescription = null)
            }
        }
    }
}

@Composable
private fun EncountersSection(
    modifier: Modifier = Modifier,
    encountersUrl: String,
    backGroundColor: MutableState<Color>,
    onEncounterSectionClick: (String, Int) -> Unit,
) {
    val context = LocalContext.current
    val status = getNetworkStatus()

    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.dimen_8dp)))
    Row(
        modifier = modifier
            .fillMaxWidth()
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
                onEncounterSectionClick(encountersUrl, backGroundColor.value.toArgb())
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.encounters_row_text),
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
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
                onEncounterSectionClick(encountersUrl, backGroundColor.value.toArgb())
            },
            imageVector = Icons.Default.NavigateNext,
            contentDescription = null
        )
    }
}

@Composable
private fun PokemonSpecieSection(
    modifier: Modifier = Modifier,
    species: Species,
    backGroundColor: MutableState<Color>,
    onSpecieSectionClick: (String, Int) -> Unit,
) {
    val context = LocalContext.current
    val status = getNetworkStatus()

    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.dimen_8dp)))
    Row(
        modifier = modifier
            .fillMaxWidth()
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
                onSpecieSectionClick(species.url, backGroundColor.value.toArgb())
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = buildAnnotatedString {
                append(stringResource(id = R.string.specie_row_text))
                append(" ${species.name.makeGoodTitle()}")
            },
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
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
                onSpecieSectionClick(species.url, backGroundColor.value.toArgb())
            },
            imageVector = Icons.Default.NavigateNext,
            contentDescription = null
        )
    }
}