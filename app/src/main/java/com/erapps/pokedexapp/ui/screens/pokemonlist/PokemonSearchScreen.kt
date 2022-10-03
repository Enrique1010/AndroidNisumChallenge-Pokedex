package com.erapps.pokedexapp.ui.screens.pokemonlist

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.erapps.pokedexapp.R
import com.erapps.pokedexapp.data.api.models.ShortPokemon
import com.erapps.pokedexapp.ui.shared.*
import com.erapps.pokedexapp.utils.getIdFromUrl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun PokemonListScreen(
    viewModel: PokemonSearchViewModel = hiltViewModel(),
    onPokemonClick: (String) -> Unit
) {

    //needed variables
    val uiState = viewModel.uiState.value
    val text = remember { mutableStateOf("") }
    val focused = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val status = getNetworkStatus()
    val context = LocalContext.current
    val scrollState = rememberLazyGridState()

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        snackbarHost = { ShowNetworkStatusSnackBar(snackbarHostState = it) },
        topBar = {

            SearchBar(
                query = text,
                onSearchClick = {
                    if (text.value.isNotEmpty()) {
                        viewModel.filterPokemonsByName(text.value)
                    }
                },
                onBack = {
                    text.value = ""
                    viewModel.filterPokemonsByName("")
                },
                focused = focused
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState) {
                UiState.Empty -> {
                    ScreenWithMessage(message = R.string.empty_text)
                }
                is UiState.Error -> {
                    ErrorScreen(
                        errorMessage = uiState.errorMessage,
                        errorStringResource = uiState.errorStringResource
                    )
                }
                is UiState.Success<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    ListOfPokemons(
                        list = uiState.data as List<ShortPokemon>,
                        onCardClick = { id ->
                            //only can go to details if internet is available
                            if (status) {
                                onPokemonClick(id)
                                return@ListOfPokemons
                            }
                            Toast.makeText(
                                context,
                                context.getString(R.string.cant_see_details_without_internet_text),
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        scrollState = scrollState
                    )
                }
                is UiState.Loading -> {
                    LoadingScreen()
                }
                else -> {
                    if (!status) {
                        ScreenWithMessage(message = R.string.search_something_wrong) {
                            viewModel.filterPokemonsByName("")
                        }
                    }
                }
            }
        }
    }

}

@ExperimentalCoroutinesApi
@Composable
fun ShowNetworkStatusSnackBar(snackbarHostState: SnackbarHostState) {

    val scope = rememberCoroutineScope()

    if (getNetworkStatus()) {
        snackbarHostState.currentSnackbarData?.dismiss()

    } else {
        val message = stringResource(id = R.string.error_no_internet)
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "",
                duration = SnackbarDuration.Indefinite
            )
        }

        AnimatedSnackBar(
            isConnected = true,
            snackbarHostState = snackbarHostState
        )
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun getNetworkStatus(): Boolean {
    val connection by connectivityState()
    return connection === NetworkState.Connected
}

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalAnimationApi
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: MutableState<String>,
    onSearchClick: () -> Unit,
    onBack: () -> Unit,
    focused: MutableState<Boolean>
) {

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AnimatedVisibility(visible = focused.value) {
            // Back button
            IconButton(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.dimen_4dp)),
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onBack()
                }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }

        CustomTextField(
            focused = focused,
            value = query,
            onSearchClick = {
                onSearchClick()
                keyboardController?.hide()
            }
        )
    }
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    focused: MutableState<Boolean>,
    value: MutableState<String>,
    onSearchClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focused.value = it.isFocused }
            .focusRequester(focusRequester),
        value = value.value,
        onValueChange = { value.value = it },
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_textfield_hint),
                color = MaterialTheme.colors.onBackground
            )
        },
        singleLine = true,
        trailingIcon = {
            if (focused.value) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        tint = MaterialTheme.colors.primary,
                        contentDescription = null
                    )
                }
            }
        },
        shape = RoundedCornerShape(4.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions { onSearchClick() },
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onBackground,
            disabledTextColor = Color.Transparent,
            backgroundColor = MaterialTheme.colors.onSurface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun ListOfPokemons(
    list: List<ShortPokemon>,
    onCardClick: (String) -> Unit,
    scrollState: LazyGridState
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = scrollState
    ) {
        items(list) { pokemon ->
            ListOfPokemonsItem(pokemon = pokemon) { onCardClick(pokemon.name) }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListOfPokemonsItem(
    modifier: Modifier = Modifier,
    pokemon: ShortPokemon,
    onCardClick: () -> Unit
) {
    val defaultColor = MaterialTheme.colors.primary
    val cardMainColor = remember { mutableStateOf(defaultColor) }

    Card(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.dimen_4dp))
            .wrapContentSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        cardMainColor.value,
                        MaterialTheme.colors.surface
                    )
                )
            ),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_4dp)),
        elevation = dimensionResource(id = R.dimen.card_elevation),
        border = BorderStroke(
            dimensionResource(id = R.dimen.card_border_stroke),
            brush = Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colors.surface,
                    MaterialTheme.colors.surface.copy(alpha = 1f)
                )
            )
        ),
        onClick = onCardClick
    ) {
        Column(
            modifier = modifier.background(
                Brush.verticalGradient(
                    listOf(
                        cardMainColor.value,
                        MaterialTheme.colors.surface
                    )
                )
            )
        ) {
            ImageSection(
                imageUrl = getPokemonImage(pokemon.url.getIdFromUrl().toInt()),
                cardMainColor = cardMainColor
            )
            TitleSection(pokemon = pokemon)
        }
    }
}

@Composable
private fun TitleSection(
    modifier: Modifier = Modifier,
    pokemon: ShortPokemon
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.dimen_4dp)),
        text = pokemon.name.capitalize(Locale.current),
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onBackground,
        textAlign = TextAlign.Center,
        fontSize = dimensionResource(id = R.dimen.dimen_24dp).value.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun ImageSection(
    modifier: Modifier = Modifier,
    imageUrl: String,
    cardMainColor: MutableState<Color>
) {
    val context = LocalContext.current

    SubcomposeAsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .size(
                dimensionResource(id = R.dimen.card_image_width),
                dimensionResource(id = R.dimen.card_image_height)
            ),
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .placeholder(R.drawable.pokemon_face)
            .error(R.drawable.pokemon_face)
            .crossfade(true)
            .build(),
        contentDescription = null,
        alignment = Alignment.TopCenter,
        loading = { LinearProgressIndicator() },
        onSuccess = { painter ->
            getImageDominantColor(cardMainColor, painter.result.drawable)
        }
    )
}